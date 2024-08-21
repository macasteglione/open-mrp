import { Equipo } from "../../talleres/interfaces/equipo";
import { Tarea } from "../../productos/interfaces/tarea";
import { OrdenTrabajo } from "./ordenTrabajo";

export interface Planificacion {
    id: number;
    inicio: Date;
    fin: Date;
    equipo: Equipo;
    tarea: Tarea;
    ordenTrabajo: OrdenTrabajo;
}