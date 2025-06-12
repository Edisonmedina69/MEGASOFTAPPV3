package py.edison.megasoftappv2.activities.Fletes;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.function.Consumer;

import py.edison.megasoftappv2.R;
import py.edison.megasoftappv2.entidades.Flete;

public class CrearFletePaso3Activity extends AppCompatActivity {

    private TextView txtResumen, txtTarifa;
    private Button btnConfirmar;
    private Flete flete;
    private DatabaseReference dbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_flete_paso3);

        txtResumen = findViewById(R.id.txtResumen);
        txtTarifa = findViewById(R.id.txtTarifa);
        btnConfirmar = findViewById(R.id.btnConfirmar);
        dbRef = FirebaseDatabase.getInstance().getReference();

        if (getIntent() != null && getIntent().hasExtra("flete")) {
            flete = getIntent().getParcelableExtra("flete");
            Log.d("DATOS_FLETE", "Origen: " + flete.getOrigen());
            Log.d("DATOS_FLETE", "Distancia: " + flete.getDistancia());
            Log.d("DATOS_FLETE", "MercanciaID: " + flete.getTipoMercanciaId());

            // DEBUG: Verificar datos recibidos
            Log.d("FLETE_DEBUG", "Datos recibidos: " +
                    "\nOrigen: " + flete.getOrigen() +
                    "\nDestino: " + flete.getDestino() +
                    "\nDistancia: " + flete.getDistancia() +
                    "\nPeso: " + flete.getPeso() +
                    "\nUrgente: " + flete.isUrgente() +
                    "\nClienteId: " + flete.getClienteId() +
                    "\nMercanciaId: " + flete.getTipoMercanciaId());

            mostrarResumen();
        } else {
            Toast.makeText(this, "Error: No se recibieron datos del flete", Toast.LENGTH_LONG).show();
            finish();
        }

        btnConfirmar.setOnClickListener(v -> guardarFleteEnFirebase());
    }

    private void mostrarResumen() {
        if (flete == null) {
            Toast.makeText(this, "Error: No se recibieron datos del flete", Toast.LENGTH_LONG).show();
            Log.e("FLETE_ERROR", "El objeto flete es null");
            finish();
            return;
        }

        try {
            // Mostrar datos básicos primero
            StringBuilder resumen = new StringBuilder("✅ Resumen del Flete\n\n");

            resumen.append("📍 Origen: ").append(TextUtils.isEmpty(flete.getOrigen()) ? "No especificado" : flete.getOrigen()).append("\n")
                    .append("🏁 Destino: ").append(TextUtils.isEmpty(flete.getDestino()) ? "No especificado" : flete.getDestino()).append("\n")
                    .append("📏 Distancia: ").append(flete.getDistancia() > 0 ? flete.getDistancia() + " km" : "No especificada").append("\n")
                    .append("⚖ Peso: ").append(flete.getPeso() > 0 ? flete.getPeso() + " kg" : "No especificado").append("\n")
                    .append("🚨 Urgente: ").append(flete.isUrgente() ? "Sí" : "No").append("\n\n");

            txtResumen.setText(resumen.toString());

            // Calcular y mostrar tarifa
            if (flete.getDistancia() > 0) {
                double tarifa = calcularTarifa(flete.getDistancia());
                String tarifaFormateada = String.format("Tarifa a cobrar: Gs. %,.2f", tarifa);
                txtTarifa.setText(tarifaFormateada);
            } else {
                txtTarifa.setText("Distancia no especificada - No se puede calcular tarifa");
            }

            // Obtener datos asíncronos
            obtenerDatosAdicionales(resumen);

        } catch (Exception e) {
            Log.e("ERROR_RESUMEN", "Error al mostrar resumen", e);
            Toast.makeText(this, "Error al mostrar resumen del flete", Toast.LENGTH_LONG).show();
        }
    }

    private void obtenerDatosAdicionales(StringBuilder resumenBase) {
        obtenerNombreCliente(flete.getClienteId(), nombreCliente -> {
            resumenBase.append("👤 Cliente: ").append(nombreCliente).append("\n");
            txtResumen.setText(resumenBase.toString());

            obtenerNombreConductor(flete.getConductorId(), nombreConductor -> {
                resumenBase.append("🚗 Conductor: ").append(nombreConductor).append("\n");
                txtResumen.setText(resumenBase.toString());

                obtenerTipoMercancia(flete.getTipoMercanciaId(), nombreMercancia -> {
                    resumenBase.append("📦 Mercancía: ").append(nombreMercancia).append("\n");
                    txtResumen.setText(resumenBase.toString());

                    obtenerNombreVehiculo(flete.getVehiculoId(), nombreVehiculo -> {
                        resumenBase.append("🚚 Vehículo: ").append(nombreVehiculo);
                        txtResumen.setText(resumenBase.toString());
                    });
                });
            });
        });
    }

    private void obtenerNombreCliente(String id, Consumer<String> callback) {
        if (id == null || id.isEmpty()) {
            callback.accept("Cliente no especificado");
            return;
        }

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("clientes").child(id);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String nombre = dataSnapshot.child("nombre").getValue(String.class);
                callback.accept(nombre != null ? nombre : "Cliente no encontrado");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                callback.accept("Error al cargar cliente");
            }
        });
    }

    private void obtenerNombreConductor(String id, Consumer<String> callback) {
        if (id == null || id.isEmpty()) {
            callback.accept("Conductor no especificado");
            return;
        }

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("conductores").child(id);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String nombre = dataSnapshot.child("nombre").getValue(String.class);
                String vehiculo = dataSnapshot.child("vehiculo").getValue(String.class);
                String nombreCompleto = nombre + (vehiculo != null ? " (" + vehiculo + ")" : "");
                callback.accept(nombreCompleto != null ? nombreCompleto : "Conductor no encontrado");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                callback.accept("Error al cargar conductor");
            }
        });
    }

    private void obtenerTipoMercancia(String id, Consumer<String> callback) {
        if (TextUtils.isEmpty(id)) {
            callback.accept("No especificado");
            return;
        }

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("tiposMercancia").child(id);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String nombre = dataSnapshot.child("nombre").getValue(String.class);
                    callback.accept(nombre != null ? nombre : "No especificado");
                } else {
                    callback.accept("No encontrado");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                callback.accept("Error al cargar");
            }
        });
    }

    private void obtenerNombreVehiculo(String id, Consumer<String> callback) {
        if (id == null || id.isEmpty()) {
            callback.accept("Vehículo no especificado");
            return;
        }

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("vehiculos").child(id);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String modelo = dataSnapshot.child("modelo").getValue(String.class);
                String placa = dataSnapshot.child("placa").getValue(String.class);
                String nombreCompleto = (modelo != null ? modelo : "") + (placa != null ? " (" + placa + ")" : "");
                callback.accept(!nombreCompleto.isEmpty() ? nombreCompleto : "Vehículo no encontrado");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                callback.accept("Error al cargar vehículo");
            }
        });
    }
    private double calcularTarifa(double distancia) {
        if (distancia <= 0) {
            Log.w("TARIFA_CALCULO", "Distancia inválida: " + distancia);
            return 0;
        }

        double tarifaBase = 1500; // Gs. por km
        double factorUrgencia = flete.isUrgente() ? 1.3 : 1.0;
        double factorPeso = 1.0;

        if (flete.getPeso() > 1000) {
            double exceso = flete.getPeso() - 1000;
            factorPeso = 1 + (0.05 * Math.ceil(exceso / 500));
        }

        return distancia * tarifaBase * factorUrgencia * factorPeso;
    }

    private void guardarFleteEnFirebase() {
        if (flete == null) {
            Toast.makeText(this, "Error: No hay datos de flete para guardar", Toast.LENGTH_SHORT).show();
            return;
        }

        String fleteId = dbRef.child("fletes").push().getKey();
        if (fleteId != null) {
            dbRef.child("fletes").child(fleteId).setValue(flete)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(this, "Flete guardado", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(this, GestionFletesActivity.class));
                        finish();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        } else {
            Toast.makeText(this, "Error al generar ID para el flete", Toast.LENGTH_SHORT).show();
        }
    }
}