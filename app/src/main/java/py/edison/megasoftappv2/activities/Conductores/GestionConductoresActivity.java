package py.edison.megasoftappv2.activities.Conductores;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import py.edison.megasoftappv2.R;
import py.edison.megasoftappv2.adapters.ConductorAdapter;
import py.edison.megasoftappv2.entidades.Conductor;
import py.edison.megasoftappv2.servicios.ConductorService;

public class GestionConductoresActivity extends AppCompatActivity implements ConductorAdapter.OnConductorClickListener {

    private ConductorAdapter conductorAdapter;
    private ConductorService conductorService;
    private List<Conductor> conductores = new ArrayList<>();
    private ProgressBar progressBar;
    private RecyclerView recyclerViewConductores;
    private FloatingActionButton fabAddConductor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestion_conductores);

        conductorService = new ConductorService();
        inicializarVistas();
        configurarRecyclerView();
        cargarConductores();

        fabAddConductor.setOnClickListener(v -> mostrarDialogoConductor(null));
    }

    private void inicializarVistas() {
        progressBar = findViewById(R.id.progressBar);
        recyclerViewConductores = findViewById(R.id.recyclerViewConductores);
        fabAddConductor = findViewById(R.id.fabAddConductor);
    }

    private void configurarRecyclerView() {
        conductorAdapter = new ConductorAdapter(conductores, new ConductorAdapter.OnConductorClickListener() {
            @Override
            public void onConductorClick(int position) {
                mostrarDialogoConductor(conductores.get(position));
            }

            @Override
            public void onConductorLongClick(int position) {
                mostrarDialogoConfirmarEliminacion(conductores.get(position));
            }
        });

        recyclerViewConductores.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewConductores.setAdapter(conductorAdapter);
    }

    private void cargarConductores() {
        showProgress();
        conductorService.listarTodosConductores(new ConductorService.ConductorListCallback() {
            @Override
            public void onSuccess(List<Conductor> listaConductores) {
                hideProgress();
                conductores.clear();
                conductores.addAll(listaConductores);
                conductorAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(String error) {
                hideProgress();
                mostrarError("Error al cargar conductores: " + error);
            }
        });
    }

    private void mostrarDialogoConductor(Conductor conductorExistente) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(conductorExistente == null ? "Nuevo Conductor" : "Editar Conductor");

        View dialogView = LayoutInflater.from(this).inflate(R.layout.item_conductor, null);
        EditText etNombre = dialogView.findViewById(R.id.etNombre);
        EditText etTelefono = dialogView.findViewById(R.id.etTelefono);
        EditText etLicencia = dialogView.findViewById(R.id.etLicencia);
        EditText etVehiculo = dialogView.findViewById(R.id.etVehiculo);

        if (conductorExistente != null) {
            etNombre.setText(conductorExistente.getNombre());
            etTelefono.setText(conductorExistente.getTelefono());
            etLicencia.setText(conductorExistente.getNumeroLicencia());
            etVehiculo.setText(conductorExistente.getVehiculoAsignado());
        }

        builder.setView(dialogView)
                .setPositiveButton("Guardar", (dialog, which) -> {
                    Conductor conductor = conductorExistente != null ? conductorExistente : new Conductor();
                    conductor.setNombre(etNombre.getText().toString());
                    conductor.setTelefono(etTelefono.getText().toString());
                    conductor.setNumeroLicencia(etLicencia.getText().toString());
                    conductor.setVehiculoAsignado(etVehiculo.getText().toString());

                    if (validarConductor(conductor)) {
                        if (conductorExistente == null) {
                            guardarConductor(conductor);
                        } else {
                            actualizarConductor(conductor);
                        }
                    }
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private boolean validarConductor(Conductor conductor) {
        if (conductor.getNombre().trim().isEmpty()) {
            mostrarError("Nombre es requerido");
            return false;
        }
        if (conductor.getTelefono().trim().isEmpty()) {
            mostrarError("Teléfono es requerido");
            return false;
        }
        if (conductor.getNumeroLicencia().trim().isEmpty()) {
            mostrarError("Número de licencia es requerido");
            return false;
        }
        return true;
    }

    @Override
    public void onConductorClick(int position) {
        mostrarDialogoConductor(conductores.get(position));
    }

    @Override
    public void onConductorLongClick(int position) {
        mostrarDialogoConfirmarEliminacion(conductores.get(position));
    }

    private void mostrarDialogoConfirmarEliminacion(Conductor conductor) {
        new AlertDialog.Builder(this)
                .setTitle("Confirmar eliminación")
                .setMessage("¿Eliminar a " + conductor.getNombre() + "?")
                .setPositiveButton("Eliminar", (dialog, which) -> eliminarConductor(conductor.getId()))
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void guardarConductor(Conductor conductor) {
        showProgress();
        conductorService.registrarConductor(conductor, new ConductorService.ConductorListCallback() {
            @Override
            public void onSuccess(List<Conductor> conductores) {
                hideProgress();
                mostrarMensaje("Conductor registrado exitosamente");
                cargarConductores();
            }

            @Override
            public void onError(String error) {
                hideProgress();
                mostrarError("Error al registrar conductor: " + error);
            }
        });
    }

    private void actualizarConductor(Conductor conductor) {
        showProgress();
        conductorService.actualizarConductor(conductor, new ConductorService.ConductorListCallback() {
            @Override
            public void onSuccess(List<Conductor> conductores) {
                hideProgress();
                mostrarMensaje("Conductor actualizado exitosamente");
                cargarConductores();
            }

            @Override
            public void onError(String error) {
                hideProgress();
                mostrarError("Error al actualizar conductor: " + error);
            }
        });
    }

    private void eliminarConductor(String conductorId) {
        showProgress();
        conductorService.eliminarConductor(conductorId, new ConductorService.ConductorListCallback() {
            @Override
            public void onSuccess(List<Conductor> conductores) {
                hideProgress();
                mostrarMensaje("Conductor eliminado exitosamente");
                cargarConductores();
            }

            @Override
            public void onError(String error) {
                hideProgress();
                mostrarError("Error al eliminar conductor: " + error);
            }
        });
    }

    private void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
        recyclerViewConductores.setVisibility(View.GONE);
    }

    private void hideProgress() {
        progressBar.setVisibility(View.GONE);
        recyclerViewConductores.setVisibility(View.VISIBLE);
    }

    private void mostrarMensaje(String mensaje) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show();
    }

    private void mostrarError(String error) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }
}