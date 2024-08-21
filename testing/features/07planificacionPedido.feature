# language: es
Característica: administra planificación de pedidos de fabricación
   Planifica, replanifica pedidos planificados y nuevos generados

  Esquema del escenario: Planificación sobre un taller seleccionado de un pedido PE
    Dado el pedido con <cuit> "<fechaPedido>" "<fechaEntrega>" "<producto>" <cantidad>
    Y el "<taller>" PlanificacionPedido
    Y tomando como base de planificación la fecha "<fechaEntrega>" PlanificacionPedido
    Cuando se solicita planificar con esquema de pronta entregar PlanificacionPedido
    Entonces se obtiene la <planificación> con <status> y "<respuesta>" PlanificacionPedido

    Ejemplos:
      | cuit       | fechaPedido | fechaEntrega | cantidad | producto        | taller | planificación | status | respuesta                                                   |
      | 1000000001 |  2023-05-04 |   2023-05-29 |       10 | mesa 1.80x0.70  | este   |            21 |    200 | Producto planificado pronta entrega en taller correctamente |
      | 2000000002 |  2023-05-04 |   2023-05-30 |       70 | mesa 2x1        | este   |            22 |    200 | Producto planificado pronta entrega en taller correctamente |
      | 3000000003 |  2023-05-04 |   2023-05-28 |      295 | silla estandar  | este   |            23 |    200 | Producto planificado pronta entrega en taller correctamente |
      | 4000000004 |  2023-05-04 |   2023-05-31 |      230 | banqueta        | este   |            24 |    200 | Producto planificado pronta entrega en taller correctamente |
      | 5000000005 |  2023-05-04 |   2023-05-30 |      400 | zocalos de 2.5m | centro |            25 |    200 | Producto planificado pronta entrega en taller correctamente |
      | 1000000001 |  2023-05-20 |   2023-06-06 |       40 | zocalos de 2m   | centro |            26 |    200 | Producto planificado pronta entrega en taller correctamente |
      | 2000000002 |  2023-05-20 |   2023-06-06 |      123 | zocalos de 1.5m | centro |            27 |    200 | Producto planificado pronta entrega en taller correctamente |
      | 3000000003 |  2023-05-20 |   2023-06-06 |      350 | puerta estandar | este   |            28 |    200 | Producto planificado pronta entrega en taller correctamente |
      | 4000000004 |  2023-05-20 |   2023-06-06 |      200 | puerta doble    | este   |            29 |    200 | Producto planificado pronta entrega en taller correctamente |
      | 5000000005 |  2023-05-20 |   2023-06-06 |      450 | perchero de pie | oeste  |            30 |    200 | Producto planificado pronta entrega en taller correctamente |
      | 5000000005 |  2023-05-20 |   2023-06-06 |        1 | cama            | centro |            31 |    200 | Producto planificado pronta entrega en taller correctamente |

  Esquema del escenario: Planificación sobre un taller seleccionado de un pedido EDF
    Dado el pedido con <cuit> "<fechaPedido>" "<fechaEntrega>" "<producto>" <cantidad>
    Y el "<taller>" PlanificacionPedido
    Y tomando como base de planificación la fecha "<fechaEntrega>" PlanificacionPedido
    Cuando se solicita planificar con entrega tardía EDF PlanificacionPedido
    Entonces se obtiene la <planificación> con <status> y "<respuesta>" PlanificacionPedido

    Ejemplos:
      | cuit       | fechaPedido | fechaEntrega | cantidad | producto        | taller | planificación | status | respuesta                                                   |
      | 1000000001 |  2023-05-04 |   2023-05-29 |       10 | mesa 1.80x0.70  | este   |            31 |    200 | Producto planificado pronta entrega en taller correctamente |
      | 2000000002 |  2023-05-04 |   2023-05-30 |       70 | mesa 2x1        | este   |            32 |    200 | Producto planificado pronta entrega en taller correctamente |
      | 3000000003 |  2023-05-04 |   2023-05-28 |      295 | silla estandar  | este   |            33 |    200 | Producto planificado pronta entrega en taller correctamente |
      | 4000000004 |  2023-05-04 |   2023-05-31 |      230 | banqueta        | este   |            34 |    200 | Producto planificado pronta entrega en taller correctamente |
      | 5000000005 |  2023-05-04 |   2023-05-30 |      400 | zocalos de 2.5m | centro |            35 |    200 | Producto planificado pronta entrega en taller correctamente |
      | 1000000001 |  2023-05-20 |   2023-06-06 |       40 | zocalos de 2m   | centro |            36 |    200 | Producto planificado pronta entrega en taller correctamente |
      | 2000000002 |  2023-05-20 |   2023-06-06 |      123 | zocalos de 1.5m | centro |            37 |    200 | Producto planificado pronta entrega en taller correctamente |
      | 3000000003 |  2023-05-20 |   2023-06-06 |      350 | puerta estandar | este   |            38 |    200 | Producto planificado pronta entrega en taller correctamente |
      | 4000000004 |  2023-05-20 |   2023-06-06 |      200 | puerta doble    | este   |            39 |    200 | Producto planificado pronta entrega en taller correctamente |
      | 5000000005 |  2023-05-20 |   2023-06-06 |      450 | perchero de pie | oeste  |            40 |    200 | Producto planificado pronta entrega en taller correctamente |
      | 5000000005 |  2023-05-20 |   2023-06-06 |        1 | cama            | centro |            41 |    200 | Producto planificado pronta entrega en taller correctamente |
