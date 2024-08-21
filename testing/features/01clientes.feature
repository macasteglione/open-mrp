# language: es
Característica: gestión de clientes

  Esquema del escenario: Nuevo cliente que encargan pedidos de fabricación de productos
    Dado que se ingresa el cliente con "<razonSocial>" y <cuit>
    Cuando presiono el botón de guardar clientes
    Entonces se espera el siguiente <status> con la "<respuesta>" clientes

    Ejemplos:
      | razonSocial                | cuit       | status | respuesta                                                                |
      | prilidiano pueyrredon      | 1000000001 |    200 | Cliente prilidiano pueyrredon (1000000001) registrado correctamente      |
      | marcelo t. de alvear       | 2000000002 |    200 | Cliente marcelo t. de alvear (2000000002) registrado correctamente       |
      | domingo faustino sarmiento | 3000000003 |    200 | Cliente domingo faustino sarmiento (3000000003) registrado correctamente |
      | walter runciman            | 4000000004 |    200 | Cliente walter runciman (4000000004) registrado correctamente            |
      | julio argentino roca       | 5000000005 |    200 | Cliente julio argentino roca (5000000005) registrado correctamente       |
      | test                       | 6000000006 |    200 | Cliente test (6000000006) registrado correctamente                       |
      | demian                     | 7000000007 |    200 | Cliente demian (7000000007) registrado correctamente                     |
