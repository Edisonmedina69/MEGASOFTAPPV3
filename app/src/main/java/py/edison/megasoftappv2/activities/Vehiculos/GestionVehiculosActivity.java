package py.edison.megasoftappv2.activities.Vehiculos;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
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
import py.edison.megasoftappv2.adapters.VehiculoAdapter;
import py.edison.megasoftappv2.entidades.Vehiculo;

import py.edison.megasoftappv2.interfaces.VehiculoListCallback;
import py.edison.megasoftappv2.servicios.VehiculoService;

public class GestionVehiculosActivity extends AppCompatActivity implements VehiculoAdapter.OnVehiculoClickListener {
    private VehiculoAdapter vehiculoAdapter;
    private VehiculoService vehiculoService;
    private List<Vehiculo> vehiculos = new ArrayList<>();
    private ProgressBar progressBar;
    private RecyclerView recyclerViewVehiculos;
    private FloatingActionButton fabAddVehiculo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestion_vehiculos);

        // Inicialización de servicios y vistas
        vehiculoService = new VehiculoService();
        inicializarVistas();
        configurarRecyclerView();
        cargarVehiculos();

        // Configurar listeners
        fabAddVehiculo.setOnClickListener(v -> mostrarDialogoVehiculo(null));
    }

    private void inicializarVistas() {
        progressBar = findViewById(R.id.progressBar);
        recyclerViewVehiculos = findViewById(R.id.recyclerViewVehiculos);
        fabAddVehiculo = findViewById(R.id.fabAddVehiculo);
    }

    private void configurarRecyclerView() {
        vehiculoAdapter = new VehiculoAdapter(vehiculos, new VehiculoAdapter.OnVehiculoClickListener() {
            @Override
            public void onVehiculoClick(int position) {
                mostrarDialogoVehiculo(vehiculos.get(position));
            }

            @Override
            public void onVehiculoLongClick(int position) {
                mostrarDialogoConfirmarEliminacion(vehiculos.get(position));
            }
        });

        recyclerViewVehiculos.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewVehiculos.setAdapter(vehiculoAdapter);
    }

    private void cargarVehiculos() {
        showProgress();
        vehiculoService.obtenerVehiculos(new VehiculoListCallback() {
            @Override
            public void onSuccess(List<Vehiculo> listaObtenida) {
                hideProgress();
                vehiculos.clear();
                vehiculos.addAll(listaObtenida);
                vehiculoAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(String error) {
                hideProgress();
                mostrarError("Error al cargar vehículos: " + error);
            }
        });
    }

    private void mostrarDialogoVehiculo(Vehiculo vehiculoExistente) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(vehiculoExistente == null ? "Nuevo Vehículo" : "Editar Vehículo");

        View dialogView = LayoutInflater.from(this).inflate(R.layout.item_vehiculo, null);

        // Obtener referencias a los campos del formulario
        EditText etMatricula = dialogView.findViewById(R.id.etMatricula);
        EditText etMarca = dialogView.findViewById(R.id.etMarca);
        EditText etModelo = dialogView.findViewById(R.id.etModelo);
        EditText etCapacidad = dialogView.findViewById(R.id.etCapacidad);
        CheckBox cbDisponible = dialogView.findViewById(R.id.cbDisponible);

        // Si estamos editando, prellenamos los campos
        if (vehiculoExistente != null) {
            etMatricula.setText(vehiculoExistente.getMatricula());
            etMarca.setText(vehiculoExistente.getMarca());
            etModelo.setText(vehiculoExistente.getModelo());
            etCapacidad.setText(String.valueOf(vehiculoExistente.getCapacidadKg()));
            cbDisponible.setChecked(vehiculoExistente.isDisponible());
        }

        builder.setView(dialogView)
                .setPositiveButton("Guardar", (dialog, which) -> {
                    Vehiculo vehiculo = vehiculoExistente != null ? vehiculoExistente : new Vehiculo();
                    vehiculo.setMatricula(etMatricula.getText().toString());
                    vehiculo.setMarca(etMarca.getText().toString());
                    vehiculo.setModelo(etModelo.getText().toString());

                    try {
                        vehiculo.setCapacidadKg(Integer.parseInt(etCapacidad.getText().toString()));
                    } catch (NumberFormatException e) {
                        mostrarError("La capacidad debe ser un número válido");
                        return;
                    }

                    vehiculo.setDisponible(cbDisponible.isChecked());

                    if (validarVehiculo(vehiculo)) {
                        if (vehiculoExistente == null) {
                            guardarVehiculo(vehiculo);
                        } else {
                            actualizarVehiculo(vehiculo);
                        }
                    }
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private boolean validarVehiculo(Vehiculo vehiculo) {
        if (vehiculo.getMatricula().trim().isEmpty()) {
            mostrarError("Matrícula es requerida");
            return false;
        }
        if (vehiculo.getMarca().trim().isEmpty()) {
            mostrarError("Marca es requerida");
            return false;
        }
        if (vehiculo.getCapacidadKg() <= 0) {
            mostrarError("La capacidad debe ser mayor a 0");
            return false;
        }
        return true;
    }

    private void guardarVehiculo(Vehiculo vehiculo) {
        showProgress();
        vehiculoService.guardarVehiculo(vehiculo, new VehiculoListCallback() {
            @Override
            public void onSuccess(List<Vehiculo> vehiculos) {
                hideProgress();
                mostrarMensaje("Vehículo guardado exitosamente");
                cargarVehiculos();
            }

            @Override
            public void onError(String error) {
                hideProgress();
                mostrarError("Error al guardar: " + error);
            }
        });
    }

    private void actualizarVehiculo(Vehiculo vehiculo) {
        showProgress();
        vehiculoService.actualizarVehiculo(vehiculo, new VehiculoListCallback() {
            @Override
            public void onSuccess(List<Vehiculo> vehiculos) {
                hideProgress();
                mostrarMensaje("Vehículo actualizado");
                cargarVehiculos();
            }

            @Override
            public void onError(String error) {
                hideProgress();
                mostrarError("Error al actualizar: " + error);
            }
        });
    }

    @Override
    public void onVehiculoClick(int position) {
        mostrarDialogoVehiculo(vehiculos.get(position));
    }

    @Override
    public void onVehiculoLongClick(int position) {
        mostrarDialogoConfirmarEliminacion(vehiculos.get(position));
    }

    private void mostrarDialogoConfirmarEliminacion(Vehiculo vehiculo) {
        new AlertDialog.Builder(this)
                .setTitle("Confirmar eliminación")
                .setMessage("¿Estás seguro de eliminar el vehículo " + vehiculo.getMatricula() + "?")
                .setPositiveButton("Eliminar", (dialog, which) -> eliminarVehiculo(vehiculo.getId()))
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void eliminarVehiculo(String vehiculoId) {
        showProgress();
        vehiculoService.eliminarVehiculo(vehiculoId, new VehiculoListCallback() {
            @Override
            public void onSuccess(List<Vehiculo> vehiculos) {
                hideProgress();
                mostrarMensaje("Vehículo eliminado");
                cargarVehiculos();
            }

            @Override
            public void onError(String error) {
                hideProgress();
                mostrarError("Error al eliminar: " + error);
            }
        });
    }

    private void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
        recyclerViewVehiculos.setVisibility(View.GONE);
    }

    private void hideProgress() {
        progressBar.setVisibility(View.GONE);
        recyclerViewVehiculos.setVisibility(View.VISIBLE);
    }

    private void mostrarMensaje(String mensaje) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show();
    }

    private void mostrarError(String error) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }
}