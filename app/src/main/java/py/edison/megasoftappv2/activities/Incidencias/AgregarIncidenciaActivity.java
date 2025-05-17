

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;

import py.edison.megasoftappv2.R;
import py.edison.megasoftappv2.entidades.Incidencias;

public class AgregarIncidenciaActivity extends AppCompatActivity {

    private EditText editDescripcion;
    private Spinner spinnerGravedad;
    private Switch switchResuelta;
    private Button btnGuardar;

    private FirebaseFirestore db;

    // Podrías recibir este ID desde otra Activity
    private String fleteId = "FLETE123"; // <- Este lo pasás como parámetro real

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_incidencia);

        db = FirebaseFirestore.getInstance();

        editDescripcion = findViewById(R.id.editDescripcion);
        spinnerGravedad = findViewById(R.id.spinnerGravedad);
        switchResuelta = findViewById(R.id.switchResuelta);
        btnGuardar = findViewById(R.id.btnGuardarIncidencia);

        btnGuardar.setOnClickListener(view -> guardarIncidencia());
    }

    private void guardarIncidencia() {
        String descripcion = editDescripcion.getText().toString().trim();
        String gravedad = spinnerGravedad.getSelectedItem().toString();
        boolean resuelta = switchResuelta.isChecked();

        if (descripcion.isEmpty()) {
            Toast.makeText(this, "La descripción no puede estar vacía", Toast.LENGTH_SHORT).show();
            return;
        }

        Incidencias incidencia = new Incidencias(
                fleteId,
                descripcion,
                Timestamp.now(),
                gravedad,
                resuelta
        );

        db.collection("incidencias")
                .add(incidencia)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(this, "Incidencia guardada", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error al guardar", Toast.LENGTH_SHORT).show();
                    Log.e("FIREBASE", "Error al guardar incidencia", e);
                });
    }
}
