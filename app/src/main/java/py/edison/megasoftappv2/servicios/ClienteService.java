package py.edison.megasoftappv2.servicios;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import py.edison.megasoftappv2.entidades.Client;

public class ClienteService {

    private DatabaseReference dbRef;
    private static final String TAG = "ClienteService";

    public void actualizarCliente(Client cliente, py.edison.megasoftappv2.servicios.ClienteListCallback clienteActualizado) {
        ClienteListCallback callback = null;
        if (cliente.getId() == null) {
            Log.e(TAG, "Cliente sin ID, no se puede actualizar");
            callback.onError("Cliente sin ID, no se puede actualizar");
            return;
        }
        dbRef.child(cliente.getId()).setValue(cliente)
                .addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "Cliente actualizado");
                    obtenerClientes(callback); // Recargar lista
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error al actualizar cliente", e);
                    callback.onError(e.getMessage());
                });
    }



    public interface ClienteCallback {
        void onCallback(Client cliente);
    }

    public ClienteService() {
        dbRef = FirebaseDatabase.getInstance().getReference("clientes");
    }
    public void guardarCliente(Client cliente, ClienteListCallback callback) {
        String key = dbRef.push().getKey();
        if (key != null) {
            cliente.setId(key);
            dbRef.child(key).setValue(cliente)
                    .addOnSuccessListener(aVoid -> {
                        Log.d(TAG, "Cliente guardado");
                        obtenerClientes(callback); // Recargar lista
                    })
                    .addOnFailureListener(e -> {
                        Log.e(TAG, "Error al guardar cliente", e);
                        callback.onError(e.getMessage());
                    });
        } else {
            callback.onError("No se pudo generar una clave para el cliente");
        }
    }



    public void obtenerClientes(ClienteListCallback callback) {
        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Client> clientes = new ArrayList<>();
                for (DataSnapshot data : snapshot.getChildren()) {
                    Client cliente = data.getValue(Client.class);
                    if (cliente != null) {
                        cliente.setId(data.getKey());
                        clientes.add(cliente);
                    }
                }
                callback.onSuccess(clientes);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "Error al obtener clientes", error.toException());
                callback.onError(error.getMessage());
            }
        });
    }

    public void buscarClientePorId(String id, ClienteCallback callback) {
        dbRef.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Client cliente = snapshot.getValue(Client.class);
                if (cliente != null) {
                    cliente.setId(snapshot.getKey());
                    callback.onCallback(cliente);
                } else {
                    callback.onCallback(null);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "Error al buscar cliente", error.toException());
            }
        });
    }



    public void eliminarCliente(String id, ClienteListCallback callback) {
        dbRef.child(id).removeValue()
                .addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "Cliente eliminado");
                    obtenerClientes(callback); // Recargar lista
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error al eliminar cliente", e);
                    callback.onError(e.getMessage());
                });
    }
}