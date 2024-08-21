package unpsjb.labprog.backend.business;

import java.util.Collection;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import unpsjb.labprog.backend.model.Equipo;

public interface EquipoRepository extends CrudRepository<Equipo, Integer>, PagingAndSortingRepository<Equipo, Integer> {
    @Query("SELECT e FROM Equipo e WHERE e.codigo = ?1")
    Optional<Equipo> findByCode(String code);

    @Query("SELECT e FROM Equipo e WHERE UPPER(e.codigo) LIKE ?1")
    Collection<Equipo> search(String term);
}