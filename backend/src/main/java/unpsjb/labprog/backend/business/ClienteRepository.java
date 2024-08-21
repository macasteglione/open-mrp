package unpsjb.labprog.backend.business;

import java.util.Collection;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import unpsjb.labprog.backend.model.Cliente;

public interface ClienteRepository
        extends CrudRepository<Cliente, Integer>, PagingAndSortingRepository<Cliente, Integer> {
    @Query("SELECT e FROM Cliente e WHERE UPPER(e.nombre) LIKE ?1")
    Collection<Cliente> search(String term);

    @Query("SELECT e FROM Cliente e WHERE e.cuit = ?1")
    Optional<Cliente> findByCuit(long cuit);
}
