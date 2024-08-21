package unpsjb.labprog.backend.business;

import java.util.Collection;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import unpsjb.labprog.backend.model.TipoEquipo;

public interface TipoEquipoRepository
        extends CrudRepository<TipoEquipo, Integer>, PagingAndSortingRepository<TipoEquipo, Integer> {
    @Query("SELECT e FROM TipoEquipo e WHERE UPPER(e.nombre) LIKE ?1")
    Collection<TipoEquipo> search(String term);

    @Query("SELECT e FROM TipoEquipo e WHERE e.nombre LIKE ?1")
    Optional<TipoEquipo> findByName(String nombre);
}
