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

public class CrearFleteActivity extends AppCompatActivity {

    private EditText etOrigen, etDestino, etDistancia, etPeso, etTarifa;
    private CheckBox cbUrgente;
    private FleteService fleteService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_flete);

        fleteService = new FleteService();
        inicializarVistas();
    }

    private void inicializarVistas() {
        // Inicializar todos los campos
        etOrigen = findViewById(R.id.etOrigen);
        etDestino = findViewById(R.id.etDestino);
        etDistancia = findViewById(R.id.etDistancia);
        etPeso = findViewById(R.id.etPeso);
        etTarifa = findViewById(R.id.etTarifa);
        cbUrgente = findViewById(R.id.cbUrgente);

        Button btnGuardar = findViewById(R.id.btnGuardarFlete);
        btnGuardar.setOnClickListener(v -> guardarFlete());
    }

    private void guardarFlete() {
        // Validar campos
        if (!validarCampos()) {
            return;
        }

        try {
            // Crear nuevo flete con todos los datos
            Flete nuevoFlete = crearNuevoFlete();

            // Guardar en Firebase
            fleteService.crearFlete(nuevoFlete, new FleteService.FleteCallback() {
                @Override
                public void onSuccess(Flete flete) {
                    mostrarMensajeExito();
                    finish();
                }

                @Override
                public void onError(String error) {
                    mostrarError(error);
                }
            });

        } catch (NumberFormatException e) {
            Toast.makeText(this, "Error en formato numérico", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean validarCampos() {
        if (etOrigen.getText().toString().trim().isEmpty()) {
            etOrigen.setError("Ingrese el origen");
            return false;
        }
        if (etDestino.getText().toString().trim().isEmpty()) {
            etDestino.setError("Ingrese el destino");
            return false;
        }
        if (etDistancia.getText().toString().trim().isEmpty()) {
            etDistancia.setError("Ingrese la distancia");
            return false;
        }
        if (etPeso.getText().toString().trim().isEmpty()) {
            etPeso.setError("Ingrese el peso");
            return false;
        }
        if (etTarifa.getText().toString().trim().isEmpty()) {
            etTarifa.setError("Ingrese la tarifa");
            return false;
        }
        return true;
    }

    private Flete crearNuevoFlete() {
        Flete flete = new Flete();
        flete.setOrigen(etOrigen.getText().toString().trim());
        flete.setDestino(etDestino.getText().toString().trim());
        flete.setDistancia(Double.parseDouble(etDistancia.getText().toString()));
        flete.setPeso(Double.parseDouble(etPeso.getText().toString()));
        flete.setTarifa(Double.parseDouble(etTarifa.getText().toString()));
        flete.setUrgente(cbUrgente.isChecked());
        flete.setEstado(Flete.PENDIENTE);
        return flete;
    }

    private void mostrarMensajeExito() {
        Toast.makeText(this, "Flete creado exitosamente", Toast.LENGTH_SHORT).show();
        setResult(RESULT_OK);
    }

    private void mostrarError(String error) {
        Toast.makeText(this, "Error al crear flete: " + error, Toast.LENGTH_SHORT).show();
    }
}