package py.edison.megasoftappv2.activities.Fletes;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import py.edison.megasoftappv2.R;
import py.edison.megasoftappv2.adapters.SpinnerPersonalizadoAdapter;
import py.edison.megasoftappv2.entidades.Flete;

public class CrearFletePaso2Activity extends AppCompatActivity {

    private Spinner spinnerClientes, spinnerConductores, spinnerMercancias, spinnerVehiculos;
    private Button btnAsignarFleteButton;
    private List<SpinnerItem> listaClientes = new ArrayList<>();
    private List<SpinnerItem> listaConductores = new ArrayList<>();
    private List<SpinnerItem> listaMercancias = new ArrayList<>();
    private List<SpinnerItem> listaVehiculos = new ArrayList<>();
    private DatabaseReference dbRef;

    private Flete fleteTemporal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_flete_paso2);

        // Recibir datos del Paso 1
        Intent intent = getIntent();
        fleteTemporal = new Flete();
        fleteTemporal.setOrigen(intent.getStringExtra("origen"));
        fleteTemporal.setDestino(intent.getStringExtra("destino"));
        fleteTemporal.setDistancia(intent.getDoubleExtra("distancia", 0));
        fleteTemporal.setPeso(intent.getDoubleExtra("peso", 0));
        fleteTemporal.setTarifa(intent.getDoubleExtra("tarifa", 0));
        fleteTemporal.setUrgente(intent.getBooleanExtra("urgente", false));

        // Inicializar Firebase
        dbRef = FirebaseDatabase.getInstance().getReference();

        // Asegurarse que el botón existe
        btnAsignarFleteButton = findViewById(R.id.btnAsignarFlete);
        if (btnAsignarFleteButton == null) {
            Toast.makeText(this, "Error: Botón no encontrado", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Inicializar Firebase
        dbRef = FirebaseDatabase.getInstance().getReference();

        // Inicializar vistas
        initViews();

        // Configurar adaptadores
        setupSpinnerAdapters();

        // Configurar listeners
        setupSpinnerListeners();
        setupButtonListener();

        // Cargar datos
        cargarDatosIniciales();
    }

    private void initViews() {
        spinnerClientes = findViewById(R.id.spinnerClientes);
        spinnerConductores = findViewById(R.id.spinnerConductores);
        spinnerMercancias = findViewById(R.id.spinnerMercancias);
        spinnerVehiculos = findViewById(R.id.spinnerVehiculos);
        btnAsignarFleteButton = findViewById(R.id.btnAsignarFlete);
    }

    private void setupSpinnerAdapters() {
        // Agregar items iniciales "Seleccione..."
        listaClientes.add(new SpinnerItem("", "Seleccione un cliente...", 0));
        listaConductores.add(new SpinnerItem("", "Seleccione un conductor...", 0));
        listaMercancias.add(new SpinnerItem("", "Seleccione un tipo...", 0));
        listaVehiculos.add(new SpinnerItem("", "Seleccione un vehículo...", 0));

        spinnerClientes.setAdapter(new SpinnerPersonalizadoAdapter(this, listaClientes));
        spinnerConductores.setAdapter(new SpinnerPersonalizadoAdapter(this, listaConductores));
        spinnerMercancias.setAdapter(new SpinnerPersonalizadoAdapter(this, listaMercancias));
        spinnerVehiculos.setAdapter(new SpinnerPersonalizadoAdapter(this, listaVehiculos));
    }

    private void cargarDatosIniciales() {
        cargarClientes();
        cargarConductores();
        cargarMercancias();
        cargarVehiculos();
    }

    private void cargarClientes() {
        dbRef.child("clientes").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listaClientes.clear();
                listaClientes.add(new SpinnerItem("", "Seleccione un cliente...", 0));

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String id = snapshot.getKey();
                    String nombre = snapshot.child("nombre").getValue(String.class);
                    listaClientes.add(new SpinnerItem(id, nombre, R.drawable.ic_cliente));
                }
                ((SpinnerPersonalizadoAdapter) spinnerClientes.getAdapter()).notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                showError("Error al cargar clientes", databaseError);
            }
        });
    }

    private void cargarConductores() {
        dbRef.child("conductores").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listaConductores.clear();
                listaConductores.add(new SpinnerItem("", "Seleccione un conductor...", 0));

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String id = snapshot.getKey();
                    String nombre = snapshot.child("nombre").getValue(String.class);
                    String licencia = snapshot.child("licencia").getValue(String.class);
                    listaConductores.add(new SpinnerItem(id, nombre + " - " + licencia, R.drawable.ic_conductor));
                }
                ((SpinnerPersonalizadoAdapter) spinnerConductores.getAdapter()).notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                showError("Error al cargar conductores", databaseError);
            }
        });
    }

    private void cargarMercancias() {
        dbRef.child("tiposMercancia").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listaMercancias.clear();
                listaMercancias.add(new SpinnerItem("", "Seleccione un tipo...", 0));

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String id = snapshot.getKey();
                    String descripcion = snapshot.child("descripcion").getValue(String.class);
                    listaMercancias.add(new SpinnerItem(id, descripcion, R.drawable.ic_mercancia));
                }
                ((SpinnerPersonalizadoAdapter) spinnerMercancias.getAdapter()).notifyDataSetChanged();

                // Verifica que los datos se cargan
                Log.d("Mercancias", "Items cargados: " + listaMercancias.size());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                showError("Error al cargar tipos de mercancía", databaseError);
            }
        });
    }

    private void cargarVehiculos() {
        dbRef.child("vehiculos").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listaVehiculos.clear();
                listaVehiculos.add(new SpinnerItem("", "Seleccione un vehículo...", 0));

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String id = snapshot.getKey();
                    String modelo = snapshot.child("modelo").getValue(String.class);
                    String placa = snapshot.child("placa").getValue(String.class);
                    String tipo = snapshot.child("tipo").getValue(String.class);
                    listaVehiculos.add(new SpinnerItem(id, modelo + " - " + placa + " (" + tipo + ")", R.drawable.ic_vehiculo));
                }
                ((SpinnerPersonalizadoAdapter) spinnerVehiculos.getAdapter()).notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                showError("Error al cargar vehículos", databaseError);
            }
        });
    }

    private void showError(String message, DatabaseError error) {
        Toast.makeText(this, message + ": " + error.getMessage(), Toast.LENGTH_SHORT).show();
    }

    private void setupSpinnerListeners() {
        // Listeners para actualizar selecciones
        spinnerClientes.setOnItemSelectedListener(createSpinnerListener("Cliente"));
        spinnerConductores.setOnItemSelectedListener(createSpinnerListener("Conductor"));
        spinnerMercancias.setOnItemSelectedListener(createSpinnerListener("Tipo de mercancía"));
        spinnerVehiculos.setOnItemSelectedListener(createSpinnerListener("Vehículo"));
    }

    private AdapterView.OnItemSelectedListener createSpinnerListener(String tipo) {
        return new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    SpinnerItem item = (SpinnerItem) parent.getItemAtPosition(position);
                    showSelection(tipo, item.getText());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        };
    }

    private void showSelection(String tipo, String value) {
        Toast.makeText(this, tipo + " seleccionado: " + value, Toast.LENGTH_SHORT).show();
    }

    private void setupButtonListener() {
        btnAsignarFleteButton.setOnClickListener(v -> {
            if (validarSelecciones()) {
                pasarASiguientePaso();
            }
        });
    }

    private boolean validarSelecciones() {
        if (!validarSpinner(spinnerClientes, "Seleccione un cliente")) return false;
        if (!validarSpinner(spinnerConductores, "Seleccione un conductor")) return false;
        if (!validarSpinner(spinnerMercancias, "Seleccione un tipo de mercancía")) return false;
        if (!validarSpinner(spinnerVehiculos, "Seleccione un vehículo")) return false;
        return true;
    }

    private boolean validarSpinner(Spinner spinner, String mensajeError) {
        if (spinner.getSelectedItemPosition() <= 0) {
            Toast.makeText(this, mensajeError, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void pasarASiguientePaso() {
        // Obtener selecciones
        String clienteId = obtenerIdSeleccionado(spinnerClientes);
        String conductorId = obtenerIdSeleccionado(spinnerConductores);
        String mercanciaId = obtenerIdSeleccionado(spinnerMercancias);
        String vehiculoId = obtenerIdSeleccionado(spinnerVehiculos);

        // Completar el flete con los datos seleccionados
        fleteTemporal.setClienteId(clienteId);
        fleteTemporal.setConductorId(conductorId);
        fleteTemporal.setTipoMercanciaId(mercanciaId);
        fleteTemporal.setVehiculoId(vehiculoId);

        // Guardar en Firebase y pasar a la siguiente actividad
        String fleteId = dbRef.child("fletes").push().getKey();
        if (fleteId != null) {
            fleteTemporal.setId(fleteId); // Asignar el ID generado

            // Primero guardamos en Firebase
            dbRef.child("fletes").child(fleteId).setValue(fleteTemporal)
                    .addOnSuccessListener(aVoid -> {
                        // Luego pasamos a la siguiente actividad con el objeto completo
                        Intent intent = new Intent(CrearFletePaso2Activity.this, CrearFletePaso3Activity.class);
                        intent.putExtra("flete", fleteTemporal); // Pasar el objeto completo

                        Toast.makeText(CrearFletePaso2Activity.this, "Flete creado exitosamente", Toast.LENGTH_SHORT).show();
                        startActivity(intent);
                        finish();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(CrearFletePaso2Activity.this,
                                "Error al guardar flete: " + e.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    });
        }
    }


    private String obtenerIdSeleccionado(Spinner spinner) {
        SpinnerItem item = (SpinnerItem) spinner.getSelectedItem();
        return item != null ? item.getId() : null;
    }

    public static class SpinnerItem {
        private final String id;
        private final String text;
        private final int iconRes;

        public SpinnerItem(String id, String text, int iconRes) {
            this.id = id;
            this.text = text;
            this.iconRes = iconRes;
        }

        public String getId() { return id; }
        public String getText() { return text; }
        public int getIconRes() { return iconRes; }

        @Override
        public String toString() {
            return text;
        }
    }
}