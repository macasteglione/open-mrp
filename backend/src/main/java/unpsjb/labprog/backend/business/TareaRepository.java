package unpsjb.labprog.backend.business;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import unpsjb.labprog.backend.model.Tarea;

public interface TareaRepository extends CrudRepository<Tarea, Integer>, PagingAndSortingRepository<Tarea, Integer> {
    @Query("SELECT e FROM Tarea e WHERE UPPER(e.nombre) LIKE ?1")
    Collection<Tarea> search(String term);
}