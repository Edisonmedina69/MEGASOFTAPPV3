package py.edison.megasoftappv2.activities.Incidencias;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import py.edison.megasoftappv2.R;

public class AdminIncidenciasActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private EditText etNuevaIncidencia;
    private Button btnAgregar;
    private RecyclerView recyclerView;
    private List<String> listaIncidencias;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(py.edison.megasoftappv2.R.layout.activity_admin_incidencias);

        db = FirebaseFirestore.getInstance();

        etNuevaIncidencia = findViewById(R.id.etNuevaIncidencia);
        btnAgregar = findViewById(R.id.btnAgregarIncidencia);
        recyclerView = findViewById(R.id.recyclerIncidencias);

        listaIncidencias = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listaIncidencias);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new RecyclerViewAdapter(listaIncidencias));

        cargarIncidencias();

        btnAgregar.setOnClickListener(v -> {
            String nombre = etNuevaIncidencia.getText().toString().trim();
            if (!nombre.isEmpty()) {
                agregarIncidencia(nombre);
            }
        });
    }

    private void cargarIncidencias() {
        db.collection("tipos_incidencias")
                .get()
                .addOnSuccessListener(query -> {
                    listaIncidencias.clear();
                    for (DocumentSnapshot doc : query.getDocuments()) {
                        String nombre = doc.getString("nombre");
                        if (nombre != null) {
                            listaIncidencias.add(nombre);
                        }
                    }
                    recyclerView.setAdapter(new RecyclerViewAdapter(listaIncidencias));
                });
    }

    private void agregarIncidencia(String nombre) {
        Map<String, Object> data = new HashMap<>();
        data.put("nombre", nombre);

        db.collection("tipos_incidencias")
                .add(data)
                .addOnSuccessListener(doc -> {
                    Toast.makeText(this, "Incidencia agregada", Toast.LENGTH_SHORT).show();
                    etNuevaIncidencia.setText("");
                    cargarIncidencias();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Error al agregar", Toast.LENGTH_SHORT).show());
    }

    // Adaptador interno para RecyclerView
    class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
        private List<String> data;

        public RecyclerViewAdapter(List<String> data) {
            this.data = data;
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView textView;

            ViewHolder(View view) {
                super(view);
                textView = view.findViewById(android.R.id.text1);
            }
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(android.R.layout.simple_list_item_1, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.textView.setText(data.get(position));
        }

        @Override
        public int getItemCount() {
            return data.size();
        }
    }
}
