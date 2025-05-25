package py.edison.megasoftappv2.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import py.edison.megasoftappv2.R;
import py.edison.megasoftappv2.activities.Clientes.GestionClientesActivity;
import py.edison.megasoftappv2.activities.Conductores.GestionConductoresActivity;
import py.edison.megasoftappv2.activities.Fletes.GestionFletesActivity;
import py.edison.megasoftappv2.activities.TipoMercaderia.GestionTipoMercanciaActivity;
import py.edison.megasoftappv2.activities.Vehiculos.GestionVehiculosActivity;

public class MainActivity extends AppCompatActivity {

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Configurar listeners para los botones principales
        findViewById(R.id.btnGestionFletes).setOnClickListener(v -> {
            startActivity(new Intent(this, GestionFletesActivity.class));
        });

        findViewById(R.id.btnGestionClientes).setOnClickListener(v -> {
            startActivity(new Intent(this, GestionClientesActivity.class));
        });

        findViewById(R.id.btnGestionConductores).setOnClickListener(v -> {
            startActivity(new Intent(this, GestionConductoresActivity.class));
        });

        findViewById(R.id.btnGestionVehiculos).setOnClickListener(v -> {
            startActivity(new Intent(this, GestionVehiculosActivity.class));
        });

        findViewById(R.id.btnTipoMercaderia).setOnClickListener(v -> {
            startActivity(new Intent(this, GestionTipoMercanciaActivity.class));
        });



    }
}