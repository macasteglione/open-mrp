package unpsjb.labprog.backend.business;

import java.util.Collection;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import unpsjb.labprog.backend.model.Taller;

public interface TallerRepository extends CrudRepository<Taller, Integer>, PagingAndSortingRepository<Taller, Integer> {
    @Query("SELECT e FROM Taller e WHERE e.codigo = ?1")
    Optional<Taller> findByCode(String code);

    @Query("SELECT e FROM Taller e WHERE UPPER(e.nombre) LIKE ?1")
    Collection<Taller> search(String term);
}
