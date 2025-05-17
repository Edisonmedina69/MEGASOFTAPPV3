package py.edison.megasoftappv2.activities.Fletes;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.datepicker.MaterialDatePicker;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import py.edison.megasoftappv2.R;
import py.edison.megasoftappv2.adapters.FleteAdapter;
import py.edison.megasoftappv2.entidades.Flete;
import py.edison.megasoftappv2.servicios.FleteService;


public class GestionFletesActivity extends AppCompatActivity {

    private static final String TAG_DATE_RANGE = "RANGO_FECHAS";
    private static final String TAG_DATE_START = "DATE_PICKER_INICIO";
    private static final String TAG_DATE_END = "DATE_PICKER_FIN";


    private FleteAdapter adapter;
    private FleteService fleteService;
    private List<Flete> listaFletes = new ArrayList<>();
    private ProgressBar progressBar;
    private MaterialButton btnFiltrarFecha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestion_fletes);

        fleteService = new FleteService();
        inicializarVistas();
        cargarFletes();
    }

    private void inicializarVistas() {
        progressBar = findViewById(R.id.progressBar);
        btnFiltrarFecha = findViewById(R.id.btnFiltrarFecha);

        // Configurar RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recyclerViewFletes);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new FleteAdapter(listaFletes, this);
        recyclerView.setAdapter(adapter);

        // Listeners
        adapter.setOnItemClickListener(this::abrirDetalleFlete);
        findViewById(R.id.btnAgregarFlete).setOnClickListener(v -> abrirCrearFlete());
        btnFiltrarFecha.setOnClickListener(v -> mostrarSelectorFechas());
    }
    private void mostrarSelectorRangoFechas() {
        MaterialDatePicker.Builder<androidx.core.util.Pair<Long, Long>> builder = MaterialDatePicker.Builder.dateRangePicker();
        builder.setTitleText("Seleccionar rango");

        MaterialDatePicker<androidx.core.util.Pair<Long, Long>> picker = builder.build();
        picker.addOnPositiveButtonClickListener(selection -> {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            aplicarFiltros("",
                    sdf.format(new Date(selection.first)),
                    sdf.format(new Date(selection.second)));
        });
        picker.show(getSupportFragmentManager(), "DATE_PICKER");
    }



    private void configurarVista() {
        // 1. Configuración del ProgressBar
        progressBar = findViewById(R.id.progressBar);

        // 2. Configuración del RecyclerView
        configurarRecyclerView();

        // 3. Configuración de botones
        configurarBotones();
    }

    private void configurarRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.recyclerViewFletes);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new FleteAdapter(listaFletes, this);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(this::abrirDetalleFlete);
    }

    private void configurarBotones() {
        // Configuración del botón Agregar
        findViewById(R.id.btnAgregarFlete).setOnClickListener(v -> abrirCrearFlete());

        // Configuración del botón Filtrar (evitando duplicación)
        btnFiltrarFecha = findViewById(R.id.btnFiltrarFecha);
        btnFiltrarFecha.setOnClickListener(v -> mostrarSelectorFechas());
    }

    private void mostrarSelectorFechas() {
        MaterialDatePicker.Builder<androidx.core.util.Pair<Long, Long>> builder =
                MaterialDatePicker.Builder.dateRangePicker();
        builder.setTitleText(getString(R.string.seleccionar_rango_fechas));

        MaterialDatePicker<androidx.core.util.Pair<Long, Long>> picker = builder.build();

        picker.addOnPositiveButtonClickListener(selection -> {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            String fechaInicio = sdf.format(new Date(selection.first));
            String fechaFin = sdf.format(new Date(selection.second));

            // Validación de fechas
            if (selection.first > selection.second) {
                Toast.makeText(this, "La fecha inicial no puede ser mayor a la final", Toast.LENGTH_SHORT).show();
                return;
            }

            aplicarFiltros("", fechaInicio, fechaFin);
        });

        picker.show(getSupportFragmentManager(), "DATE_RANGE_PICKER");
    }

    private void abrirDetalleFlete(Flete flete) {
        Intent intent = new Intent(this, DetalleFleteActivity.class);
        intent.putExtra("FLETE_ID", flete.getId());
        startActivity(intent);
    }

    private void cargarFletes() {
        showProgress();
        fleteService.listarFletes(new FleteService.FletesListCallback() {
            @Override
            public void onSuccess(List<Flete> fletes) {
                hideProgress();
                listaFletes.clear();
                listaFletes.addAll(fletes);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onError(String error) {
                hideProgress();
                Toast.makeText(GestionFletesActivity.this, error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgress() {
        progressBar.setVisibility(View.GONE);
    }

    private void abrirCrearFlete() {
        startActivity(new Intent(this, CrearFleteActivity.class));
    }

    private void mostrarDialogoFiltros() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.dialog_filtros, null);

        Spinner spinnerEstado = view.findViewById(R.id.spinnerEstado);
        EditText etFechaInicio = view.findViewById(R.id.etFechaInicio);
        EditText etFechaFin = view.findViewById(R.id.etFechaFin);

        builder.setView(view)
                .setPositiveButton("Aplicar", (dialog, which) -> {
                    String estado = spinnerEstado.getSelectedItem().toString();
                    String fechaInicio = etFechaInicio.getText().toString();
                    String fechaFin = etFechaFin.getText().toString();
                    aplicarFiltros(estado, fechaInicio, fechaFin);
                })
                .setNegativeButton("Limpiar", (dialog, which) -> cargarFletes());

        AlertDialog dialog = builder.create();
        dialog.show();

        etFechaInicio.setOnClickListener(v -> {
            MaterialDatePicker<Long> datePicker = MaterialDatePicker.Builder.datePicker()
                    .setTitleText(getString(R.string.fecha_inicio))
                    .build();
            datePicker.addOnPositiveButtonClickListener(selection -> {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                etFechaInicio.setText(sdf.format(new Date(selection)));
            });
            datePicker.show(getSupportFragmentManager(), TAG_DATE_START);
        });

        etFechaFin.setOnClickListener(v -> {
            MaterialDatePicker<Long> datePicker = MaterialDatePicker.Builder.datePicker()
                    .setTitleText(getString(R.string.fecha_fin))
                    .build();
            datePicker.addOnPositiveButtonClickListener(selection -> {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                etFechaFin.setText(sdf.format(new Date(selection)));
            });
            datePicker.show(getSupportFragmentManager(), TAG_DATE_END);
        });
    }

    private void aplicarFiltros(String estadoFiltro, String fechaInicio, String fechaFin) {
        List<Flete> filtrados = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

        for (Flete f : listaFletes) {
            boolean coincideEstado = estadoFiltro.isEmpty() || f.getEstado().equals(estadoFiltro);
            boolean coincideFecha = true;

            if (!fechaInicio.isEmpty() && !fechaFin.isEmpty()) {
                try {
                    Date fechaInicioDate = sdf.parse(fechaInicio);
                    Date fechaFinDate = sdf.parse(fechaFin);
                    Date fechaFlete = sdf.parse(f.getFechaSalida());

                    coincideFecha = (fechaFlete.equals(fechaInicioDate) || fechaFlete.after(fechaInicioDate)) &&
                            (fechaFlete.equals(fechaFinDate) || fechaFlete.before(fechaFinDate));

                } catch (Exception e) {
                    e.printStackTrace();
                    coincideFecha = false;
                }
            }

            if (coincideEstado && coincideFecha) {
                filtrados.add(f);
            }
        }
        adapter.actualizarLista(filtrados);
    }

    @Override
    protected void onDestroy() {
        fleteService.limpiarListeners();
        super.onDestroy();
    }
}