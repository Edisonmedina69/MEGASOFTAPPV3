package py.edison.megasoftappv2.interfaces;

import java.util.List;

import py.edison.megasoftappv2.entidades.Client;

public interface ClienteListCallback {
    void onSuccess(List<Client> clientes);
    void onError(String error);
}
