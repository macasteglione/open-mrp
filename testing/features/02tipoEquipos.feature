# language: es
Característica: gestión de talleres

  Esquema del escenario: Nuevo tipo de equipo para asignar a talleres
    Dado que se ingresa el tipo de equipo con "<nombre>"
    Cuando presiono el botón de guardar tipos
    Entonces se espera el siguiente <status> con la "<respuesta>" tipos

    Ejemplos:
      | nombre            | status | respuesta                                                 |
      | cierra            |    200 | Tipo de equipo cierra registrado correctamente            |
      | lijadora de banda |    200 | Tipo de equipo lijadora de banda registrado correctamente |
      | mesa ensamblado   |    200 | Tipo de equipo mesa ensamblado registrado correctamente   |
      | fresadora         |    200 | Tipo de equipo fresadora registrado correctamente         |
      | Cepillo Garlopa   |    200 | Tipo de equipo Cepillo Garlopa registrado correctamente   |
      | dobladora         |    200 | Tipo de equipo dobladora registrado correctamente         |
      | martillo          |    200 | Tipo de equipo martillo registrado correctamente          |
      | soplete           |    200 | Tipo de equipo soplete registrado correctamente           |
      | destornillador    |    200 | Tipo de equipo destornillador registrado correctamente    |
