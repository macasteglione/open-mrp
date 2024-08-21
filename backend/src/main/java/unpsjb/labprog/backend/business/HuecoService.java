package unpsjb.labprog.backend.business;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import unpsjb.labprog.backend.model.Equipo;
import unpsjb.labprog.backend.model.Hueco;
import unpsjb.labprog.backend.model.Planificacion;
import unpsjb.labprog.backend.model.Taller;
import unpsjb.labprog.backend.model.Tarea;
import unpsjb.labprog.backend.model.TipoEquipo;

@Service
public class HuecoService {
    @Autowired
    private PlanificacionService planificacionService;
    @Autowired
    private PedidoFabricacionService pedidoFabricacionService;

    private final Map<Taller, Map<TipoEquipo, List<Hueco>>> mapaTallerTipoEquipos = new HashMap<>();
    private Map<TipoEquipo, List<Hueco>> backupRollback = new HashMap<>();

    public void mapearTaller(Taller aTaller) {
        mapaTallerTipoEquipos.put(aTaller, new HashMap<>());
    }

    public void mapearTipoEquipo(Taller aTaller, TipoEquipo aTipoEquipo) {
        List<Equipo> equiposTipoEquipo = aTaller.getEquipos().stream()
                .filter(equipo -> equipo.getTipoEquipo().equals(aTipoEquipo))
                .collect(Collectors.toList());

        List<Hueco> huecosTipoEquipo = equiposTipoEquipo.stream()
                .flatMap(equipo -> generarHuecosEquipo(equipo).stream())
                .collect(Collectors.toList());

        mapaTallerTipoEquipos.get(aTaller).put(aTipoEquipo, huecosTipoEquipo);
    }

    public Collection<Hueco> generarHuecosEquipo(Equipo aEquipo) {
        List<Planificacion> planificaciones = new ArrayList<>(planificacionService.findByEquipoId(aEquipo.getId()));
        return generarHuecos(planificaciones, aEquipo);
    }

    public Collection<Hueco> generarHuecos(Collection<Planificacion> somePlanificaciones, Equipo aEquipo) {
        List<Hueco> huecos = new ArrayList<>();

        if (somePlanificaciones == null || somePlanificaciones.isEmpty()) {
            huecos.add(new Hueco(pedidoFabricacionService.primeraFechaPedido(),
                    pedidoFabricacionService.ultimaFechaEntrega(), aEquipo));
            return huecos;
        }

        agregarHuecosIniciales(huecos, somePlanificaciones, aEquipo);
        agregarHuecosIntermedios(huecos, somePlanificaciones, aEquipo);
        agregarHuecosFinales(huecos, somePlanificaciones, aEquipo);

        return huecos;
    }

    private void agregarHuecosIniciales(Collection<Hueco> someHuecos,
            Collection<Planificacion> somePlanificaciones, Equipo aEquipo) {
        Timestamp primeraFechaPedido = pedidoFabricacionService.primeraFechaPedido();
        List<Planificacion> planificaciones = new ArrayList<>(somePlanificaciones);

        if (hayHueco(primeraFechaPedido, planificaciones.get(0).getInicio()))
            someHuecos.add(new Hueco(primeraFechaPedido, planificaciones.get(0).getInicio(), aEquipo));
    }

    private void agregarHuecosIntermedios(
            Collection<Hueco> someHuecos, Collection<Planificacion> somePlanificaciones, Equipo aEquipo) {
        List<Planificacion> planificaciones = new ArrayList<>(somePlanificaciones);

        for (int i = 0; i < planificaciones.size() - 1; i++)
            if (hayHueco(planificaciones.get(i).getFin(), planificaciones.get(i + 1).getInicio()))
                someHuecos.add(
                        new Hueco(planificaciones.get(i).getFin(), planificaciones.get(i + 1).getInicio(), aEquipo));
    }

    private void agregarHuecosFinales(Collection<Hueco> someHuecos, Collection<Planificacion> somePlanificaciones,
            Equipo aEquipo) {
        Timestamp ultimaFechaEntrega = pedidoFabricacionService.ultimaFechaEntrega();
        List<Planificacion> planificaciones = new ArrayList<>(somePlanificaciones);

        if (hayHueco(planificaciones.get(planificaciones.size() - 1).getFin(), ultimaFechaEntrega))
            someHuecos.add(
                    new Hueco(planificaciones.get(planificaciones.size() - 1).getFin(), ultimaFechaEntrega, aEquipo));
    }

    private boolean hayHueco(Timestamp fecha1, Timestamp fecha2) {
        return fecha1.before(fecha2);
    }

