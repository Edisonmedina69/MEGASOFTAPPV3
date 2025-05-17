package py.edison.megasoftappv2.fragments.Fletes;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import py.edison.megasoftappv2.R;
import py.edison.megasoftappv2.activities.Fletes.DetalleFleteActivity;
import py.edison.megasoftappv2.adapters.FleteAdapter;
import py.edison.megasoftappv2.entidades.Flete;
import py.edison.megasoftappv2.servicios.FleteService;

public class FleteListFragment extends Fragment {
    private FleteAdapter adapter;
    private FleteService fleteService;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_flete_list, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.rvFletes);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new FleteAdapter(new ArrayList<>(), getContext());
        adapter.setOnItemClickListener(flete -> {
            // Manejar clic para ver detalle
            abrirDetalleFlete(flete.getId());
        });
        recyclerView.setAdapter(adapter);

        fleteService = new FleteService();
        cargarFletes();

        return view;
    }

    private void cargarFletes() {
        fleteService.listarFletes(new FleteService.FletesListCallback() {
            @Override
            public void onSuccess(List<Flete> fletes) {
                adapter.actualizarLista(fletes);
            }

            @Override
            public void onError(String error) {
                Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void abrirDetalleFlete(String fleteId) {
        Intent intent = new Intent(getActivity(), DetalleFleteActivity.class);
        intent.putExtra("FLETE_ID", fleteId);
        startActivity(intent);
    }
}