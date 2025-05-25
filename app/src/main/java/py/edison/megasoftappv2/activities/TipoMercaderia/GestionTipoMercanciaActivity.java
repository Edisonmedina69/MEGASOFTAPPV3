package py.edison.megasoftappv2.activities.TipoMercaderia;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import py.edison.megasoftappv2.R;
import py.edison.megasoftappv2.entidades.TipoMercancia;
import py.edison.megasoftappv2.interfaces.TipoMercanciaListCallback;
import py.edison.megasoftappv2.servicios.TipoMercanciaService;

public class GestionTipoMercanciaActivity extends AppCompatActivity {

    private TipoMercanciaService tipoMercanciaService;
    private List<TipoMercancia> tiposMercancia = new ArrayList<>();
    private ArrayAdapter<TipoMercancia> adapter;
    private ProgressBar progressBar;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestion_tipo_mercancia);

        tipoMercanciaService = new TipoMercanciaService();
        progressBar = findViewById(R.id.progressBar);
        listView = findViewById(R.id.lvTiposMercancia);

        configurarLista();
        cargarTiposMercancia();

        FloatingActionButton fab = findViewById(R.id.fabAddTipoMercancia);
        fab.setOnClickListener(v -> mostrarDialogoTipoMercancia(null));
    }

    private void configurarLista() {
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, tiposMercancia);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener((parent, view, position, id) -> {
            mostrarDialogoTipoMercancia(tiposMercancia.get(position));
        });

        listView.setOnItemLongClickListener((parent, view, position, id) -> {
            mostrarDialogoConfirmarEliminacion(tiposMercancia.get(position));
            return true;
        });
    }

    private void cargarTiposMercancia() {
        showProgress();
        tipoMercanciaService.obtenerTiposMercancia(new TipoMercanciaListCallback() {
            @Override
            public void onSuccess(List<TipoMercancia> tipos) {
                hideProgress();
                tiposMercancia.clear();
                tiposMercancia.addAll(tipos);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onError(String error) {
                hideProgress();
                mostrarError("Error al cargar tipos: " + error);
            }
        });
    }

    private void mostrarDialogoTipoMercancia(TipoMercancia tipoExistente) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(tipoExistente == null ? "Nuevo Tipo" : "Editar Tipo");

        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialogo_tipo_mercancia, null);
        EditText etDescripcion = dialogView.findViewById(R.id.etDescripcion);

        if (tipoExistente != null) {
            etDescripcion.setText(tipoExistente.getDescripcion());
        }

        builder.setView(dialogView)
                .setPositiveButton("Guardar", (dialog, which) -> {
                    String descripcion = etDescripcion.getText().toString().trim();

                    if (descripcion.isEmpty()) {
                        mostrarError("La descripción no puede estar vacía");
                        return;
                    }

                    TipoMercancia tipo = tipoExistente != null ? tipoExistente : new TipoMercancia();
                    tipo.setDescripcion(descripcion);

                    if (tipoExistente == null) {
                        guardarTipoMercancia(tipo);
                    } else {
                        actualizarTipoMercancia(tipo);
                    }
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void guardarTipoMercancia(TipoMercancia tipo) {
        showProgress();
        tipoMercanciaService.guardarTipoMercancia(tipo, new TipoMercanciaListCallback() {
            @Override
            public void onSuccess(List<TipoMercancia> tipos) {
                hideProgress();
                mostrarMensaje("Tipo guardado");
                cargarTiposMercancia();
            }

            @Override
            public void onError(String error) {
                hideProgress();
                mostrarError("Error al guardar: " + error);
            }
        });
    }

    private void actualizarTipoMercancia(TipoMercancia tipo) {
        showProgress();
        tipoMercanciaService.actualizarTipoMercancia(tipo, new TipoMercanciaListCallback() {
            @Override
            public void onSuccess(List<TipoMercancia> tipos) {
                hideProgress();
                mostrarMensaje("Tipo actualizado");
                cargarTiposMercancia();
            }

            @Override
            public void onError(String error) {
                hideProgress();
                mostrarError("Error al actualizar: " + error);
            }
        });
    }

    private void mostrarDialogoConfirmarEliminacion(TipoMercancia tipo) {
        new AlertDialog.Builder(this)
                .setTitle("Confirmar eliminación")
                .setMessage("¿Eliminar el tipo: " + tipo.getDescripcion() + "?")
                .setPositiveButton("Eliminar", (dialog, which) -> eliminarTipoMercancia(tipo.getId()))
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void eliminarTipoMercancia(String id) {
        showProgress();
        tipoMercanciaService.eliminarTipoMercancia(id, new TipoMercanciaListCallback() {
            @Override
            public void onSuccess(List<TipoMercancia> tipos) {
                hideProgress();
                mostrarMensaje("Tipo eliminado");
                cargarTiposMercancia();
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
        listView.setVisibility(View.GONE);
    }

    private void hideProgress() {
        progressBar.setVisibility(View.GONE);
        listView.setVisibility(View.VISIBLE);
    }

    private void mostrarMensaje(String mensaje) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show();
    }

    private void mostrarError(String error) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }
}