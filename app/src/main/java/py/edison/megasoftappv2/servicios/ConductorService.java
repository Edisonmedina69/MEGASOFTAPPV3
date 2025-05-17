package py.edison.megasoftappv2.servicios;

import java.util.ArrayList;
import java.util.List;

import py.edison.megasoftappv2.entidades.Client;
import py.edison.megasoftappv2.entidades.Conductor;

public class ConductorService {

    private List<Conductor> conductors;
    private Conductor conductorActualizado;

    public ConductorService() {
        this.conductors = new ArrayList<>();
    }

    public void registrarConductores(Conductor conductor) {
        conductors.add(conductor);
    }

    public Conductor buscarConductorPorId(String id) {
        for (Conductor conductor : conductors) {
            if (conductor.getId().equals(id)) {  // Fixed: Changed Conductor.getId() to conductor.getId()
                return conductor;
            }
        }
        return null;  // Moved outside the loop to check all conductors first
    }

    public List<Conductor> listarTodosCondutores() {  // Changed to lowercase (Java naming convention)
        return conductors;
    }

    public void actualizarConductor(Conductor conductorActualizado) {
        for (int i = 0; i < conductors.size(); i++) {
            if (conductors.get(i).getId().equals(conductorActualizado.getId())) {
                conductors.set(i, conductorActualizado);
                break;
            }
        }
    }
}