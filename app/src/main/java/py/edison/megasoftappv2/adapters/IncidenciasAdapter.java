

import android.view.*;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import py.edison.megasoftappv2.R;
import py.edison.megasoftappv2.entidades.Incidencias;


import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class IncidenciasAdapter extends RecyclerView.Adapter<IncidenciasAdapter.ViewHolder> {

    private final List<Incidencias> lista;

    public IncidenciasAdapter(List<Incidencias> lista) {
        this.lista = lista;
    }

    @NonNull
    @Override
    public IncidenciasAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_incidencia, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull IncidenciasAdapter.ViewHolder holder, int position) {
        Incidencias i = lista.get(position);
        holder.txtDescripcion.setText(i.getDescripcion());
        holder.txtEstado.setText("Estado: " + i.getGravedad());
        holder.txtFecha.setText("Fecha: " + new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(i.getFecha().toDate()));
    }

    @Override
    public int getItemCount() {
        return lista.size();
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
