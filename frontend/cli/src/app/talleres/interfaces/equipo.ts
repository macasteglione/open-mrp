import { TipoEquipo } from "../tipos/interfaces/tipoEquipo";

export interface Equipo {
    id: number;
    codigo: string;
    capacidad: number;
    tipoEquipo: TipoEquipo;
}
