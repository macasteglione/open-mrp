package unpsjb.labprog.backend.presenter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import unpsjb.labprog.backend.Response;
import unpsjb.labprog.backend.business.ClienteService;
import unpsjb.labprog.backend.model.Cliente;

@RestController
@RequestMapping("clientes")
public class ClientePresenter {
    @Autowired
    ClienteService service;

    @RequestMapping(value = "/search/{term}", method = RequestMethod.GET)
    public ResponseEntity<Object> search(@PathVariable("term") String term) {
        return Response.ok(service.search(term));
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Object> create(@RequestBody Cliente aCliente) {
        return (aCliente.getId() != 0)
                ? Response.notFound("Esta intentando crear un cliente. Esta no puede tener un id indefinido.")
                : Response.ok(service.save(aCliente),
                        "Cliente " + aCliente.getNombre() + " (" + aCliente.getCuit() + ") registrado correctamente");
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<Object> findAll() {
        return Response.ok(service.findAll());
    }

    @RequestMapping(value = "/id/{id}", method = RequestMethod.GET)
    public ResponseEntity<Object> findById(@PathVariable("id") int id) {
        Cliente aCliente = service.findById(id);
        return (aCliente != null) ? Response.ok(aCliente) : Response.notFound("Cliente " + id + " no encontrado.");
    }

    @RequestMapping(value = "/page", method = RequestMethod.GET)
    public ResponseEntity<Object> findByPage(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return Response.ok(service.findByPage(page, size));
    }

    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity<Object> update(@RequestBody Cliente aCustomer) {
        return (aCustomer.getId() <= 0) ? Response.notFound("Debe especificar un id valido para modificar un cliente.")
                : Response.ok(service.save(aCustomer));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Object> delete(@PathVariable("id") int id) {
        service.delete(id);
        return Response.ok("Cliente " + id + " borrado con exito.");
    }

    @RequestMapping(value = "/cuit/{code}", method = RequestMethod.GET)
    public ResponseEntity<Object> findByCodeTaller(@PathVariable("code") long code) {
        Cliente aCliente = service.findByCuit(code);
        return (aCliente != null) ? Response.ok(aCliente) : Response.notFound("Cliente " + code + " no encontrado.");
    }
}
