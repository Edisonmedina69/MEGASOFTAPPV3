package py.edison.megasoftappv2.servicios;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import py.edison.megasoftappv2.entidades.ConfiguracionTarifa;
import py.edison.megasoftappv2.entidades.Flete;
import py.edison.megasoftappv2.utils.DateUtils;

public class FleteService {
    private final DatabaseReference fletesRef;
    private static final String TAG = "FleteService";
    private ValueEventListener activeListener;

    public FleteService() {
        this.fletesRef = FirebaseDatabase.getInstance().getReference("fletes");
        Log.d(TAG, "Servicio de Fletes inicializado");



    }

    public interface FleteCallback {
        void onSuccess(Flete flete);
        void onError(String error);
    }

    public interface FletesListCallback {
        void onSuccess(List<Flete> fletes);
        void onError(String error);
    }

    public void crearFlete(Flete flete, FleteCallback callback) {
        try {
            Log.d(TAG, "Intentando crear nuevo flete");
            String key = fletesRef.push().getKey();
            if (key == null) {
                Log.e(TAG, "No se pudo generar clave para nuevo flete");
                callback.onError("Error al generar ID");
                return;
            }

            flete.setId(key);
            Log.d(TAG, "Flete a guardar: " + flete.toString());

            fletesRef.child(key).setValue(flete)
                    .addOnSuccessListener(aVoid -> {
                        Log.d(TAG, "Flete guardado exitosamente: " + key);
                        callback.onSuccess(flete);
                    })
                    .addOnFailureListener(e -> {
                        Log.e(TAG, "Error al guardar flete: " + e.getMessage(), e);
                        callback.onError(e.getMessage());
                    });
        } catch (Exception e) {
            Log.e(TAG, "Excepción al crear flete: " + e.getMessage(), e);
            callback.onError("Error inesperado: " + e.getMessage());
        }
    }

    // Obtener flete por ID
    public void obtenerFlete(String id, FleteCallback callback) {
        if (id == null || id.isEmpty()) {
            callback.onError("ID de flete inválido");
            return;
        }

        fletesRef.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    if (!snapshot.exists()) {
                        callback.onError("Flete no encontrado");
                        return;
                    }

                    Flete flete = parseFleteFromSnapshot(snapshot);
                    callback.onSuccess(flete);
                } catch (Exception e) {
                    Log.e("FIREBASE_ERROR", "Error al parsear flete", e);
                    callback.onError("Error en los datos del flete: " + e.getMessage());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                callback.onError("Error de Firebase: " + error.getMessage());
            }
        });
    }

    // Actualizar flete completo
    public void actualizarFlete(Flete flete, FleteCallback callback) {
        if (flete.getId() == null || flete.getId().isEmpty()) {
            callback.onError("El flete no tiene un ID válido");
            return;
        }

        Map<String, Object> updates = new HashMap<>();
        updates.put("origen", flete.getOrigen());
        updates.put("destino", flete.getDestino());
        updates.put("distancia", flete.getDistancia());
        updates.put("peso", flete.getPeso());
        updates.put("tarifa", flete.getTarifa());
        updates.put("estado", flete.getEstado());
        updates.put("urgente", flete.isUrgente());
        updates.put("fechaActualizacion", DateUtils.getCurrentDateTime());

        fletesRef.child(flete.getId()).updateChildren(updates)
                .addOnSuccessListener(aVoid -> obtenerFlete(flete.getId(), callback))
                .addOnFailureListener(e -> callback.onError(e.getMessage()));
    }

    // Listar todos los fletes
    public void listarFletes(FletesListCallback callback) {
        limpiarListeners();
        activeListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Flete> fletes = new ArrayList<>();
                for (DataSnapshot data : snapshot.getChildren()) {
                    try {
                        Flete flete = parseFleteFromSnapshot(data);
                        fletes.add(flete);
                    } catch (Exception e) {
                        Log.e("FIREBASE_ERROR", "Error al parsear flete " + data.getKey(), e);
                    }
                }
                callback.onSuccess(fletes);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                callback.onError(error.getMessage());
            }
        };
        fletesRef.addValueEventListener(activeListener);
    }

    // Filtrar por estado
    public void filtrarPorEstado(String estado, FletesListCallback callback) {
        limpiarListeners();
        activeListener = fletesRef.orderByChild("estado")
                .equalTo(estado)
                .addValueEventListener(createListener(callback));
    }

    // Actualizar solo el estado
    public void actualizarEstado(String fleteId, String nuevoEstado, FleteCallback callback) {
        Map<String, Object> updates = new HashMap<>();
        updates.put("estado", nuevoEstado);
        updates.put("fechaActualizacion", DateUtils.getCurrentDateTime());

        actualizarCamposFlete(fleteId, updates, callback);
    }

    // Actualizar campos específicos
    public void actualizarCamposFlete(String fleteId, Map<String, Object> camposActualizados, FleteCallback callback) {
        if (fleteId == null || fleteId.isEmpty()) {
            callback.onError("ID de flete inválido");
            return;
        }

        camposActualizados.put("fechaActualizacion", DateUtils.getCurrentDateTime());

        fletesRef.child(fleteId).updateChildren(camposActualizados)
                .addOnSuccessListener(aVoid -> obtenerFlete(fleteId, callback))
                .addOnFailureListener(e -> callback.onError(e.getMessage()));
    }

    public void limpiarListeners() {
        if (activeListener != null) {
            fletesRef.removeEventListener(activeListener);
            activeListener = null;
        }
    }

    // Métodos auxiliares
    private Flete parseFleteFromSnapshot(DataSnapshot snapshot) {
        Flete flete = new Flete();
        flete.setId(snapshot.getKey());
        flete.setOrigen(snapshot.child("origen").getValue(String.class));
        flete.setDestino(snapshot.child("destino").getValue(String.class));
        flete.setEstado(snapshot.child("estado").getValue(String.class));
        flete.setDistancia(parseFirebaseDouble(snapshot, "distancia"));
        flete.setPeso(parseFirebaseDouble(snapshot, "peso"));
        flete.setTarifa(parseFirebaseDouble(snapshot, "tarifa"));
        flete.setTarifaTotal(parseFirebaseDouble(snapshot, "tarifaTotal"));
        flete.setUrgente(Boolean.TRUE.equals(snapshot.child("urgente").getValue(Boolean.class)));

        // Manejo de fechas (puedes adaptar según tu implementación)
        Object fechaSalida = snapshot.child("fechaSalida").getValue();
        if (fechaSalida instanceof Long) {

            flete.setFechaSalida((Long) fechaSalida);
        }

        return flete;
    }

    private double parseFirebaseDouble(DataSnapshot snapshot, String key) {
        Object value = snapshot.child(key).getValue();
        if (value == null) return 0.0;

        if (value instanceof Number) {
            return ((Number) value).doubleValue();
        } else if (value instanceof String) {
            try {
                return Double.parseDouble((String) value);
            } catch (NumberFormatException e) {
                Log.w("CONVERSION", "Valor no numérico para " + key + ": " + value);
                return 0.0;
            }
        }
        return 0.0;
    }

    private ValueEventListener createListener(FletesListCallback callback) {
        return new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Flete> fletes = new ArrayList<>();
                for (DataSnapshot data : snapshot.getChildren()) {
                    try {
                        Flete flete = parseFleteFromSnapshot(data);
                        fletes.add(flete);
                    } catch (Exception e) {
                        Log.e("FIREBASE_ERROR", "Error al parsear flete " + data.getKey(), e);
                    }
                }
                callback.onSuccess(fletes);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                callback.onError(error.getMessage());
            }
        };
    }
}