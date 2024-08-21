package unpsjb.labprog.backend;

import java.sql.Timestamp;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import unpsjb.labprog.backend.business.PedidoFabricacionService;
import unpsjb.labprog.backend.model.Cliente;
import unpsjb.labprog.backend.model.Estado;
import unpsjb.labprog.backend.model.PedidoFabricacion;
import unpsjb.labprog.backend.model.Producto;

@Service
public class Utils {
    @Autowired
    PedidoFabricacionService service;

    public PedidoFabricacion crearPedidoDeUnProducto(Producto producto, int cantidad, Timestamp fecha,
            Cliente cliente) {
        PedidoFabricacion pedido = new PedidoFabricacion();
        pedido.setProducto(producto);
        pedido.setCantidad(cantidad);
        pedido.setEstado(Estado.PENDIENTE);
        pedido.setFechaPedido(fecha);
        pedido.setFechaEntrega(Timestamp.valueOf(fecha.toLocalDateTime().plusYears(1)));
        pedido.setCliente(cliente);
        pedido.setPlanificaciones(new ArrayList<>());
        return pedido;
    }
}
