import { PedidoFabricacion } from "../../pedidos/interfaces/pedidoFabricacion";
import { Equipo } from "../../talleres/interfaces/equipo";

export interface GanttPlanification {
    equipo: Equipo;
    pedido: PedidoFabricacion;
    inicio: Date;
    fin: Date;
}
