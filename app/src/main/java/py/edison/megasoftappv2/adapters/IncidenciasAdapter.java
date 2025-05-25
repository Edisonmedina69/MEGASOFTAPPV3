package py.edison.megasoftappv2.adapters;

import android.view.*;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import py.edison.megasoftappv2.R;
import py.edison.megasoftappv2.entidades.Incidencia;


import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class IncidenciasAdapter extends RecyclerView.Adapter<IncidenciasAdapter.ViewHolder> {

    private final List<Incidencia> listaIncidencias;

    public IncidenciasAdapter(List<Incidencia> lista) {
        this.listaIncidencias = lista;
    }

    @NonNull
    @Override
    public IncidenciasAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_incidencia, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull IncidenciasAdapter.ViewHolder holder, int position) {
        Incidencia i = listaIncidencias.get(position);
        holder.txtDescripcion.setText(i.getDescripcion());

        holder.txtFecha.setText("Fecha: " + new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()));
    }

    @Override
    public int getItemCount() {
        return listaIncidencias.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtDescripcion, txtEstado, txtFecha;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtDescripcion = itemView.findViewById(R.id.txtDescripcion);
            txtEstado = itemView.findViewById(R.id.txtEstado);
            txtFecha = itemView.findViewById(R.id.txtFecha);
        }
    }
}
