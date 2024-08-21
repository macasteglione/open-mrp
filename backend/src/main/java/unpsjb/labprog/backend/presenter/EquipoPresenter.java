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
import unpsjb.labprog.backend.business.EquipoService;
import unpsjb.labprog.backend.model.Equipo;

@RestController
@RequestMapping("equipos")
public class EquipoPresenter {
    @Autowired
    EquipoService service;

    @RequestMapping(value = "/search/{term}", method = RequestMethod.GET)
    public ResponseEntity<Object> search(@PathVariable("term") String term) {
        return Response.ok(service.search(term));
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Object> create(@RequestBody Equipo aEquipo) {
        return (aEquipo.getId() != 0)
                ? Response.notFound("Esta intentando crear un tipo de equipo. Esta no puede tener un id indefinido.")
                : Response.ok(service.save(aEquipo),
                        "Equipo " + aEquipo.getCodigo() + " registrado correctamente");
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<Object> findAll() {
        return Response.ok(service.findAll());
    }

    @RequestMapping(value = "/id/{id}", method = RequestMethod.GET)
    public ResponseEntity<Object> findById(@PathVariable("id") int id) {
        Equipo aEquipo = service.findById(id);
        return (aEquipo != null) ? Response.ok(
                aEquipo) : Response.notFound("Euipo " + id + " no encontrado.");
    }

    @RequestMapping(value = "/page", method = RequestMethod.GET)
    public ResponseEntity<Object> findByPage(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return Response.ok(service.findByPage(page, size));
    }

    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity<Object> update(@RequestBody Equipo aEquipo) {
        return (aEquipo.getId() <= 0) ? Response.notFound("Debe especificar un id valido para modificar un Equipo.")
                : Response.ok(service.save(aEquipo));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Object> delete(@PathVariable("id") int id) {
        service.delete(id);
        return Response.ok("Euipo " + id + " borrado con exito.");
    }
}
