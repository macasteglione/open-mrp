package unpsjb.labprog.backend.model;

import java.sql.Timestamp;
import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@Entity
public class PedidoFabricacion {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private Timestamp fechaPedido;
    private Timestamp fechaEntrega;

    @Column(nullable = false)
    private int cantidad;

    @ManyToOne
    private Producto producto;

    @ManyToOne
    private Cliente cliente;

    @Enumerated(EnumType.STRING)
    private Estado estado;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "pedido")
    @JsonIgnore
    private Collection<Planificacion> planificaciones;
}
