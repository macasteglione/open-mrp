# language: es
Característica: gestión de productos

  Esquema del escenario: Nuevo producto sin tareas
    Dado que se ingresa el nuevo producto con "<nombre>"
    Cuando presiono el botón de guardar productos
    Entonces se espera el siguiente <status> con la "<respuesta>" productos

    Ejemplos:
      | nombre          | status | respuesta                                        |
      | mesa 1.80x0.70  |    200 | Producto mesa 1.80x0.70 ingresado correctamente  |
      | mesa 2x1        |    200 | Producto mesa 2x1 ingresado correctamente        |
      | silla estandar  |    200 | Producto silla estandar ingresado correctamente  |
      | banqueta        |    200 | Producto banqueta ingresado correctamente        |
      | zocalos de 1.5m |    200 | Producto zocalos de 1.5m ingresado correctamente |
      | zocalos de 2m   |    200 | Producto zocalos de 2m ingresado correctamente   |
      | zocalos de 2.5m |    200 | Producto zocalos de 2.5m ingresado correctamente |
      | puerta estandar |    200 | Producto puerta estandar ingresado correctamente |
      | puerta doble    |    200 | Producto puerta doble ingresado correctamente    |
      | perchero de pie |    200 | Producto perchero de pie ingresado correctamente |
      | cama            |    200 | Producto cama ingresado correctamente            |
      | teseracto       |    200 | Producto teseracto ingresado correctamente       |
      | teclado         |    200 | Producto teclado ingresado correctamente         |
      | caja            |    200 | Producto caja ingresado correctamente            |

  Esquema del escenario: Agregar tareas a los talleres existente
    Dado que existen los talleres cuando se agrega la tarea con "<nombreTarea>", <orden>, <tiempo> y "<tipoEquipo>" para el producto "<nombreProducto>"
    Cuando presiono el botón de guardar tareas
    Entonces se espera el siguiente <status> con la "<respuesta>" productos

    Ejemplos:
      | nombreProducto  | nombreTarea         | orden | tiempo | tipoEquipo        | status | respuesta                                          |
      | mesa 1.80x0.70  | cortar patas        |     1 |     10 | cierra            |    200 | Producto mesa 1.80x0.70 actualizado correctamente  |
      | mesa 1.80x0.70  | cortar tapa         |     2 |     15 | cierra            |    200 | Producto mesa 1.80x0.70 actualizado correctamente  |
      | mesa 1.80x0.70  | pulir patas         |     3 |      7 | lijadora de banda |    200 | Producto mesa 1.80x0.70 actualizado correctamente  |
      | mesa 1.80x0.70  | pulir tapa          |     4 |     25 | lijadora de banda |    200 | Producto mesa 1.80x0.70 actualizado correctamente  |
      | mesa 1.80x0.70  | ensamblar y encolar |     5 |     45 | mesa ensamblado   |    200 | Producto mesa 1.80x0.70 actualizado correctamente  |
      | mesa 1.80x0.70  | pintar y finalizar  |     6 |     35 | mesa ensamblado   |    200 | Producto mesa 1.80x0.70 actualizado correctamente  |
      | mesa 2x1        | cortar patas        |     1 |     12 | cierra            |    200 | Producto mesa 2x1 actualizado correctamente        |
      | mesa 2x1        | cortar tapa         |     2 |     19 | cierra            |    200 | Producto mesa 2x1 actualizado correctamente        |
      | mesa 2x1        | pulir patas         |     3 |     10 | lijadora de banda |    200 | Producto mesa 2x1 actualizado correctamente        |
      | mesa 2x1        | pulir tapa          |     4 |     29 | lijadora de banda |    200 | Producto mesa 2x1 actualizado correctamente        |
      | mesa 2x1        | ensamblar y encolar |     5 |     52 | mesa ensamblado   |    200 | Producto mesa 2x1 actualizado correctamente        |
      | mesa 2x1        | pintar y finalizar  |     6 |     41 | mesa ensamblado   |    200 | Producto mesa 2x1 actualizado correctamente        |
      | silla estandar  | cortar patas        |     1 |      8 | cierra            |    200 | Producto silla estandar actualizado correctamente  |
      | silla estandar  | cortar respaldo     |     2 |      7 | cierra            |    200 | Producto silla estandar actualizado correctamente  |
      | silla estandar  | cortar asiento      |     3 |     10 | cierra            |    200 | Producto silla estandar actualizado correctamente  |
      | silla estandar  | pulir patas         |     4 |      5 | lijadora de banda |    200 | Producto silla estandar actualizado correctamente  |
      | silla estandar  | pulir respaldo      |     5 |      4 | lijadora de banda |    200 | Producto silla estandar actualizado correctamente  |
      | silla estandar  | pulir asiento       |     6 |     20 | lijadora de banda |    200 | Producto silla estandar actualizado correctamente  |
      | silla estandar  | ensamblar y encolar |     7 |     32 | mesa ensamblado   |    200 | Producto silla estandar actualizado correctamente  |
      | silla estandar  | pintar y finalizar  |     8 |     23 | mesa ensamblado   |    200 | Producto silla estandar actualizado correctamente  |
      | banqueta        | cortar patas        |     1 |      8 | cierra            |    200 | Producto banqueta actualizado correctamente        |
      | banqueta        | cortar asiento      |     3 |     10 | cierra            |    200 | Producto banqueta actualizado correctamente        |
      | banqueta        | pulir patas         |     4 |      5 | lijadora de banda |    200 | Producto banqueta actualizado correctamente        |
      | banqueta        | pulir asiento       |     6 |     20 | lijadora de banda |    200 | Producto banqueta actualizado correctamente        |
      | banqueta        | ensamblar y encolar |     7 |     32 | mesa ensamblado   |    200 | Producto banqueta actualizado correctamente        |
      | banqueta        | pintar y finalizar  |     8 |     23 | mesa ensamblado   |    200 | Producto banqueta actualizado correctamente        |
      | zocalos de 1.5m | cortar              |     1 |      5 | cierra            |    200 | Producto zocalos de 1.5m actualizado correctamente |
      | zocalos de 1.5m | fresar              |     2 |      6 | fresadora         |    200 | Producto zocalos de 1.5m actualizado correctamente |
      | zocalos de 1.5m | cepillar            |     3 |      8 | Cepillo Garlopa   |    200 | Producto zocalos de 1.5m actualizado correctamente |
      | zocalos de 2m   | cortar              |     1 |      5 | cierra            |    200 | Producto zocalos de 2m actualizado correctamente   |
      | zocalos de 2m   | fresar              |     2 |      7 | fresadora         |    200 | Producto zocalos de 2m actualizado correctamente   |
      | zocalos de 2m   | cepillar            |     3 |      9 | Cepillo Garlopa   |    200 | Producto zocalos de 2m actualizado correctamente   |
      | zocalos de 2.5m | cortar              |     1 |      6 | cierra            |    200 | Producto zocalos de 2.5m actualizado correctamente |
      | zocalos de 2.5m | fresar              |     2 |      8 | fresadora         |    200 | Producto zocalos de 2.5m actualizado correctamente |
      | zocalos de 2.5m | cepillar            |     3 |     10 | Cepillo Garlopa   |    200 | Producto zocalos de 2.5m actualizado correctamente |
      | puerta estandar | cortar              |     1 |     25 | cierra            |    200 | Producto puerta estandar actualizado correctamente |
      | puerta estandar | pulir               |     2 |     28 | lijadora de banda |    200 | Producto puerta estandar actualizado correctamente |
      | puerta estandar | cepillar            |     3 |     12 | Cepillo Garlopa   |    200 | Producto puerta estandar actualizado correctamente |
      | puerta estandar | ensamblar y encolar |     4 |     31 | mesa ensamblado   |    200 | Producto puerta estandar actualizado correctamente |
      | puerta estandar | fresar              |     5 |     12 | fresadora         |    200 | Producto puerta estandar actualizado correctamente |
      | puerta doble    | cortar              |     1 |     50 | cierra            |    200 | Producto puerta doble actualizado correctamente    |
      | puerta doble    | pulir               |     2 |     56 | lijadora de banda |    200 | Producto puerta doble actualizado correctamente    |
      | puerta doble    | cepillar            |     3 |     24 | Cepillo Garlopa   |    200 | Producto puerta doble actualizado correctamente    |
      | puerta doble    | ensamblar y encolar |     4 |     62 | mesa ensamblado   |    200 | Producto puerta doble actualizado correctamente    |
      | puerta doble    | fresar              |     5 |     24 | fresadora         |    200 | Producto puerta doble actualizado correctamente    |
      | perchero de pie | cortar              |     1 |     50 | cierra            |    200 | Producto perchero de pie actualizado correctamente |
      | perchero de pie | doblar              |     2 |     24 | dobladora         |    200 | Producto perchero de pie actualizado correctamente |
      | perchero de pie | ensamblar y encolar |     3 |     31 | mesa ensamblado   |    200 | Producto perchero de pie actualizado correctamente |
      | perchero de pie | Lustrar             |     4 |     24 | mesa ensamblado   |    200 | Producto perchero de pie actualizado correctamente |
      | cama            | cortar patas        |     1 |     11 | cierra            |    200 | Producto cama actualizado correctamente            |
      | cama            | lijar patas         |     2 |     11 | lijadora de banda |    200 | Producto cama actualizado correctamente            |
      | cama            | cortar madera       |     3 |     11 | cierra            |    200 | Producto cama actualizado correctamente            |
      | teseracto       | moldear             |     1 |      3 | martillo          |    200 | Producto teseracto actualizado correctamente       |
      | teseracto       | quemar              |     2 |      6 | soplete           |    200 | Producto teseracto actualizado correctamente       |
      | teseracto       | atornillar          |     3 |      2 | destornillador    |    200 | Producto teseracto actualizado correctamente       |
      | teclado         | moldear             |     1 |      8 | martillo          |    200 | Producto teclado actualizado correctamente         |
      | teclado         | quemar              |     2 |      3 | soplete           |    200 | Producto teclado actualizado correctamente         |
      | teclado         | atornillar          |     3 |      4 | destornillador    |    200 | Producto teclado actualizado correctamente         |
      | caja            | moldear             |     1 |      7 | martillo          |    200 | Producto caja actualizado correctamente            |
      | caja            | quemar              |     2 |      8 | soplete           |    200 | Producto caja actualizado correctamente            |
      | caja            | atornillar          |     3 |      9 | destornillador    |    200 | Producto caja actualizado correctamente            |
