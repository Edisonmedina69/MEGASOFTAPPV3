package py.edison.megasoftappv2.servicios;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import py.edison.megasoftappv2.entidades.Incidencia;
import py.edison.megasoftappv2.interfaces.IncidenciaCallback;

public class IncidenciaService {
    private DatabaseReference dbRef;

    public IncidenciaService() {
        dbRef = FirebaseDatabase.getInstance().getReference("incidencias");
    }

    public void reportarIncidencia(Incidencia incidencia, IncidenciaCallback callback) {
        String id = dbRef.push().getKey();
        incidencia.setId(id);
        dbRef.child(id).setValue(incidencia)
                .addOnSuccessListener(aVoid -> callback.onSuccess(incidencia))
                .addOnFailureListener(e -> callback.onError(e.getMessage()));
    }
}