package py.edison.megasoftappv2.servicios;

import java.util.List;

import py.edison.megasoftappv2.entidades.Client;

public abstract class ClienteListCallback {
    public abstract void onSuccess(List<Client> clientes);

    public abstract void onError(String error);
}
