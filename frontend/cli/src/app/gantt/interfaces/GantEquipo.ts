import { Equipo } from "../../talleres/interfaces/equipo";
import { GanttPlanification } from "./GantPlanificacion";

export interface GanttEquipo {
    equipo: Equipo;
    planificaciones: GanttPlanification[];
}
