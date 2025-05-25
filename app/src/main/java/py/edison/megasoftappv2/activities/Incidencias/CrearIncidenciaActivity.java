package py.edison.megasoftappv2.activities.Incidencias;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

import py.edison.megasoftappv2.R;

public class CrearIncidenciaActivity extends AppCompatActivity {

    private EditText editDescripcion, editTexto;
    private Button btnGuardar;

    private DatabaseReference dbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_incidencia);

        editDescripcion = findViewById(R.id.editDescripcion);
        editTexto = findViewById(R.id.editTexto);
        btnGuardar = findViewById(R.id.btnGuardarIncidencia);

        dbRef = FirebaseDatabase.getInstance().getReference("incidencias");

        btnGuardar.setOnClickListener(v -> guardarIncidencia());
    }

    private void guardarIncidencia() {
        String descripcion = editDescripcion.getText().toString().trim();
        String texto = editTexto.getText().toString().trim();

        if (descripcion.isEmpty() || texto.isEmpty()) {
            Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        String id = dbRef.push().getKey();
        Map<String, Object> data = new HashMap<>();
        data.put("descripcion", descripcion);
        data.put("texto", texto);
        data.put("estado", "activo");

        dbRef.child(id).setValue(data)
                .addOnSuccessListener(unused -> {
                    Toast.makeText(this, "Incidencia guardada", Toast.LENGTH_SHORT).show();
                    editDescripcion.setText("");
                    editTexto.setText("");
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error al guardar", Toast.LENGTH_SHORT).show();
                });
    }
}
