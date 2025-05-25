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

import py.edison.megasoftappv2.entidades.Vehiculo;
import py.edison.megasoftappv2.interfaces.VehiculoListCallback;

public class VehiculoService {

    private DatabaseReference dbRef;
    private static final String TAG = "VehiculoService";

    public VehiculoService() {
        dbRef = FirebaseDatabase.getInstance().getReference("vehiculos");
    }


    public void obtenerVehiculosDisponibles(VehiculoListCallback callback) {
        dbRef.orderByChild("disponible").equalTo(true)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        List<Vehiculo> disponibles = new ArrayList<>();
                        for (DataSnapshot data : snapshot.getChildren()) {
                            Vehiculo vehiculo = data.getValue(Vehiculo.class);
                            if (vehiculo != null) {
                                vehiculo.setId(data.getKey());
                                disponibles.add(vehiculo);
                            }
                        }
                        callback.onSuccess(disponibles);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e(TAG, "Error al obtener vehículos disponibles", error.toException());
                        callback.onError(error.getMessage());
                    }
                });
    }

    public void buscarVehiculoPorId(String id, VehiculoCallback callback) {
        dbRef.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Vehiculo vehiculo = snapshot.getValue(Vehiculo.class);
                if (vehiculo != null) {
                    vehiculo.setId(snapshot.getKey());
                    callback.onCallback(vehiculo);
                } else {
                    callback.onCallback(null);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "Error al buscar vehículo", error.toException());
                callback.onCallback(null);
            }
        });
    }


    public void obtenerVehiculos(py.edison.megasoftappv2.interfaces.VehiculoListCallback vehiculoListCallback) {
        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Vehiculo> listaVehiculos = new ArrayList<>();
                for (DataSnapshot data : snapshot.getChildren()) {
                    Vehiculo vehiculo = data.getValue(Vehiculo.class);
                    if (vehiculo != null) {
                        vehiculo.setId(data.getKey());
                        listaVehiculos.add(vehiculo);
                    }
                }
                vehiculoListCallback.onSuccess(listaVehiculos);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "Error al obtener vehículos", error.toException());
                vehiculoListCallback.onError(error.getMessage());
            }
        });
    }

    public void guardarVehiculo(Vehiculo vehiculo, py.edison.megasoftappv2.interfaces.VehiculoListCallback vehículoGuardadoExitosamente) {
        String key = dbRef.push().getKey();
        py.edison.megasoftappv2.interfaces.VehiculoListCallback callback = null;
        if (key != null) {
            vehiculo.setId(key);
            dbRef.child(key).setValue(vehiculo)
                    .addOnSuccessListener(aVoid -> {
                        Log.d(TAG, "Vehículo guardado");
                        obtenerVehiculos(callback); // Recargar lista
                    })
                    .addOnFailureListener(e -> {
                        Log.e(TAG, "Error al guardar vehículo", e);
                        callback.onError(e.getMessage());
                    });
        } else {
            callback.onError("No se pudo generar una clave para el vehículo");
        }
    }



    public void actualizarVehiculo(Vehiculo vehiculo, VehiculoListCallback callback) {
        if (vehiculo.getId() == null) {
            if (callback != null) {
                callback.onError("Vehículo sin ID, no se puede actualizar");
            }
            return;
        }

        dbRef.child(vehiculo.getId()).setValue(vehiculo)
                .addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "Vehículo actualizado");
                    if (callback != null) {
                        obtenerVehiculos(callback);
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error al actualizar vehículo", e);
                    if (callback != null) {
                        callback.onError(e.getMessage());
                    }
                });
    }


    public void eliminarVehiculo(String vehiculoId, py.edison.megasoftappv2.interfaces.VehiculoListCallback vehículoEliminado) {
        py.edison.megasoftappv2.interfaces.VehiculoListCallback callback = null;
        dbRef.child(vehiculoId).removeValue()
                .addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "Vehículo eliminado");
                    obtenerVehiculos(callback); // Recargar lista
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error al eliminar vehículo", e);
                    callback.onError(e.getMessage());
                });
    }

    public interface VehiculoCallback {
        void onCallback(Vehiculo vehiculo);
    }

}