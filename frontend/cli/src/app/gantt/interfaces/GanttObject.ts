import { GanttPlanification } from "./GantPlanificacion";

export interface GanttObject {
    cantDias: number;
    planificaciones: GanttPlanification[];
}
