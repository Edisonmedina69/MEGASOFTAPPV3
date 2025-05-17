package py.edison.megasoftappv2.servicios;

import py.edison.megasoftappv2.entidades.Vehiculo;
import java.util.ArrayList;
import java.util.List;

public class VehiculoService {
    private List<Vehiculo> vehiculos;

    public VehiculoService() {
        this.vehiculos = new ArrayList<>();
    }

    public void registrarVehiculo(Vehiculo vehiculo) {
        vehiculos.add(vehiculo);
    }

    public List<Vehiculo> obtenerVehiculosDisponibles() {
        List<Vehiculo> disponibles = new ArrayList<>();
        for (Vehiculo v : vehiculos) {
            if (v.isDisponible()) {
                disponibles.add(v);
            }
        }
        return disponibles;
    }

    public Vehiculo buscarVehiculoPorId(String id) {
        for (Vehiculo v : vehiculos) {
            if (v.getId().equals(id)) {
                return v;
            }
        }
        return null;
    }
}