package unpsjb.labprog.backend.business;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import unpsjb.labprog.backend.model.GanttObject;
import unpsjb.labprog.backend.model.GanttPlanificacion;
import unpsjb.labprog.backend.model.Taller;

@Service
public class GanttService {
    @Autowired
    private GanttRepository ganttRepository;

    private Collection<GanttPlanificacion> getPlanifications(Taller aTaller, Timestamp desde, Timestamp hasta) {
        if (!ganttRepository.getPlanificacionesTaller().contains(aTaller))
            return new ArrayList<>();

        List<GanttPlanificacion> planificaciones = new ArrayList<>();
        aTaller.getEquipos().forEach(equipo -> planificaciones
                .addAll(ganttRepository.getGanttPlanificaciones(aTaller, equipo, desde, hasta)));

        return planificaciones;
    }

    public GanttObject getGanttObject(Taller taller, Timestamp desde, Timestamp hasta) {
        if (taller == null)
            throw new IllegalArgumentException("Taller no encontrado");

        GanttObject ganttObject = new GanttObject();
        ganttObject.setPlanificaciones(getPlanifications(taller, desde, hasta));

        Timestamp minStart = ganttRepository.getInicioMinimo(taller, desde, hasta);
        Timestamp maxFinish = ganttRepository.getFinMaximo(taller, desde, hasta);

        if (minStart != null && maxFinish != null)
            ganttObject.setCantDias(convertirDeMilisegundosADias(maxFinish.getTime() - minStart.getTime()));
        else
            ganttObject.setCantDias(0L);

        return ganttObject;
    }

    private Long convertirDeMilisegundosADias(Long milis) {
        return milis / (1000 * 3600 * 24);
    }
}
