package unpsjb.labprog.backend.business;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import unpsjb.labprog.backend.model.PedidoFabricacion;
import unpsjb.labprog.backend.model.Producto;

public interface PedidoFabricacionRepository extends CrudRepository<PedidoFabricacion, Integer>,
        PagingAndSortingRepository<PedidoFabricacion, Integer> {
    @Query("SELECT MIN(p.fechaPedido) FROM PedidoFabricacion p")
    Timestamp getFechaMinima();

    @Query("SELECT MAX(p.fechaEntrega) FROM PedidoFabricacion p")
    Timestamp getFechaMaxima();

    @Query("SELECT e FROM PedidoFabricacion e WHERE e.estado = Estado.PLANIFICADO")
    Collection<PedidoFabricacion> recuperarPedidosPlanificados();

    @Query("SELECT e FROM PedidoFabricacion e WHERE e.estado = Estado.NO_PLANIFICABLE")
    Collection<PedidoFabricacion> recuperarPedidosNoPlanificados();

    @Query("SELECT p FROM PedidoFabricacion p ORDER BY p.fechaPedido ASC")
    Collection<PedidoFabricacion> findAllOrderByFechaPedido();

    @Query("SELECT p FROM PedidoFabricacion p WHERE p.fechaEntrega <= :fecha ORDER BY p.fechaEntrega ASC")
    Collection<PedidoFabricacion> findAllOrderByFechaEntrega(Timestamp fecha);

    @Query("SELECT e FROM PedidoFabricacion e WHERE UPPER(e.cliente.nombre) LIKE ?1 OR UPPER(e.producto.nombre) LIKE ?1")
    Page<PedidoFabricacion> buscarPedido(String term, Pageable pageable);

    @Query("SELECT e FROM PedidoFabricacion e WHERE e.cliente.cuit = :cuit AND e.fechaPedido = :fechaPedido AND e.fechaEntrega = :fechaEntrega AND e.producto = :producto AND e.cantidad = :cantidad")
    Optional<PedidoFabricacion> search(long cuit, Timestamp fechaPedido, Timestamp fechaEntrega, Producto producto,
            int cantidad);
}