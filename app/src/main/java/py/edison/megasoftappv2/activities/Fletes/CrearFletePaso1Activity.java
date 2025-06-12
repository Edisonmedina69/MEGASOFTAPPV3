package py.edison.megasoftappv2.activities.Fletes;



import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.firebase.Timestamp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import py.edison.megasoftappv2.R;
import py.edison.megasoftappv2.entidades.Flete;

public class CrearFletePaso1Activity extends AppCompatActivity {
    private EditText etOrigen, etDestino, etDistancia, etPeso, etTarifa;
    private CheckBox cbUrgente;
    private Button btnSiguienteP; // Cambiado a btnSiguienteP para coincidir con tu XML
    private DatabaseReference fletesRef;
    private Flete flete;

    private CheckBox cbProgramarFecha;
    private EditText etFechaProgramada;
    private Long fechaSeleccionada = System.currentTimeMillis(); // Fecha actual por defecto

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_flete_paso1);
// Inicializar el objeto Flete aquí
        flete = new Flete();
        // Vincular componentes
        etOrigen = findViewById(R.id.etOrigen);
        etDestino = findViewById(R.id.etDestino);
        etDistancia = findViewById(R.id.etDistancia);
        etPeso = findViewById(R.id.etPeso);
        etTarifa = findViewById(R.id.etTarifa);
        cbUrgente = findViewById(R.id.cbUrgente);
        btnSiguienteP = findViewById(R.id.btnsiguientePas);
        if (btnSiguienteP == null) {
            Toast.makeText(this, "Error: Botón no encontrado", Toast.LENGTH_LONG).show();
            finish();
            return;
        }


        // Configurar listeners
        btnSiguienteP.setOnClickListener(v -> {
            if (validarCamposCompletos()) {
                guardarYAvanzar();
            }
        });
        // Referencia a Firebase
        fletesRef = FirebaseDatabase.getInstance().getReference("fletes");

        // Configurar click listener CORRECTAMENTE
        btnSiguienteP.setOnClickListener(v -> guardarYAvanzar());


        cbProgramarFecha = findViewById(R.id.cbProgramarFecha);
        etFechaProgramada = findViewById(R.id.etFechaProgramada);

        // Mostrar/ocultar EditText al marcar el CheckBox
        cbProgramarFecha.setOnCheckedChangeListener((buttonView, isChecked) -> {
            etFechaProgramada.setVisibility(isChecked ? View.VISIBLE : View.GONE);
            if (!isChecked) fechaSeleccionada = System.currentTimeMillis(); // Reset a fecha actual
        });

        // Selector de fecha al tocar EditText
        etFechaProgramada.setOnClickListener(v -> mostrarDatePicker());
    }
    private boolean validarCamposCompletos() {
        boolean valido = true;

        if (etOrigen.getText().toString().trim().isEmpty()) {
            etOrigen.setError("Ingrese el origen");
            valido = false;
        }

        if (etDestino.getText().toString().trim().isEmpty()) {
            etDestino.setError("Ingrese el destino");
            valido = false;
        }

        try {
            Double.parseDouble(etDistancia.getText().toString());
        } catch (NumberFormatException e) {
            etDistancia.setError("Distancia inválida");
            valido = false;
        }

        try {
            Double.parseDouble(etPeso.getText().toString());
        } catch (NumberFormatException e) {
            etPeso.setError("Peso inválido");
            valido = false;
        }

        try {
            Double.parseDouble(etTarifa.getText().toString());
        } catch (NumberFormatException e) {
            etTarifa.setError("Tarifa inválida");
            valido = false;
        }

        return valido;
    }
    private void mostrarDatePicker() {
        MaterialDatePicker<Long> datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Seleccionar fecha")
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds()) // Fecha actual como predeterminada
                .build();

        datePicker.addOnPositiveButtonClickListener(selection -> {
            fechaSeleccionada = selection;
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            etFechaProgramada.setText(sdf.format(new Date(selection)));
        });

        datePicker.show(getSupportFragmentManager(), "DATE_PICKER");
    }


    private void guardarYAvanzar() {
        try {
            // Configurar todos los datos en el objeto flete
            flete.setOrigen(etOrigen.getText().toString().trim());
            flete.setDestino(etDestino.getText().toString().trim());
            flete.setDistancia(Double.parseDouble(etDistancia.getText().toString()));
            flete.setPeso(Double.parseDouble(etPeso.getText().toString()));
            flete.setTarifa(Double.parseDouble(etTarifa.getText().toString()));
            flete.setUrgente(cbUrgente.isChecked());
            flete.setFechaSalida(fechaSeleccionada);


            // Pasar a la siguiente actividad
            Intent intent = new Intent(this, CrearFletePaso2Activity.class);
            intent.putExtra("flete", flete);
            startActivity(intent);

        } catch (NumberFormatException e) {
            Toast.makeText(this, "Distancia, peso y tarifa deben ser números válidos", Toast.LENGTH_SHORT).show();
        }
    }
}