package unpsjb.labprog.backend.business;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import unpsjb.labprog.backend.model.Equipo;
import unpsjb.labprog.backend.model.Hueco;
import unpsjb.labprog.backend.model.Planificacion;
import unpsjb.labprog.backend.model.Taller;
import unpsjb.labprog.backend.model.TipoEquipo;

@Service
public class PlanificacionService {
    @Autowired
    PlanificacionRepository repository;
    @Autowired
    PedidoFabricacionService pedidoFabricacionService;

    public Collection<Planificacion> findByEquipoId(int equipoId) {
        return repository.findByEquipoId(equipoId);
    }

    public Collection<Planificacion> findByEquipoIdAndFechas(int equipoId, Date desde, Date hasta) {
        return repository.findByEquipoIdAndFechas(equipoId, desde, hasta);
    }

    public Collection<Planificacion> findAll() {
        List<Planificacion> result = new ArrayList<>();
        repository.findAll().forEach(e -> result.add(e));
        return result;
    }

    @Transactional
    public Planificacion create(Planificacion aPlanificacion) {
        return repository.save(aPlanificacion);
    }

    @Transactional
    public Planificacion save(Planificacion e) {
        return repository.save(e);
    }

    public void saveAll(Collection<Planificacion> somePlanificaciones) {
        repository.saveAll(somePlanificaciones);
    }

    @Transactional
    public void delete(int id) {
        repository.deleteById(id);
    }

    public Collection<Planificacion> encontrarDisponibilidad(Equipo aEquipo) {
        return repository.encontrarDisponibilidad(aEquipo);
    }

    public Collection<Hueco> obtenerHuecos(Equipo aEquipo) {
        List<Hueco> huecos = new ArrayList<>();
        List<Planificacion> planificaciones = new ArrayList<>(encontrarDisponibilidad(aEquipo));
        Timestamp prevFin = null;

        for (Planificacion planificacion : planificaciones) {
            if (prevFin == null || prevFin.before(planificacion.getInicio())) {
                Timestamp inicioHueco = (prevFin == null) ? pedidoFabricacionService.primeraFechaPedido() : prevFin;
                Timestamp finHueco = planificacion.getInicio();
                huecos.add(new Hueco(inicioHueco, finHueco, null));
            }
            prevFin = planificacion.getFin();
        }

        if (prevFin != null) {
            Timestamp inicioHueco = prevFin;
            Timestamp finHueco = pedidoFabricacionService.ultimaFechaEntrega();
            huecos.add(new Hueco(inicioHueco, finHueco, null));
        }

        return huecos;
    }

    public void agotarTaller(Taller aTaller) {
        for (Equipo equipo : aTaller.getEquipos()) {
            Planificacion planificacion = new Planificacion();
            planificacion.setTarea(null);
            planificacion.setEquipo(equipo);
            planificacion.setInicio(pedidoFabricacionService.primeraFechaPedido());
            planificacion.setFin(pedidoFabricacionService.ultimaFechaEntrega());
            repository.save(planificacion);
        }
    }

    public Collection<Planificacion> encontrarPlanificacionesTipoEquipo(Taller aTaller, TipoEquipo aTipoEquipo) {
        return repository.encontrarDisponibilidadTipoEquipo(aTaller, aTipoEquipo);
    }
}
