import { Routes } from "@angular/router";
import { HomeComponent } from "./home/home.component";
import { ClientesDetailComponent } from "./clientes/components/clientes-detail.component";
import { ClientesComponent } from "./clientes/components/clientes.component";
import { TipoEquiposComponent } from "./talleres/tipos/components/tipoEquipos.component";
import { TipoEquiposDetailComponent } from "./talleres/tipos/components/tipoEquipos-detail.component";
import { TalleresComponent } from "./talleres/components/talleres.component";
import { TalleresDetailComponent } from "./talleres/components/talleres-detail.component";
import { ProductosComponent } from "./productos/components/productos.component";
import { ProductosDetailComponent } from "./productos/components/productos-detail.component";
import { PedidosComponent } from "./pedidos/components/pedidos.component";
import { PedidosDetailComponent } from "./pedidos/components/pedidos-detail.component";
import { GanttComponent } from "./gantt/components/gantt.component";

let pageTitle: string = "MRP Lite -";

export const routes: Routes = [
    { path: "", component: HomeComponent, title: `${pageTitle} Home` },
    {
        path: "clientes",
        component: ClientesComponent,
        title: `${pageTitle} Clientes`,
    },
    { path: "clientes/:id", component: ClientesDetailComponent },
    {
        path: "talleres/equipos/tipos",
        component: TipoEquiposComponent,
        title: `${pageTitle} Tipo de Equipos`,
    },
    {
        path: "talleres/equipos/tipos/:id",
        component: TipoEquiposDetailComponent,
    },
    {
        path: "talleres",
        component: TalleresComponent,
        title: `${pageTitle} Talleres`,
    },
    { path: "talleres/:id", component: TalleresDetailComponent },
    {
        path: "productos",
        component: ProductosComponent,
        title: `${pageTitle} Productos`,
    },
    { path: "productos/:id", component: ProductosDetailComponent },
    {
        path: "pedidos",
        component: PedidosComponent,
        title: `${pageTitle} Pedidos`,
    },
    { path: "pedidos/:id", component: PedidosDetailComponent },
    {
        path: "visualizar",
        component: GanttComponent,
        title: `${pageTitle} Visualizar`,
    },
];
