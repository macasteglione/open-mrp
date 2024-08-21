package unpsjb.labprog.backend.business;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import unpsjb.labprog.backend.model.Equipo;
import unpsjb.labprog.backend.model.Estado;
import unpsjb.labprog.backend.model.Hueco;
import unpsjb.labprog.backend.model.PedidoFabricacion;
import unpsjb.labprog.backend.model.Planificacion;
import unpsjb.labprog.backend.model.Producto;
import unpsjb.labprog.backend.model.Registro;
import unpsjb.labprog.backend.model.Taller;
import unpsjb.labprog.backend.model.Tarea;

@Service
public class PlanificadorTardioService {
    @Autowired
    private PedidoFabricacionService pedidoFabricacionService;
    @Autowired
    private PlanificacionService planificacionService;
    @Autowired
    private TallerPertinenteService tallerPertinenteService;
    @Autowired
    private PlanificacionHuecoService planificacionHuecoService;
    @Autowired
    private RegistroService registroService;

    public void planificarTodo(Timestamp fecha) {
        pedidoFabricacionService.findAllOrderByFechaEntrega(fecha)
                .forEach(this::planificarPedido);
    }

    public PedidoFabricacion planificarPedido(PedidoFabricacion pedido) {
        if (pedido == null)
            throw new PedidoNotFoundException();

        if (pedido.getEstado() == Estado.PLANIFICADO || pedido.getEstado() == Estado.FINALIZADO) {
            registroService.save(
                    new Registro("El pedido con id " + pedido.getId() + " no se pudo planificar debido a su estado"));
            return pedido;
        }

        boolean planificado = false;
        for (Taller taller : tallerPertinenteService.talleresPorEficiencia(pedido.getProducto())) {
            try {
                planificado = planificarProductoT(pedido, taller);
                if (planificado) {
                    registroService.save(new Registro("Se planificó el pedido con id " + pedido.getId()
                            + " en el taller: " + taller.getNombre() + " con esquema de entrega tardía"));
                    break;
                }
            } catch (NoPlanificableException ex) {
                planificacionHuecoService.rollback(pedido.getPlanificaciones());
                pedido.getPlanificaciones().clear();
            }
        }

        if (planificado) {
            pedido.setEstado(Estado.PLANIFICADO);
            planificacionService.saveAll(pedido.getPlanificaciones());
        } else {
            pedido.setEstado(Estado.NO_PLANIFICABLE);
            registroService.save(new Registro(
                    "No pudo planificarse el pedido con id " + pedido.getId() + " con esquema de entrega tardía"));
        }

        registroService.create(new Registro(
                "Pedido con id " + pedido.getId() + " solicitado el " + pedido.getFechaPedido() + " a entregar el "
                        + pedido.getFechaEntrega() + " con estado: " + pedido.getEstado()));

        return pedido;
    }

    @Transactional(rollbackFor = NoPlanificableException.class)
    public boolean planificarProductoT(PedidoFabricacion pedido, Taller taller) {
        List<Tarea> tareas = new ArrayList<>(ordenarTareasT(pedido.getProducto()));
        Timestamp fechaTrabajo;

        for (int i = 0; i < pedido.getCantidad(); i++) {
            fechaTrabajo = pedido.getFechaEntrega();
            for (Tarea tarea : tareas) {
                fechaTrabajo = planificarTareaT(pedido, taller, tarea, fechaTrabajo);
                if (fechaTrabajo.before(pedido.getFechaPedido())) {
                    throw new NoPlanificableException();
                }
            }
        }
        return true;
    }

    public Timestamp planificarTareaT(PedidoFabricacion pedido, Taller taller, Tarea tarea, Timestamp fechaTrabajo) {
        Planificacion planificacion = generarPlanificacionT(tarea, taller, fechaTrabajo, pedido);
        if (planificacion == null)
            throw new NoPlanificableException();

        pedido.getPlanificaciones().add(planificacion);
        return planificacion.getInicio();
    }

