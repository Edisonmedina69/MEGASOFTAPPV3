package py.edison.megasoftappv2.activities.Incidencias;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import py.edison.megasoftappv2.R;
import py.edison.megasoftappv2.entidades.Incidencias;

public class IncidenciaEstadoActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private String fleteId = "ID_DEL_FLETE"; // Pasar este ID real por intent o lógica de negocio

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incidencia_estado);

        db = FirebaseFirestore.getInstance();

        Button btnSalida = findViewById(R.id.btnSalida);
        Button btnLlegada = findViewById(R.id.btnLlegada);
        Button btnParada = findViewById(R.id.btnParada);

        btnSalida.setOnClickListener(v -> guardarIncidencia("Salida"));
        btnLlegada.setOnClickListener(v -> guardarIncidencia("Llegada"));
        btnParada.setOnClickListener(v -> guardarIncidencia("Parada para cargar combustible"));
    }

    private void guardarIncidencia(String descripcion) {
        Incidencias incidencia = new Incidencias(
                fleteId,
                descripcion,
                new Date(),
                "Leve",
                false
        );

        db.collection("incidencias")
                .add(incidencia)
                .addOnSuccessListener(documentReference -> {
                    Log.d("FIREBASE", "Incidencia guardada");
                    enviarNotificacion(descripcion); // 🔔 Envía notificación
                })
                .addOnFailureListener(e -> {
                    Log.e("FIREBASE", "Error al guardar", e);
                });
    }

    private void enviarNotificacion(String descripcionSeleccionada) {
        String tokenDestino = "TOKEN_DEL_CLIENTE_O_ADMIN"; // 🔁 reemplazá esto

        JSONObject body = new JSONObject();
        try {
            JSONObject notification = new JSONObject();
            notification.put("title", "Estado del flete");
            notification.put("body", descripcionSeleccionada);

            body.put("to", tokenDestino);
            body.put("notification", notification);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://fcm.googleapis.com/fcm/send";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, body,
                response -> Log.d("FCM", "Notificación enviada"),
                error -> Log.e("FCM", "Error al enviar", error)
        ) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "key=AAA_TU_CLAVE_FCM_DEL_SERVIDOR"); // 🔁 reemplazá
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };

        queue.add(request);
    }
}
