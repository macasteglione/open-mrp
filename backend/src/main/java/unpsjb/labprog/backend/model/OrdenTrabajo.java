package unpsjb.labprog.backend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@Entity
public class OrdenTrabajo {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    // @JsonManagedReference
    // @OneToMany(cascade = CascadeType.ALL, mappedBy = "ordenTrabajo")
    // private Collection<Planificacion> planificaciones;
}
