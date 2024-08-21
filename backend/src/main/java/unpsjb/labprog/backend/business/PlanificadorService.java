package unpsjb.labprog.backend.business;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import unpsjb.labprog.backend.model.Equipo;
import unpsjb.labprog.backend.model.Estado;
import unpsjb.labprog.backend.model.Hueco;
import unpsjb.labprog.backend.model.PedidoFabricacion;
import unpsjb.labprog.backend.model.Planificacion;
import unpsjb.labprog.backend.model.Producto;
import unpsjb.labprog.backend.model.Registro;
import unpsjb.labprog.backend.model.Taller;
import unpsjb.labprog.backend.model.Tarea;
import unpsjb.labprog.backend.model.TipoEquipo;

@Service
public class PlanificadorService {
    @Autowired
    private ProductoService productoService;
    @Autowired
    private TallerService tallerService;
    @Autowired
    private PedidoFabricacionService pedidoFabricacionService;
    @Autowired
    private PlanificacionService planificacionService;
    @Autowired
    private HuecoService huecoService;
    @Autowired
    private TallerPertinenteService tallerPertinenteService;
    @Autowired
    private RegistroService registroService;

    private Timestamp primerLugar(Equipo aEquipo, Tarea aTarea, Timestamp finAnterior, Taller aTaller) {
        huecoService.obtenerHueco(aTaller, aTarea, finAnterior);
        List<Hueco> huecos = new ArrayList<>(planificacionService.obtenerHuecos(aEquipo));

        for (Hueco hueco : huecos)
            if (hueco.getInicio().getTime() >= finAnterior.getTime() &&
                    calcularFechaFin(aTarea, aEquipo, hueco.getInicio()).getTime() <= hueco.getFin().getTime())
                return hueco.getInicio();

        return finAnterior;
    }

    public Taller recuperarTallerPertinente(String nombreProducto) {
        Producto producto = productoService.findByName(nombreProducto);
        if (producto == null)
            throw new ProductoNotFoundException();

        List<Taller> talleres = new ArrayList<>(tallerService.findAllOrderByCode());
        Set<TipoEquipo> productoTipoEquipos = producto.getTareas().stream()
                .map(Tarea::getTipoEquipo)
                .collect(Collectors.toSet());

        return talleres.stream()
                .filter(taller -> taller.getEquipos().stream()
                        .map(Equipo::getTipoEquipo)
                        .collect(Collectors.toSet())
                        .containsAll(productoTipoEquipos))
                .findFirst()
                .orElse(null);
    }

    public Collection<Taller> recuperarTalleresPertinentes(String nombreProducto) {
        Producto producto = productoService.findByName(nombreProducto);
        if (producto == null)
            throw new ProductoNotFoundException();

        List<Taller> talleres = new ArrayList<>(tallerService.findAllOrderByCode());
        Set<TipoEquipo> productoTipoEquipos = producto.getTareas().stream()
                .map(Tarea::getTipoEquipo)
                .collect(Collectors.toSet());

        return talleres.stream()
                .filter(taller -> taller.getEquipos().stream()
                        .map(Equipo::getTipoEquipo)
                        .collect(Collectors.toSet())
                        .containsAll(productoTipoEquipos))
                .collect(Collectors.toList());
    }

    public long calcularDuracionTarea(Equipo aEquipo, Tarea aTarea) {
        return aTarea.getTiempo() / aEquipo.getCapacidad() * 6000;
    }

    private Timestamp calcularFechaFin(Tarea aTarea, Equipo aEquipo, Timestamp tiempoBase) {
        long duracionMilisegundos = aTarea.getTiempo() / aEquipo.getCapacidad() * 6000;
        return new Timestamp(tiempoBase.getTime() + duracionMilisegundos);
    }

    public void planificarTodo() {
        pedidoFabricacionService.findAllOrderByFechaPedido().forEach(this::planificarPedido);
    }

    public void rollbackHardcodeado(PedidoFabricacion pedido) {
        pedido.getPlanificaciones().forEach(planificacion -> planificacionService.delete(planificacion.getId()));
    }

