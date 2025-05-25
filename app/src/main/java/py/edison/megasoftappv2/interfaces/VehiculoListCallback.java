package py.edison.megasoftappv2.interfaces;

import java.util.List;

import py.edison.megasoftappv2.entidades.Vehiculo;
import py.edison.megasoftappv2.servicios.VehiculoService;

public interface VehiculoListCallback {
    void onSuccess(List<Vehiculo> listaObtenida);

    void onError(String error);
}
