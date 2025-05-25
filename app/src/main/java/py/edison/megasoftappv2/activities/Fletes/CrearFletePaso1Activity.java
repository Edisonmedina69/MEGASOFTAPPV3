package py.edison.megasoftappv2.activities.Fletes;



import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.Timestamp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

import py.edison.megasoftappv2.R;
import py.edison.megasoftappv2.entidades.Flete;

public class CrearFletePaso1Activity extends AppCompatActivity {
    private EditText etOrigen, etDestino, etDistancia, etPeso, etTarifa;
    private CheckBox cbUrgente;
    private Button btnSiguienteP; // Cambiado a btnSiguienteP para coincidir con tu XML
    private DatabaseReference fletesRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_flete_paso1);

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
            Log.e("CrearFletePaso2", "Botón btnSiguiente no existe en el layout");
            finish();
            return;
        }
        // Referencia a Firebase
        fletesRef = FirebaseDatabase.getInstance().getReference("fletes");

        // Configurar click listener CORRECTAMENTE
        btnSiguienteP.setOnClickListener(v -> guardarYAvanzar());
    }
    private void guardarYAvanzar() {
        try {
            String origen = etOrigen.getText().toString().trim();
            String destino = etDestino.getText().toString().trim();
            double distanciaVal = Double.parseDouble(etDistancia.getText().toString());
            double pesoVal = Double.parseDouble(etPeso.getText().toString());
            double tarifaVal = Double.parseDouble(etTarifa.getText().toString());
            boolean urgente = cbUrgente.isChecked();

            if (origen.isEmpty() || destino.isEmpty()) {
                Toast.makeText(this, "Origen y Destino son obligatorios", Toast.LENGTH_SHORT).show();
                return;
            }

            // Crear objeto Flete temporal (sin guardar aún)
            Flete flete = new Flete();
            flete.setOrigen(origen);
            flete.setDestino(destino);
            flete.setDistancia(distanciaVal);
            flete.setPeso(pesoVal);
            flete.setTarifa(tarifaVal);
            flete.setUrgente(urgente);

            // Pasar a Paso 2 con los datos temporales
            Intent intent = new Intent(this, CrearFletePaso2Activity.class);
            intent.putExtra("origen", origen);
            intent.putExtra("destino", destino);
            intent.putExtra("distancia", distanciaVal);
            intent.putExtra("peso", pesoVal);
            intent.putExtra("tarifa", tarifaVal);
            intent.putExtra("urgente", urgente);
            startActivity(intent);

        } catch (NumberFormatException e) {
            Toast.makeText(this, "Distancia, peso y tarifa deben ser números válidos", Toast.LENGTH_SHORT).show();
        }
    }
}