    public Planificacion obtenerHueco(Taller aTaller, Tarea aTarea, Timestamp aFecha) {
        mapearTallerSiNoExiste(aTaller);
        mapearTipoEquipoSiNoExiste(aTaller, aTarea.getTipoEquipo());

        List<Hueco> huecos = ordenarHuecos(mapaTallerTipoEquipos.get(aTaller).get(aTarea.getTipoEquipo()));
        Hueco aHueco = huecos.stream().filter(hueco -> esCompatible(hueco, aTarea, aFecha)).findFirst().orElse(null);

        if (aHueco != null)
            return crearPlanificacionSiCompatible(huecos, aHueco, aTarea, aFecha);

        return null;
    }

    private void mapearTallerSiNoExiste(Taller aTaller) {
        mapaTallerTipoEquipos.computeIfAbsent(aTaller, k -> new HashMap<>());
    }

    private void mapearTipoEquipoSiNoExiste(Taller aTaller, TipoEquipo aTipoEquipo) {
        mapaTallerTipoEquipos.get(aTaller).computeIfAbsent(aTipoEquipo, k -> {
            mapearTipoEquipo(aTaller, aTipoEquipo);
            return mapaTallerTipoEquipos.get(aTaller).get(aTipoEquipo);
        });
    }

    private Planificacion crearPlanificacionSiCompatible(Collection<Hueco> someHuecos, Hueco aHueco, Tarea aTarea,
            Timestamp aFecha) {
        Planificacion planificacion;

        if (aHueco.getInicio().before(aFecha))
            planificacion = crearPlanificacion(aTarea, aHueco.getEquipo(), aFecha);
        else if (aHueco.getInicio().after(aFecha) && aHueco.getFin().after(aFecha))
            planificacion = crearPlanificacion(aTarea, aHueco.getEquipo(), aHueco.getInicio());
        else
            return null;

        ajustarHuecos(someHuecos, aHueco, planificacion);
        return planificacion;
    }

    private Planificacion crearPlanificacion(Tarea aTarea, Equipo aEquipo, Timestamp aFecha) {
        Planificacion planificacion = new Planificacion();
        planificacion.setTarea(aTarea);
        planificacion.setEquipo(aEquipo);
        planificacion.setInicio(aFecha);
        planificacion.setFin(calcularFechaFin(aTarea, aEquipo, aFecha));
        return planificacion;
    }

    private void ajustarHuecos(Collection<Hueco> someHuecos, Hueco aHueco, Planificacion aPlanificacion) {
        if (aPlanificacion.getInicio().equals(aHueco.getInicio())) {
            someHuecos.remove(aHueco);
            aHueco.setFin(aPlanificacion.getFin());
            someHuecos.add(aHueco);
        } else if (aPlanificacion.getFin().equals(aHueco.getFin())) {
            someHuecos.remove(aHueco);
            aHueco.setInicio(aPlanificacion.getFin());
            someHuecos.add(aHueco);
        } else {
            someHuecos.remove(aHueco);
            Hueco nuevoHueco = new Hueco(aPlanificacion.getFin(), aHueco.getFin(), aHueco.getEquipo());
            aHueco.setFin(aPlanificacion.getInicio());
            someHuecos.add(aHueco);
            someHuecos.add(nuevoHueco);
        }
    }

    public void actualizarHuecos(Taller aTaller, TipoEquipo aTipoEquipo, Collection<Hueco> someHuecos) {
        mapaTallerTipoEquipos.get(aTaller).put(aTipoEquipo, new ArrayList<>(someHuecos));
    }

    private boolean esCompatible(Hueco aHueco, Tarea aTarea, Timestamp aFecha) {
        long duracionTarea = calcularDuracionTarea(aHueco.getEquipo(), aTarea);

        return aHueco.tiempo() >= duracionTarea && aHueco.getFin().after(aFecha)
                && calcularFechaFin(aTarea, aHueco.getEquipo(), aFecha).before(aHueco.getFin());
    }

    private long calcularDuracionTarea(Equipo aEquipo, Tarea aTarea) {
        return aTarea.getTiempo() / aEquipo.getCapacidad();
    }

    private Timestamp calcularFechaFin(Tarea aTarea, Equipo aEquipo, Timestamp tiempoBase) {
        long duracionMilisegundos = aTarea.getTiempo() / aEquipo.getCapacidad() * 6000;
        return new Timestamp(tiempoBase.getTime() + duracionMilisegundos);
    }

    public void rollback(Taller aTaller) {
        mapaTallerTipoEquipos.remove(aTaller);
        mapearTaller(aTaller);
    }

    public void rollbackBackup(Taller aTaller) {
        mapaTallerTipoEquipos.put(aTaller, backupRollback);
    }

    public void generarBackup(Taller aTaller) {
        mapearTallerSiNoExiste(aTaller);
        backupRollback = new HashMap<>(mapaTallerTipoEquipos.get(aTaller));
    }

    public void romperTodo() {
        mapaTallerTipoEquipos.clear();
    }

    public List<Hueco> ordenarHuecos(List<Hueco> huecos) {
        huecos.sort(Comparator.comparing(Hueco::getInicio));
        return huecos;
    }
}
