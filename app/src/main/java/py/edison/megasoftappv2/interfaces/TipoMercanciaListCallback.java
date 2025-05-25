package py.edison.megasoftappv2.interfaces;

import java.util.List;

import py.edison.megasoftappv2.entidades.TipoMercancia;

public interface TipoMercanciaListCallback {
    void onSuccess(List<TipoMercancia> tipos);

    void onError(String error);
}
