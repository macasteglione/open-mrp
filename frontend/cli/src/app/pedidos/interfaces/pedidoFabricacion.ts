import { Cliente } from "../../clientes/interfaces/cliente";
import { Producto } from "../../productos/interfaces/producto";
import { Estado } from "./estado";
import { Planificacion } from "./planificacion";

export interface PedidoFabricacion {
    id: number;
    fechaPedido: Date;
    fechaEntrega: Date;
    cantidad: number;
    producto: Producto;
    cliente: Cliente;
    estado: Estado;
    planificaciones: Planificacion[];
}