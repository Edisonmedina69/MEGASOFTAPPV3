package py.edison.megasoftappv2.utils;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

import py.edison.megasoftappv2.entidades.Client;
import py.edison.megasoftappv2.entidades.Flete;
import py.edison.megasoftappv2.entidades.Vehiculo;

public class FirebaseDatabaseHelper {
    private static final String CLIENTES_NODE = "clientes";
    private static final String FLETES_NODE = "fletes";
    private static final String VEHICULOS_NODE = "vehiculos";

    private DatabaseReference databaseReference;

    public FirebaseDatabaseHelper() {
        databaseReference = FirebaseDatabase.getInstance().getReference();
    }

    // Métodos para clientes
    public String agregarCliente(Client cliente, DatabaseReference.CompletionListener listener) {
        String key = cliente.getId();
        if(key == null || key.isEmpty()) {
            key = databaseReference.child(CLIENTES_NODE).push().getKey();
            cliente.setId(key);
        }
        databaseReference.child(CLIENTES_NODE).child(key).setValue(cliente, listener);
        return key;
    }

    public void obtenerCliente(String id, ValueEventListener listener) {
        databaseReference.child(CLIENTES_NODE).child(id).addListenerForSingleValueEvent(listener);
    }

    public void actualizarCliente(String id, Map<String, Object> updates, DatabaseReference.CompletionListener listener) {
        databaseReference.child(CLIENTES_NODE).child(id).updateChildren(updates, listener);
    }

    // Métodos similares para Fletes y Vehiculos...

    // Método genérico para queries
    public Query getQuery(String node, String orderBy, String equalTo) {
        return databaseReference.child(node).orderByChild(orderBy).equalTo(equalTo);
    }
}