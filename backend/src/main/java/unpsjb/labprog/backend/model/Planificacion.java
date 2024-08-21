package unpsjb.labprog.backend.model;

import java.sql.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@Entity
public class Planificacion {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @ManyToOne
    private Tarea tarea;

    @ManyToOne
    private PedidoFabricacion pedido;

    @ManyToOne
    private Equipo equipo;

    @Column(nullable = false)
    private Timestamp inicio;

    @Column(nullable = false)
    private Timestamp fin;
}
