package py.edison.megasoftappv2.activities.Fletes;

import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import py.edison.megasoftappv2.R;
import py.edison.megasoftappv2.entidades.Flete;
import py.edison.megasoftappv2.servicios.FleteService;
import android.widget.Spinner;
import android.widget.ArrayAdapter;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.Timestamp;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class DetalleFleteActivity extends AppCompatActivity {
    private Flete flete;
    private FleteService fleteService = new FleteService();
    private boolean modoEdicion = false;

    // Referencias a vistas
    private EditText tvOrigen, tvDestino, etDistancia, etPeso, etTarifa, etEstado;
    private CheckBox cbUrgente;
    private Button btnEditar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_flete);

        String fleteId = getIntent().getStringExtra("FLETE_ID");
        modoEdicion = getIntent().getBooleanExtra("MODO_EDICION", false);

        configurarVistas();
        cargarFlete(fleteId);

        Spinner spinnerIncidencias = findViewById(R.id.spinnerIncidencias);
        Button btnEnviarIncidencia = findViewById(R.id.btnEnviarIncidencia);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        List<String> listaIncidencias = new ArrayList<>();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, listaIncidencias);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerIncidencias.setAdapter(adapter);

// Cargar tipos de incidencias creadas por el admin
        db.collection("tipos_incidencias").get().addOnSuccessListener(query -> {
            for (DocumentSnapshot doc : query) {
                listaIncidencias.add(doc.getString("nombre")); // Asegurate que se llame "nombre"
            }
            adapter.notifyDataSetChanged();
        });

// Guardar incidencia seleccionada
        btnEnviarIncidencia.setOnClickListener(v -> {
            String descripcion = spinnerIncidencias.getSelectedItem().toString();

            Map<String, Object> data = new HashMap<>();
            data.put("fleteId", flete.getId());
            data.put("descripcion", descripcion);
            data.put("fecha", new Timestamp(new Date()));
            data.put("gravedad", "Moderada");
            data.put("resuelta", false);

            db.collection("incidencias").add(data)
                    .addOnSuccessListener(doc -> Toast.makeText(this, "Incidencia enviada", Toast.LENGTH_SHORT).show())
                    .addOnFailureListener(e -> Toast.makeText(this, "Error al enviar", Toast.LENGTH_SHORT).show());
        });

    }

    private void configurarVistas() {
        // Inicializar vistas
      /*  tvOrigen = findViewById(R.id.tvOrigenDetalle);
        tvDestino = findViewById(R.id.tvDestinoDetalle);
        etDistancia = findViewById(R.id.etDistanciaDetalle);
        etPeso = findViewById(R.id.etPesoDetalle);
        etTarifa = findViewById(R.id.etTarifaDetalle);
        etEstado = findViewById(R.id.etEstadoDetalle);
        cbUrgente = findViewById(R.id.cbUrgenteDetalle);*/
        btnEditar = findViewById(R.id.btnEditar);

        btnEditar.setOnClickListener(v -> {
            if (modoEdicion) {
                guardarCambios();
            } else {
                habilitarEdicion();
            }
        });
    }

    private void habilitarEdicion() {
        modoEdicion = true;

        tvOrigen.setEnabled(true);
        tvDestino.setEnabled(true);
        etDistancia.setEnabled(true);
        etPeso.setEnabled(true);
        etTarifa.setEnabled(true);
        etEstado.setEnabled(true);
        cbUrgente.setEnabled(true);

        btnEditar.setText("Guardar");
    }

    private void deshabilitarEdicion() {
        tvOrigen.setEnabled(false);
        tvDestino.setEnabled(false);
        etDistancia.setEnabled(false);
        etPeso.setEnabled(false);
        etTarifa.setEnabled(false);
        etEstado.setEnabled(false);
        cbUrgente.setEnabled(false);
    }

    private void guardarCambios() {
        try {
            // Validar campos vacíos
            if (tvOrigen.getText().toString().isEmpty() ||
                    tvDestino.getText().toString().isEmpty() ||
                    etDistancia.getText().toString().isEmpty() ||
                    etPeso.getText().toString().isEmpty() ||
                    etTarifa.getText().toString().isEmpty() ||
                    etEstado.getText().toString().isEmpty()) {
                Toast.makeText(this, "Todos los campos son requeridos", Toast.LENGTH_SHORT).show();
                return;
            }

            // Guardar datos editados en el objeto flete
            flete.setOrigen(tvOrigen.getText().toString());
            flete.setDestino(tvDestino.getText().toString());
            flete.setDistancia(Double.parseDouble(etDistancia.getText().toString()));
            flete.setPeso(Double.parseDouble(etPeso.getText().toString()));
            flete.setTarifa(Double.parseDouble(etTarifa.getText().toString()));
            flete.setEstado(etEstado.getText().toString());
            flete.setUrgente(cbUrgente.isChecked());

            // Llamar al servicio para actualizar
            fleteService.actualizarFlete(flete, new FleteService.FleteCallback() {
                @Override
                public void onSuccess(Flete fleteActualizado) {
                    Toast.makeText(DetalleFleteActivity.this, "Flete actualizado", Toast.LENGTH_SHORT).show();
                    modoEdicion = false;
                    btnEditar.setText("Editar");
                    deshabilitarEdicion();
                    // Actualizar el objeto flete local con los datos actualizados
                    flete = fleteActualizado;
                }

                @Override
                public void onError(String error) {
                    Toast.makeText(DetalleFleteActivity.this, "Error: " + error, Toast.LENGTH_SHORT).show();
                }
            });
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Error en formato numérico", Toast.LENGTH_SHORT).show();
        }
    }
    private void cargarFlete(String id) {
        fleteService.obtenerFlete(id, new FleteService.FleteCallback() {
            @Override
            public void onSuccess(Flete fleteCargado) {
                flete = fleteCargado;
                mostrarDatos();
            }

            @Override
            public void onError(String error) {
                Toast.makeText(DetalleFleteActivity.this, error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void mostrarDatos() {
        tvOrigen.setText(flete.getOrigen());
        tvDestino.setText(flete.getDestino());
        etDistancia.setText(String.valueOf(flete.getDistancia()));
        etPeso.setText(String.valueOf(flete.getPeso()));
        etTarifa.setText(String.valueOf(flete.getTarifa()));
        etEstado.setText(flete.getEstado());
        cbUrgente.setChecked(flete.isUrgente());
    }
}
