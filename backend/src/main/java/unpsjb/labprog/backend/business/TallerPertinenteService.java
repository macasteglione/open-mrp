package unpsjb.labprog.backend.business;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import unpsjb.labprog.backend.model.Equipo;
import unpsjb.labprog.backend.model.Producto;
import unpsjb.labprog.backend.model.Taller;
import unpsjb.labprog.backend.model.Tarea;
import unpsjb.labprog.backend.model.TipoEquipo;

@Service
public class TallerPertinenteService {
    @Autowired
    private TallerService tallerService;

    public Taller primerTallerPertinente(Producto producto) {
        List<Taller> talleresPertinentes = new ArrayList<>(todosTallerPertinente(producto));
        return !talleresPertinentes.isEmpty() ? talleresPertinentes.get(0) : null;
    }

    public Collection<Taller> todosTallerPertinente(Producto producto) {
        Set<TipoEquipo> tiposEquipoProducto = getTiposEquipoProducto(producto);
        return tallerService.findAllOrderByCode().stream()
                .filter(taller -> contieneTiposEquipo(taller, tiposEquipoProducto))
                .collect(Collectors.toList());
    }

    private Set<TipoEquipo> getTiposEquipoProducto(Producto producto) {
        return producto.getTareas().stream()
                .map(Tarea::getTipoEquipo)
                .collect(Collectors.toSet());
    }

    private boolean contieneTiposEquipo(Taller taller, Set<TipoEquipo> tiposEquipo) {
        Set<TipoEquipo> tiposEquipoTaller = taller.getEquipos().stream()
                .map(Equipo::getTipoEquipo)
                .collect(Collectors.toSet());
        return tiposEquipoTaller.containsAll(tiposEquipo);
    }

    public Collection<Taller> talleresOrdenados(Producto producto) {
        Set<TipoEquipo> tiposEquipoProducto = getTiposEquipoProducto(producto);
        return tallerService.findAllOrderByCode().stream()
                .filter(taller -> contieneTiposEquipo(taller, tiposEquipoProducto))
                .collect(Collectors.toList());
    }

    public Collection<Taller> talleresPorEficiencia(Producto producto) {
        List<Taller> talleresPertinentes = new ArrayList<>(todosTallerPertinente(producto));
        ordenarPorCapacidadPromedio(talleresPertinentes, producto);
        return talleresPertinentes;
    }

    private void ordenarPorCapacidadPromedio(List<Taller> talleres, Producto producto) {
        Set<TipoEquipo> tiposEquipoProducto = getTiposEquipoProducto(producto);
        talleres.sort(
                Comparator.comparingDouble(taller -> calcularCapacidadPromedio((Taller) taller, tiposEquipoProducto))
                        .reversed());
    }

    private double calcularCapacidadPromedio(Taller taller, Set<TipoEquipo> tiposEquipoProducto) {
        return taller.getEquipos().stream()
                .filter(equipo -> tiposEquipoProducto.contains(equipo.getTipoEquipo()))
                .mapToDouble(Equipo::getCapacidad)
                .average()
                .orElse(0);
    }
}
