package py.edison.megasoftappv2.servicios;

import androidx.annotation.NonNull;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import py.edison.megasoftappv2.entidades.TipoMercancia;
import py.edison.megasoftappv2.interfaces.TipoMercanciaListCallback;

public class TipoMercanciaService {
    private static final String TAG = "TipoMercanciaService";
    private final DatabaseReference dbRef;
    private ValueEventListener valueEventListener;

    public TipoMercanciaService() {
        dbRef = FirebaseDatabase.getInstance().getReference("tiposMercancia");
    }

    public void guardarTipoMercancia(TipoMercancia tipo, TipoMercanciaListCallback callback) {
        try {
            validarTipoMercancia(tipo);

            String key = tipo.getId() != null ? tipo.getId() : dbRef.push().getKey();
            if (key == null) {
                callback.onError("No se pudo generar clave para el tipo");
                return;
            }

            tipo.setId(key);
            dbRef.child(key).setValue(tipo)
                    .addOnSuccessListener(aVoid -> {
                        Log.i(TAG, "Tipo guardado: " + tipo.getDescripcion());
                        obtenerTiposMercanciaUnaVez(callback);
                    })
                    .addOnFailureListener(e -> {
                        Log.e(TAG, "Error al guardar: " + e.getMessage());
                        callback.onError("Error al guardar: " + e.getMessage());
                    });
        } catch (IllegalArgumentException e) {
            callback.onError(e.getMessage());
        }
    }

    public void obtenerTiposMercancia(TipoMercanciaListCallback callback) {
        removeListener(); // Limpiar listener anterior

        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<TipoMercancia> tipos = procesarSnapshot(snapshot);
                callback.onSuccess(tipos);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "Error en listener: " + error.getMessage());
                callback.onError(error.getMessage());
            }
        };

        dbRef.addValueEventListener(valueEventListener);
    }

    public void obtenerTiposMercanciaUnaVez(TipoMercanciaListCallback callback) {
        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<TipoMercancia> tipos = procesarSnapshot(snapshot);
                callback.onSuccess(tipos);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "Error en lectura única: " + error.getMessage());
                callback.onError(error.getMessage());
            }
        });
    }

    public void actualizarTipoMercancia(TipoMercancia tipo, TipoMercanciaListCallback callback) {
        try {
            validarTipoMercancia(tipo);

            if (tipo.getId() == null) {
                callback.onError("ID de tipo no puede ser nulo");
                return;
            }

            dbRef.child(tipo.getId()).setValue(tipo)
                    .addOnSuccessListener(aVoid -> {
                        Log.i(TAG, "Tipo actualizado: " + tipo.getId());
                        obtenerTiposMercanciaUnaVez(callback);
                    })
                    .addOnFailureListener(e -> {
                        Log.e(TAG, "Error al actualizar: " + e.getMessage());
                        callback.onError("Error al actualizar: " + e.getMessage());
                    });
        } catch (IllegalArgumentException e) {
            callback.onError(e.getMessage());
        }
    }

    public void eliminarTipoMercancia(String id, TipoMercanciaListCallback callback) {
        if (id == null || id.isEmpty()) {
            callback.onError("ID no válido para eliminar");
            return;
        }

        dbRef.child(id).removeValue()
                .addOnSuccessListener(aVoid -> {
                    Log.i(TAG, "Tipo eliminado: " + id);
                    obtenerTiposMercanciaUnaVez(callback);
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error al eliminar: " + e.getMessage());
                    callback.onError("Error al eliminar: " + e.getMessage());
                });
    }

    public void removeListener() {
        if (valueEventListener != null) {
            dbRef.removeEventListener(valueEventListener);
            valueEventListener = null;
        }
    }

    private List<TipoMercancia> procesarSnapshot(DataSnapshot snapshot) {
        List<TipoMercancia> tipos = new ArrayList<>();
        for (DataSnapshot data : snapshot.getChildren()) {
            TipoMercancia tipo = data.getValue(TipoMercancia.class);
            if (tipo != null) {
                tipo.setId(data.getKey());
                tipos.add(tipo);
                Log.d(TAG, "Procesado: " + tipo.getDescripcion());
            }
        }
        return tipos;
    }

    private void validarTipoMercancia(TipoMercancia tipo) throws IllegalArgumentException {
        if (tipo.getDescripcion() == null || tipo.getDescripcion().trim().isEmpty()) {
            throw new IllegalArgumentException("La descripción no puede estar vacía");
        }
    }
}