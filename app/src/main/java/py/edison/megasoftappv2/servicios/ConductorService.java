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

import py.edison.megasoftappv2.entidades.Conductor;

public class ConductorService {

    private DatabaseReference dbRef;
    private static final String TAG = "ConductorService";

    public interface ConductorCallback {
        void onCallback(Conductor conductor);
    }

    public interface ConductorListCallback {
        void onSuccess(List<Conductor> conductores);
        void onError(String error);
    }

    public ConductorService() {
        dbRef = FirebaseDatabase.getInstance().getReference("conductores");
    }

    public void registrarConductor(Conductor conductor, ConductorListCallback callback) {
        String key = dbRef.push().getKey();
        if (key != null) {
            conductor.setId(key);
            dbRef.child(key).setValue(conductor)
                    .addOnSuccessListener(aVoid -> {
                        Log.d(TAG, "Conductor registrado");
                        listarTodosConductores(callback); // Recargar lista
                    })
                    .addOnFailureListener(e -> {
                        Log.e(TAG, "Error al registrar conductor", e);
                        callback.onError(e.getMessage());
                    });
        } else {
            callback.onError("No se pudo generar una clave para el conductor");
        }
    }

    public void listarTodosConductores(ConductorListCallback callback) {
        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Conductor> conductores = new ArrayList<>();
                for (DataSnapshot data : snapshot.getChildren()) {
                    Conductor conductor = data.getValue(Conductor.class);
                    if (conductor != null) {
                        conductor.setId(data.getKey());
                        conductores.add(conductor);
                    }
                }
                callback.onSuccess(conductores);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "Error al obtener conductores", error.toException());
                callback.onError(error.getMessage());
            }
        });
    }

    public void buscarConductorPorId(String id, ConductorCallback callback) {
        dbRef.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Conductor conductor = snapshot.getValue(Conductor.class);
                if (conductor != null) {
                    conductor.setId(snapshot.getKey());
                    callback.onCallback(conductor);
                } else {
                    callback.onCallback(null);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "Error al buscar conductor", error.toException());
            }
        });
    }

    public void actualizarConductor(Conductor conductor, ConductorListCallback callback) {
        if (conductor.getId() == null) {
            Log.e(TAG, "Conductor sin ID, no se puede actualizar");
            callback.onError("Conductor sin ID, no se puede actualizar");
            return;
        }
        dbRef.child(conductor.getId()).setValue(conductor)
                .addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "Conductor actualizado");
                    listarTodosConductores(callback); // Recargar lista
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error al actualizar conductor", e);
                    callback.onError(e.getMessage());
                });
    }

    public void eliminarConductor(String id, ConductorListCallback callback) {
        dbRef.child(id).removeValue()
                .addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "Conductor eliminado");
                    listarTodosConductores(callback); // Recargar lista
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error al eliminar conductor", e);
                    callback.onError(e.getMessage());
                });
    }
}