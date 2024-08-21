package unpsjb.labprog.backend.presenter;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import unpsjb.labprog.backend.Response;
import unpsjb.labprog.backend.business.PedidoFabricacionService;
import unpsjb.labprog.backend.business.ProductoService;
import unpsjb.labprog.backend.model.PedidoFabricacion;
import unpsjb.labprog.backend.model.Producto;

@RestController
@RequestMapping("pedidos")
public class PedidoFabricacionPresenter {
    @Autowired
    PedidoFabricacionService service;
    @Autowired
    ProductoService productoService;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<Object> findAll() {
        return Response.ok(service.findAll());
    }

    @RequestMapping(value = "/page", method = RequestMethod.GET)
    public ResponseEntity<Object> findByPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return Response.ok(service.findByPage(page, size));
    }

    @RequestMapping(value = "/id/{id}", method = RequestMethod.GET)
    public ResponseEntity<Object> findById(@PathVariable("id") int id) {
        PedidoFabricacion aPedidoFabricacionOrNull = service.findById(id);
        return (aPedidoFabricacionOrNull != null) ? Response.ok(aPedidoFabricacionOrNull)
                : Response.notFound("Pedido id " + id + " no encontrado");
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Object> create(@RequestBody PedidoFabricacion aPedidoFabricacion) {
        if (aPedidoFabricacion.getId() != 0) {
            return Response.error(aPedidoFabricacion,
                    "Está intentando crear un PedidoFabricacion, éste no puede tener un id indefinido.");
        }
        return Response.ok(service.create(aPedidoFabricacion), "Pedido de fabricación generado correctamente");
    }

    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity<Object> update(@RequestBody PedidoFabricacion aPedidoFabricacion) {
        if (aPedidoFabricacion.getId() <= 0) {
            return Response.error(aPedidoFabricacion, "debe especificar un id válido para poder modificar el Pedido");
        }
        return Response.ok(service.save(aPedidoFabricacion), "Pedido de fabricación actualizado correctamente");
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Object> delete(@PathVariable("id") int id) {
        service.delete(id);
        return Response.ok("Pedido " + id + " borrado con éxito.");
    }

    @RequestMapping(value = "planificados", method = RequestMethod.GET)
    public ResponseEntity<Object> recuperarPedidosPlanificados() {
        List<PedidoFabricacion> aPedidosOrNull;

        aPedidosOrNull = new ArrayList<>(service.recuperarPedidosPlanificados());
        return (!aPedidosOrNull.isEmpty())
                ? Response.ok(aPedidosOrNull, "Pedidos planificados recuperados exitosamente")
                : Response.notFound("No hay pedidos planificados");
    }

    @RequestMapping(value = "noPlanificados", method = RequestMethod.GET)
    public ResponseEntity<Object> recuperarPedidosNoPlanificados() {
        List<PedidoFabricacion> aPedidosOrNull;

        aPedidosOrNull = new ArrayList<>(service.recuperarPedidosNoPlanificados());
        return (!aPedidosOrNull.isEmpty())
                ? Response.ok(aPedidosOrNull, "Pedidos no planificados recuperados exitosamente")
                : Response.notFound("No hay pedidos en estado no planificado");
    }

    @RequestMapping(value = "primerPedido", method = RequestMethod.GET)
    public ResponseEntity<Object> primeraFechaPedido() {
        return Response.ok(service.primeraFechaPedido());
    }

    @RequestMapping(value = "ultimaEntrega", method = RequestMethod.GET)
    public ResponseEntity<Object> ultimaFechaEntrega() {
        return Response.ok(service.ultimaFechaEntrega());
    }

    @RequestMapping(value = "deshacer/{id}", method = RequestMethod.GET)
    public ResponseEntity<Object> deshacerPlanificacion(@PathVariable("id") int idPedido) {

        PedidoFabricacion aPedidosOrNull = service.deshacerPedido(idPedido);

        return (aPedidosOrNull != null) ? Response.ok(aPedidosOrNull, "Pedido desplanificado exitosamente")
                : Response.notFound("No se pudo deshacer la planificacion del pedido");
    }

    // Es para el test de planificacion de producto
    @RequestMapping(value = "/search/{cuit}/{fechaPedido}/{fechaEntrega}/{producto}/{cantidad}", method = RequestMethod.GET)
    public ResponseEntity<Object> search(@PathVariable("cuit") long cuit,
            @PathVariable("fechaPedido") String fechaPedido, @PathVariable("fechaEntrega") String fechaEntrega,
            @PathVariable("producto") String producto, @PathVariable("cantidad") int cantidad) {
        Producto aProducto = productoService.findByName(producto);
        return (aProducto != null)
                ? Response.ok(service.search(cuit, new Timestamp(Date.valueOf(fechaPedido).getTime()),
                        new Timestamp(Date.valueOf(fechaEntrega).getTime()), aProducto, cantidad))
                : Response.notFound("No se encontro el producto");
    }

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public ResponseEntity<Object> buscarPedido(@RequestParam String term,
            @RequestParam int page,
            @RequestParam int size) {
        return Response.ok(service.buscarPedido(term, page, size));
    }
}