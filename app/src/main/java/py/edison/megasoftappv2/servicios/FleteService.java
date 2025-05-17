package py.edison.megasoftappv2.servicios;

import androidx.annotation.NonNull;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import py.edison.megasoftappv2.entidades.ConfiguracionTarifa;
import py.edison.megasoftappv2.entidades.Flete;
import py.edison.megasoftappv2.utils.DateUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
public class FleteService {
    private final DatabaseReference fletesRef;
    private ValueEventListener activeListener;

    public FleteService() {
        this.fletesRef = FirebaseDatabase.getInstance().getReference("fletes");
    }

    // Actualizar flete completo
    public void actualizarFlete(Flete flete, FleteCallback callback) {
        if (flete.getId() == null || flete.getId().isEmpty()) {
            callback.onError("El flete no tiene un ID válido");
            return;
        }

        // Crear mapa con todos los campos a actualizar
        Map<String, Object> updates = new HashMap<>();
        updates.put("origen", flete.getOrigen());
        updates.put("destino", flete.getDestino());
        updates.put("distancia", flete.getDistancia());
        updates.put("peso", flete.getPeso());
        updates.put("tarifa", flete.getTarifa());
        updates.put("estado", flete.getEstado());
        updates.put("urgente", flete.isUrgente());
        // Agrega aquí otros campos que necesites actualizar

        fletesRef.child(flete.getId()).updateChildren(updates)
                .addOnSuccessListener(aVoid -> {
                    // Después de actualizar, obtenemos el flete actualizado para devolverlo
                    obtenerFlete(flete.getId(), callback);
                })
                .addOnFailureListener(e -> callback.onError(e.getMessage()));
    }

    public double calcularPrecioFlete(int i, int i1, ConfiguracionTarifa tarifa, boolean b) {
        return 0;
    }

    public interface FleteCallback {
        void onSuccess(Flete flete);
        void onError(String error);
    }

    public interface FletesListCallback {
        void onSuccess(List<Flete> fletes);
        void onError(String error);
    }

    // Crear nuevo flete
    public void crearFlete(Flete flete, FleteCallback callback) {
        String key = fletesRef.push().getKey();
        flete.setId(key);
        fletesRef.child(key).setValue(flete)
                .addOnSuccessListener(aVoid -> callback.onSuccess(flete))
                .addOnFailureListener(e -> callback.onError(e.getMessage()));
    }

    // Obtener flete por ID
    public void obtenerFlete(String id, FleteCallback callback) {
        fletesRef.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Flete flete = snapshot.getValue(Flete.class);
                if (flete != null) {
                    flete.setId(snapshot.getKey());
                    callback.onSuccess(flete);
                } else {
                    callback.onError("Flete no encontrado");
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                callback.onError(error.getMessage());
            }
        });
    }

    // Actualizar campos específicos de un flete
    public void actualizarCamposFlete(String fleteId, Map<String, Object> camposActualizados, FleteCallback callback) {
        if (fleteId == null || fleteId.isEmpty()) {
            callback.onError("ID de flete inválido");
            return;
        }

        // Agregar fecha de actualización
        camposActualizados.put("fechaActualizacion", DateUtils.getCurrentDateTime());

        fletesRef.child(fleteId).updateChildren(camposActualizados)
                .addOnSuccessListener(aVoid -> obtenerFlete(fleteId, callback))
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
                    Flete flete = data.getValue(Flete.class);
                    if (flete != null) {
                        flete.setId(data.getKey());
                        fletes.add(flete);
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

    // Actualizar estado
    public void actualizarEstado(String fleteId, String nuevoEstado, FleteCallback callback) {
        Map<String, Object> updates = new HashMap<>();
        updates.put("estado", nuevoEstado);
        updates.put("fechaActualizacion", DateUtils.getCurrentDateTime());

        fletesRef.child(fleteId).updateChildren(updates)
                .addOnSuccessListener(aVoid -> obtenerFlete(fleteId, callback))
                .addOnFailureListener(e -> callback.onError(e.getMessage()));
    }

    public void cambiarEstadoFlete(Flete flete, String nuevoEstado) {
        flete.setEstado(nuevoEstado);
        // actualizar en Firebase o donde guardes
    }

    private ValueEventListener createListener(FletesListCallback callback) {
        return new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Flete> fletes = new ArrayList<>();
                for (DataSnapshot data : snapshot.getChildren()) {
                    Flete flete = data.getValue(Flete.class);
                    if (flete != null) {
                        flete.setId(data.getKey());
                        fletes.add(flete);
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

    public void limpiarListeners() {
        if (activeListener != null) {
            fletesRef.removeEventListener(activeListener);
        }
    }
}