    private Planificacion generarPlanificacionT(Tarea tarea, Taller taller, Timestamp fechaTrabajo,
            PedidoFabricacion pedido) {
        List<Hueco> huecos = new ArrayList<>(planificacionHuecoService.huecosTallerTipo(tarea, taller));
        huecos.sort(Comparator.comparing(Hueco::getFin).reversed());

        Hueco hueco = primerLugar(tarea, taller, fechaTrabajo);
        if (hueco == null)
            return null;

        Planificacion planificacion = new Planificacion();
        planificacion.setEquipo(hueco.getEquipo());
        planificacion.setTarea(tarea);
        planificacion.setFin(new Timestamp(Math.min(hueco.getFin().getTime(), fechaTrabajo.getTime())));
        planificacion.setInicio(calcularFechaInicio(hueco.getEquipo(), tarea, planificacion.getFin()));
        planificacion.setPedido(pedido);

        planificacionHuecoService.actualizarHuecos(planificacion.getInicio(), planificacion.getFin(), hueco);

        return planificacion;
    }

    private boolean fits(Timestamp taskStart, Timestamp gapStart) {
        return !taskStart.before(gapStart);
    }

    public boolean isUsefulGap(Hueco actualGap, Timestamp lastTaskDate, Timestamp taskStart, Timestamp taskStart2) {
        return (actualGap.getFin().before(lastTaskDate) && fits(taskStart, actualGap.getInicio()))
                || (lastTaskDate.after(actualGap.getInicio()) && lastTaskDate.before(actualGap.getFin())
                        && fits(taskStart2, actualGap.getInicio()));
    }

    private Timestamp minusSeconds(Timestamp from, long durationInSeconds) {
        return Timestamp.valueOf(from.toLocalDateTime().minusSeconds(durationInSeconds));
    }

    private Timestamp getStartDate(Timestamp from, long taskTime, double equipmentCapacity) {
        long durationInSeconds = (long) ((taskTime / equipmentCapacity) * 60);
        return minusSeconds(from, durationInSeconds);
    }

    private Hueco primerLugar(Tarea tarea, Taller taller, Timestamp finAnterior) {
        List<Hueco> huecos = new ArrayList<>(planificacionHuecoService.huecosTallerTipo(tarea, taller));
        huecos.sort(Comparator.comparing(Hueco::getFin).reversed());

        for (Hueco actualGap : huecos) {
            Timestamp taskStart = getStartDate(actualGap.getFin(), tarea.getTiempo(),
                    actualGap.getEquipo().getCapacidad());
            Timestamp taskStart2 = getStartDate(finAnterior, tarea.getTiempo(), actualGap.getEquipo().getCapacidad());

            if (isUsefulGap(actualGap, finAnterior, taskStart, taskStart2)) {
                Hueco result = new Hueco();
                result.setFin(new Timestamp(Math.min(actualGap.getFin().getTime(), finAnterior.getTime())));
                result.setInicio(
                        getStartDate(result.getFin(), tarea.getTiempo(), actualGap.getEquipo().getCapacidad()));
                result.setEquipo(actualGap.getEquipo());

                planificacionHuecoService.actualizarHuecos(result.getInicio(), result.getFin(), actualGap);
                return result;
            }
        }
        return null;
    }

    public Timestamp calcularFechaInicio(Equipo equipo, Tarea tarea, Timestamp fecha) {
        long duracion = calcularDuracionTarea(tarea, equipo);
        return new Timestamp(fecha.getTime() - duracion * 60000);
    }

    public int calcularDuracionTarea(Tarea tarea, Equipo equipo) {
        return (int) (tarea.getTiempo() / equipo.getCapacidad());
    }

    public Collection<Tarea> ordenarTareasT(Producto producto) {
        return producto.getTareas().stream()
                .sorted(Comparator.comparingInt(Tarea::getOrden).reversed())
                .collect(Collectors.toList());
    }
}
