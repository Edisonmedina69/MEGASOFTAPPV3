package py.edison.megasoftappv2.activities.Clientes;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import py.edison.megasoftappv2.R;
import py.edison.megasoftappv2.activities.Fletes.CrearFleteActivity;
import py.edison.megasoftappv2.adapters.ClientAdapter;
import py.edison.megasoftappv2.entidades.Client;
import py.edison.megasoftappv2.servicios.ClienteListCallback;
import py.edison.megasoftappv2.servicios.ClienteService;

public class GestionClientesActivity extends AppCompatActivity implements ClientAdapter.OnClientClickListener {
    private ClientAdapter clientAdapter;
    private ClienteService clienteService;
    private List<Client> clients = new ArrayList<>();
    private ProgressBar progressBar;
    private RecyclerView recyclerViewClientes;
    private FloatingActionButton fabAddClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestion_clientes);

        // Inicialización de servicios  y vistas
        clienteService = new ClienteService();
        inicializarVistas();
        configurarRecyclerView();
        cargarClientes();

        // Configurar listeners
        fabAddClient.setOnClickListener(v -> mostrarDialogoCliente(null));



    }

    private void configurarVistas() {
        progressBar = findViewById(R.id.progressBar);
        RecyclerView recyclerView = findViewById(R.id.recyclerViewFletes);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //  clientAdapter = new FleteAdapter(new ArrayList<>(), this::mostrarDetalleFlete);
        recyclerView.setAdapter(clientAdapter);

        findViewById(R.id.btnAgregarFlete).setOnClickListener(v ->
                startActivity(new Intent(this, CrearFleteActivity.class)));
    }

    private void inicializarVistas() {
        progressBar = findViewById(R.id.progressBar);
        recyclerViewClientes = findViewById(R.id.recyclerViewClientes);
        fabAddClient = findViewById(R.id.fabAddClient);
    }
    private void configurarRecyclerView() {
        clientAdapter = new ClientAdapter(clients, new ClientAdapter.OnClientClickListener() {
            @Override
            public void onClientClick(int position) {
                mostrarDialogoCliente(clients.get(position));
            }

            @Override
            public void onClientLongClick(int position) {
                mostrarDialogoConfirmarEliminacion(clients.get(position));
            }
        });

        recyclerViewClientes.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewClientes.setAdapter(clientAdapter);
    }

    private void cargarClientes() {
        showProgress();
        clienteService.obtenerClientes(new ClienteListCallback() {
            @Override
            public void onSuccess(List<Client> listaObtenida) {
                hideProgress();
                clients.clear(); // Esta es la lista de clase
                clients.addAll(listaObtenida); // Agregamos los nuevos clientes
                clientAdapter.notifyDataSetChanged(); // Notificamos al adapter
            }

            @Override
            public void onError(String error) {
                hideProgress();
                mostrarError("Error al cargar clientes: " + error);
            }
        });
    }


    private void mostrarDialogoCliente(Client clienteExistente) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(clienteExistente == null ? "Nuevo Cliente" : "Editar Cliente");

        View dialogView = LayoutInflater.from(this).inflate(R.layout.item_cliente, null);

        // Obtener referencias a los campos del formulario
        EditText etNombre = dialogView.findViewById(R.id.etNombre);
        EditText etIdentidad = dialogView.findViewById(R.id.etIdentidad);
        EditText etRuc = dialogView.findViewById(R.id.etRuc);
        EditText etTelefono = dialogView.findViewById(R.id.etTelefono);
        EditText etDireccion = dialogView.findViewById(R.id.etDireccion);

        // Si estamos editando, prellenamos los campos
        if (clienteExistente != null) {
            etNombre.setText(clienteExistente.getNombre());
            etIdentidad.setText(clienteExistente.getIdentidad());
            etRuc.setText(clienteExistente.getRuc());
            etTelefono.setText(clienteExistente.getTelefono());
            etDireccion.setText(clienteExistente.getDireccion());
        }

        builder.setView(dialogView)
                .setPositiveButton("Guardar", (dialog, which) -> {
                    Client cliente = clienteExistente != null ? clienteExistente : new Client();
                    cliente.setNombre(etNombre.getText().toString());
                    cliente.setIdentidad(etIdentidad.getText().toString());
                    cliente.setRuc(etRuc.getText().toString());
                    cliente.setTelefono(etTelefono.getText().toString());
                    cliente.setDireccion(etDireccion.getText().toString());

                    if (validarCliente(cliente)) {
                        if (clienteExistente == null) {
                            guardarCliente(cliente);
                        } else {
                            actualizarCliente(cliente);
                        }
                    }
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private boolean validarCliente(Client cliente) {
        if (cliente.getNombre().trim().isEmpty()) {
            mostrarError("Nombre es requerido");
            return false;
        }
        if (cliente.getRuc().length() != 8) {
            mostrarError("RUC debe tener 8 dígitos");
            return false;
        }
        if (cliente.getTelefono().trim().isEmpty()) {
            mostrarError("Teléfono es requerido");
            return false;
        }
        return true;
    }
    private void guardarCliente(Client cliente) {
        showProgress();
        clienteService.guardarCliente(cliente, new ClienteListCallback() {
            @Override
            public void onSuccess(List<Client> clientes) {
                hideProgress();
                mostrarMensaje("Cliente guardado exitosamente");
                cargarClientes();
            }

            @Override
            public void onError(String error) {
                hideProgress();
                mostrarError("Error al guardar: " + error);
            }
        });
    }


    private void actualizarCliente(Client cliente) {
      showProgress();
        clienteService.actualizarCliente(cliente,   new ClienteListCallback() {
            @Override
            public void onSuccess(List<Client> clientes) {
                hideProgress();
                mostrarMensaje("Cliente actualizado");
                cargarClientes();
            }


            public void onError(String error) {
                hideProgress();
                mostrarError("Error al actualizar: " + error);
            }
        });
    }

    //@Override
    public void onClientClick(int position) {
        mostrarDialogoCliente(clients.get(position));
    }

    //@Override
    public void     onClientLongClick(int position) {
        mostrarDialogoConfirmarEliminacion(clients.get(position));
    }

    private void mostrarDialogoConfirmarEliminacion(Client cliente) {
        new AlertDialog.Builder(this)
                .setTitle("Confirmar eliminación")
                .setMessage("¿Estás seguro de eliminar a " + cliente.getNombre() + "?")
                .setPositiveButton("Eliminar", (dialog, which) -> eliminarCliente(cliente.getId()))
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void eliminarCliente(String clienteId) {
      showProgress();
        clienteService.eliminarCliente(clienteId, new ClienteListCallback() {
            @Override
            public void onSuccess(List<Client> clientes) {
                hideProgress();
                mostrarMensaje("Cliente eliminado");
                cargarClientes();
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
        recyclerViewClientes.setVisibility(View.GONE);
    }

    private void hideProgress() {
        progressBar.setVisibility(View.GONE);
        recyclerViewClientes.setVisibility(View.VISIBLE);
    }

    private void mostrarMensaje(String mensaje) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show();
    }

    private void mostrarError(String error) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }

}