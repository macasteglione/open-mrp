package unpsjb.labprog.backend.model;

import java.sql.Timestamp;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Hueco {
    public Hueco(Timestamp inicio, Timestamp fin, Equipo equipo) {
        this.inicio = inicio;
        this.fin = fin;
        this.equipo = equipo;
    }

    private Timestamp inicio;
    private Timestamp fin;
    private Equipo equipo;

    public long tiempo() {
        return ((fin.getTime() - inicio.getTime()) / (60000)) * equipo.getCapacidad();
    }
}
