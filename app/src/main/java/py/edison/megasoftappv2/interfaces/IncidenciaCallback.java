package py.edison.megasoftappv2.interfaces;

import py.edison.megasoftappv2.entidades.Incidencia;

public interface IncidenciaCallback {
    void onSuccess(Incidencia incidencia);

    void onError(String message);
}
