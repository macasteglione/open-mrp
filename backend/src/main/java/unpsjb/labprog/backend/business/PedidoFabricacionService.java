package unpsjb.labprog.backend.business;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import unpsjb.labprog.backend.model.Estado;
import unpsjb.labprog.backend.model.PedidoFabricacion;
import unpsjb.labprog.backend.model.Producto;
import unpsjb.labprog.backend.model.Registro;

@Service
public class PedidoFabricacionService {
    @Autowired
    PedidoFabricacionRepository repository;
    @Autowired
    RegistroService registroService;

    @Cacheable("primeraFechaPedido")
    public Timestamp primeraFechaPedido() {
        return repository.getFechaMinima();
    }

    @Cacheable("ultimaFechaPedido")
    public Timestamp ultimaFechaEntrega() {
        return repository.getFechaMaxima();
    }

    public PedidoFabricacion findById(int id) {
        return repository.findById(id).orElse(null);
    }

    public Collection<PedidoFabricacion> recuperarPedidosNoPlanificados() {
        List<PedidoFabricacion> result = new ArrayList<PedidoFabricacion>();
        result.addAll(repository.recuperarPedidosNoPlanificados());
        return result;
    }

    public Collection<PedidoFabricacion> recuperarPedidosPlanificados() {
        List<PedidoFabricacion> result = new ArrayList<PedidoFabricacion>();
        result.addAll(repository.recuperarPedidosPlanificados());
        return result;
    }

    public Page<PedidoFabricacion> findByPage(int page, int size) {
        return repository.findAll(PageRequest.of(page, size));
    }

    public Collection<PedidoFabricacion> findAll() {
        List<PedidoFabricacion> result = new ArrayList<>();
        repository.findAll().forEach(e -> result.add(e));
        return result;
    }

    public Collection<PedidoFabricacion> findAllOrderByFechaPedido() {
        List<PedidoFabricacion> result = new ArrayList<>();
        repository.findAll().forEach(e -> result.add(e));
        return result;
    }

    public Collection<PedidoFabricacion> findAllOrderByFechaEntrega(Timestamp fecha) {
        List<PedidoFabricacion> result = new ArrayList<>();
        repository.findAllOrderByFechaEntrega(fecha).forEach(e -> result.add(e));
        return result;
    }

    @CacheEvict(value = { "primeraFechaPedido", "ultimaFechaEntrega" }, allEntries = true)
    @Transactional
    public PedidoFabricacion create(PedidoFabricacion aPedidoFabricacion) {
        aPedidoFabricacion.setEstado(Estado.PENDIENTE);
        return repository.save(aPedidoFabricacion);
    }

    @CacheEvict(value = { "primeraFechaPedido", "ultimaFechaEntrega" }, allEntries = true)
    @Transactional
    public PedidoFabricacion save(PedidoFabricacion e) {
        return repository.save(e);
    }

    @CacheEvict(value = { "primeraFechaPedido", "ultimaFechaEntrega" }, allEntries = true)
    @Transactional
    public void delete(int id) {
        repository.deleteById(id);
    }

    public PedidoFabricacion deshacerPedido(int idPedido) {
        PedidoFabricacion aPedidoOrNull = repository.findById(idPedido).orElse(null);
        if (aPedidoOrNull == null)
            return null;

        if (aPedidoOrNull.getEstado().equals(Estado.FINALIZADO)) {
            registroService.save(new Registro("No se pudo borrar la planificacion del pedido con id: "
                    + aPedidoOrNull.getId() + " debido a que ya terminó de planificarse"));
            return null;
        }

        aPedidoOrNull.setEstado(Estado.PENDIENTE);
        aPedidoOrNull.getPlanificaciones().clear();
        registroService.save(new Registro("Se desplanificó el pedido con id: " + aPedidoOrNull.getId()));

        return save(aPedidoOrNull);
    }

    public PedidoFabricacion search(long cuit, Timestamp fechaPedido, Timestamp fechaEntrega, Producto producto,
            int cantidad) {
        return repository.search(cuit, fechaPedido, fechaEntrega, producto, cantidad).orElse(null);
    }

    public Page<PedidoFabricacion> buscarPedido(String term, int page, int size) {
        return repository.buscarPedido("%" + term.toUpperCase() + "%", PageRequest.of(page, size));
    }
}
