import { Planificacion } from "./planificacion";

export interface OrdenTrabajo {
    id: number;
    numero: number;
    planificaciones: Planificacion[];
}
