package unpsjb.labprog.backend.business;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import unpsjb.labprog.backend.model.Equipo;
import unpsjb.labprog.backend.model.Hueco;
import unpsjb.labprog.backend.model.Planificacion;
import unpsjb.labprog.backend.model.Taller;
import unpsjb.labprog.backend.model.Tarea;

@Service
public class PlanificacionHuecoService {
    @Autowired
    private PedidoFabricacionService pedidoFabricacionService;
    @Autowired
    private PlanificacionRepository repository;

    private Map<Equipo, List<Hueco>> mapa = new HashMap<>();
    private Map<Equipo, List<Hueco>> backup = new HashMap<>();

    private Collection<Hueco> obtenerHuecos(Equipo aEquipo) {
        return mapa.computeIfAbsent(aEquipo, this::calcularHuecosParaEquipo);
    }

    private List<Hueco> calcularHuecosParaEquipo(Equipo aEquipo) {
        List<Hueco> huecos = new ArrayList<>();
        List<Planificacion> planificaciones = new ArrayList<>(repository.encontrarDisponibilidad(aEquipo));

        if (planificaciones.isEmpty()) {
            huecos.add(crearHueco(aEquipo, pedidoFabricacionService.primeraFechaPedido(),
                    pedidoFabricacionService.ultimaFechaEntrega()));
            return huecos;
        }

        Timestamp prevFin = pedidoFabricacionService.primeraFechaPedido();

        for (Planificacion planificacion : planificaciones) {
            if (prevFin.before(planificacion.getInicio()))
                huecos.add(crearHueco(aEquipo, prevFin, planificacion.getInicio()));

            prevFin = planificacion.getFin();
        }

        huecos.add(crearHueco(aEquipo, prevFin, pedidoFabricacionService.ultimaFechaEntrega()));
        return huecos;
    }

    private Hueco crearHueco(Equipo aEquipo, Timestamp inicio, Timestamp fin) {
        Hueco hueco = new Hueco();
        hueco.setEquipo(aEquipo);
        hueco.setInicio(inicio);
        hueco.setFin(fin);
        return hueco;
    }

    public void actualizarHuecos(Timestamp inicio, Timestamp fin, Hueco aHueco) {
        List<Hueco> listaHuecos = mapa.getOrDefault(aHueco.getEquipo(), new ArrayList<>());
        listaHuecos.remove(aHueco);

        Hueco hueco1 = crearHueco(aHueco.getEquipo(), aHueco.getInicio(), inicio);
        Hueco hueco2 = crearHueco(aHueco.getEquipo(), fin, aHueco.getFin());

        if (isValidHueco(hueco1))
            listaHuecos.add(hueco1);
        if (isValidHueco(hueco2))
            listaHuecos.add(hueco2);

        mapa.put(aHueco.getEquipo(), listaHuecos);
    }

    private boolean isValidHueco(Hueco aHueco) {
        return aHueco.getInicio().before(aHueco.getFin());
    }

    public void rollback(Collection<Planificacion> somePlanificaciones) {
        for (Planificacion planificacion : somePlanificaciones) {
            List<Hueco> huecos = mapa.get(planificacion.getEquipo());

            if (huecos != null)
                huecos.add(reemplazar(planificacion));
        }
    }

    private Hueco reemplazar(Planificacion aPlanificacion) {
        return crearHueco(aPlanificacion.getEquipo(), aPlanificacion.getInicio(), aPlanificacion.getFin());
    }

    public Collection<Hueco> huecosTallerTipo(Tarea aTarea, Taller aTaller) {
        List<Hueco> result = new ArrayList<>();

        aTaller.getEquipos().stream()
                .filter(equipo -> aTarea.getTipoEquipo().equals(equipo.getTipoEquipo()))
                .forEach(equipo -> result.addAll(obtenerHuecos(equipo)));

        return result;
    }

    public void iniciar() {
        this.mapa = new HashMap<>();
    }

    public void rollback() {
        this.mapa = this.backup;
    }

    public void generarBackup() {
        this.backup = new HashMap<>(this.mapa);
    }
}
