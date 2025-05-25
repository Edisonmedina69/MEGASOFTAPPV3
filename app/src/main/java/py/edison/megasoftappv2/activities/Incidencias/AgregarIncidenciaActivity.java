import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import py.edison.megasoftappv2.entidades.Incidencia;
import py.edison.megasoftappv2.R;

public class AgregarIncidenciaActivity extends AppCompatActivity {

    private Spinner spinnerIncidencias;
    private Button btnRegistrar;

    private ArrayList<Incidencia> listaIncidencias = new ArrayList<>();
    private ArrayAdapter<String> adapter;

    private DatabaseReference dbRef;
    private String fleteId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_incidencia);

        spinnerIncidencias = findViewById(R.id.spinnerIncidencias);
        btnRegistrar = findViewById(R.id.btnRegistrarIncidencia);

        fleteId = getIntent().getStringExtra("fleteId"); // <- Esto debe venir desde el intent

        dbRef = FirebaseDatabase.getInstance().getReference();

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, new ArrayList<>());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerIncidencias.setAdapter(adapter);

        cargarIncidenciasDesdeFirebase();

        btnRegistrar.setOnClickListener(v -> registrarIncidenciaSeleccionada());
    }

    private void cargarIncidenciasDesdeFirebase() {
        dbRef.child("incidencias").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot incSnap : snapshot.getChildren()) {
                    Incidencia inc = incSnap.getValue(Incidencia.class);
                    if (inc != null) {
                        inc.setId(incSnap.getKey());
                        listaIncidencias.add(inc);
                        adapter.add(inc.getDescripcion());
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AgregarIncidenciaActivity.this, "Error al cargar incidencias", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void registrarIncidenciaSeleccionada() {
        int pos = spinnerIncidencias.getSelectedItemPosition();
        if (pos < 0 || pos >= listaIncidencias.size()) return;

        Incidencia seleccionada = listaIncidencias.get(pos);
        String fechaHora = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(new Date());

        Map<String, Object> data = new HashMap<>();
        data.put("descripcion", seleccionada.getDescripcion());
        data.put("fechaHora", fechaHora);

        String nuevaId = dbRef.child("fletes").child(fleteId).child("incidencias").push().getKey();
        dbRef.child("fletes").child(fleteId).child("incidencias").child(nuevaId).setValue(data)
                .addOnSuccessListener(unused -> Toast.makeText(this, "Incidencia registrada", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(this, "Error al guardar", Toast.LENGTH_SHORT).show());
    }
}