    public PedidoFabricacion planificarPedido(PedidoFabricacion pedido) {
        if (pedido == null)
            throw new PedidoNotFoundException();

        if (pedido.getEstado() == Estado.PLANIFICADO || pedido.getEstado() == Estado.FINALIZADO) {
            registroService.save(
                    new Registro("El pedido con id " + pedido.getId() + " no se pudo planificar debido a su estado"));
            return pedido;
        }

        boolean result = false;

        for (Taller taller : tallerPertinenteService.talleresPorEficiencia(pedido.getProducto())) {
            huecoService.generarBackup(taller);
            try {
                result = planificarProducto2(pedido, taller);
                if (result) {
                    registroService.save(new Registro("Se planificó el pedido con id " + pedido.getId()
                            + " en el taller: " + taller.getNombre() + " con esquema de pronta entrega"));
                    break;
                }
            } catch (NoPlanificableException ex) {
                huecoService.rollbackBackup(taller);
                pedido.getPlanificaciones().clear();
                huecoService.rollback(taller);
                result = false;
            }
        }

        if (result) {
            pedido.setEstado(Estado.PLANIFICADO);
            planificacionService.saveAll(pedido.getPlanificaciones());
            pedidoFabricacionService.save(pedido);
        } else {
            pedido.setEstado(Estado.NO_PLANIFICABLE);
            registroService.save(new Registro(
                    "No pudo planificarse el pedido con id " + pedido.getId() + " con esquema de pronta entrega"));
        }

        return pedido;
    }

    @Transactional(rollbackOn = NoPlanificableException.class)
    public boolean planificarProducto(PedidoFabricacion aPedido, Taller aTaller) {
        List<Tarea> tareas = new ArrayList<>(ordenarTareas(aPedido.getProducto()));
        Timestamp fechaTrabajo;

        for (int i = 0; i < aPedido.getCantidad(); i++) {
            fechaTrabajo = aPedido.getFechaPedido();

            for (Tarea tarea : tareas) {
                List<Equipo> equipos = new ArrayList<>(buscarEquipo(tarea, aTaller));
                fechaTrabajo = planificarTarea(tarea, equipos.get(0), aTaller, fechaTrabajo, aPedido);

                if (fechaTrabajo.getTime() > aPedido.getFechaEntrega().getTime())
                    throw new NoPlanificableException();
            }
        }
        return true;
    }

    public Timestamp planificarTarea(Tarea aTarea, Equipo aEquipo, Taller aTaller, Timestamp fechaTrabajo,
            PedidoFabricacion aPedido) {
        Timestamp iniciaPlanificacion = primerLugar(aEquipo, aTarea, fechaTrabajo, aTaller);
        Timestamp terminaPlanificacion = calcularFechaFin(aTarea, aEquipo, iniciaPlanificacion);

        Planificacion planificacion = new Planificacion();
        planificacion.setInicio(iniciaPlanificacion);
        planificacion.setEquipo(aEquipo);
        planificacion.setTarea(aTarea);
        planificacion.setFin(terminaPlanificacion);

        aPedido.getPlanificaciones().add(planificacion);
        planificacionService.save(planificacion);

        return terminaPlanificacion;
    }

    public Collection<Equipo> buscarEquipo(Tarea aTarea, Taller aTaller) {
        return aTaller.getEquipos().stream()
                .filter(equipo -> equipo.getTipoEquipo().getNombre().equals(aTarea.getTipoEquipo().getNombre()))
                .collect(Collectors.toList());
    }

    public Collection<Tarea> ordenarTareas(Producto aProducto) {
        return aProducto.getTareas().stream()
                .sorted(Comparator.comparingInt(Tarea::getOrden))
                .collect(Collectors.toList());
    }

    public void agotarTaller(int id) {
        Taller taller = tallerService.findById(id);
        planificacionService.agotarTaller(taller);
    }

    public boolean planificarProducto2(PedidoFabricacion aPedido, Taller aTaller) {
        List<Tarea> tareas = new ArrayList<>(ordenarTareas(aPedido.getProducto()));
        Timestamp fechaTrabajo;

        for (int i = 0; i < aPedido.getCantidad(); i++) {
            fechaTrabajo = aPedido.getFechaPedido();

            for (Tarea tarea : tareas) {
                fechaTrabajo = planificarTarea2(aPedido, aTaller, tarea, fechaTrabajo);

                if (fechaTrabajo.getTime() > aPedido.getFechaEntrega().getTime())
                    throw new NoPlanificableException();
            }
        }
        return true;
    }

    public Timestamp planificarTarea2(PedidoFabricacion aPedido, Taller aTaller, Tarea aTarea, Timestamp fechaTrabajo) {
        Planificacion planificacion = generarPlanificacion(aTarea, aTaller, fechaTrabajo);

        if (planificacion == null)
            throw new NoPlanificableException();

        aPedido.getPlanificaciones().add(planificacion);
        return planificacion.getFin();
    }

    private Planificacion generarPlanificacion(Tarea aTarea, Taller aTaller, Timestamp fechaTrabajo) {
        return huecoService.obtenerHueco(aTaller, aTarea, fechaTrabajo);
    }
}
