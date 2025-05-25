package py.edison.megasoftappv2.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import py.edison.megasoftappv2.R;
import py.edison.megasoftappv2.activities.Fletes.CrearFletePaso1Activity;
import py.edison.megasoftappv2.entidades.Flete;

public class FleteAdapter extends RecyclerView.Adapter<FleteAdapter.FleteViewHolder> {
    private List<Flete> listaFletes;
    private Context context;
    private OnItemClickListener itemClickListener;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());

    public interface OnItemClickListener {
        void onItemClick(Flete flete);
    }

    public FleteAdapter(List<Flete> listaFletes, Context context) {
        this.listaFletes = listaFletes != null ? listaFletes : new ArrayList<>();
        this.context = context;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.itemClickListener = listener;
    }

    public void actualizarLista(List<Flete> nuevaLista) {
        this.listaFletes = nuevaLista != null ? nuevaLista : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public FleteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_flete, parent, false);
        return new FleteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FleteViewHolder holder, int position) {
        Flete flete = listaFletes.get(position);

        // Setear datos
        holder.tvOrigen.setText(flete.getOrigen() != null ? flete.getOrigen() : "N/A");
        holder.tvDestino.setText(flete.getDestino() != null ? flete.getDestino() : "N/A");

        // Estado automático como PENDIENTE (ya no viene del usuario)
        String estado = "PENDIENTE"; // Estado fijo
        holder.tvEstado.setText(estado);

        // Formatear fecha
        if (flete.getFechaSalida() != null) {
            holder.tvFecha.setText(dateFormat.format(flete.getFechaSalida()));
        } else {
            holder.tvFecha.setText("Fecha no definida");
        }

        // Establecer color según estado (ahora siempre PENDIENTE)
        int[] colors = getEstadoColors(estado);
        holder.itemView.setBackgroundColor(ContextCompat.getColor(context, colors[0]));
        holder.tvEstado.setTextColor(ContextCompat.getColor(context, colors[1]));

        // Listeners
        holder.itemView.setOnClickListener(v -> {
            if (itemClickListener != null) {
                itemClickListener.onItemClick(flete);
            }
        });

        holder.btnEditar.setOnClickListener(v -> {
            Intent intent = new Intent(context, CrearFletePaso1Activity.class);
            intent.putExtra("FLETE_ID", flete.getId());
            intent.putExtra("MODO_EDICION", true);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return listaFletes.size();
    }

    private int[] getEstadoColors(String estado) {
        // Simplificado ya que siempre será PENDIENTE
        return new int[]{R.color.amarillo_claro, R.color.amarillo_oscuro};

        /* Versión original por si necesitas mantener otros estados:
        switch (estado) {
            case "COMPLETADO":
                return new int[]{R.color.verde_claro, R.color.verde_oscuro};
            case "CANCELADO":
                return new int[]{R.color.rojo_claro, R.color.rojo_oscuro};
            case "EN_PROCESO":
                return new int[]{R.color.azul_claro, R.color.azul_oscuro};
            case "PENDIENTE":
            default:
                return new int[]{R.color.amarillo_claro, R.color.amarillo_oscuro};
        }
        */
    }

    static class FleteViewHolder extends RecyclerView.ViewHolder {
        TextView tvOrigen, tvDestino, tvEstado, tvFecha;
        Button btnEditar;

        public FleteViewHolder(@NonNull View itemView) {
            super(itemView);
            tvOrigen = itemView.findViewById(R.id.etOrigen);
            tvDestino = itemView.findViewById(R.id.etDestino);
            tvEstado = itemView.findViewById(R.id.tvEstado);
            tvFecha = itemView.findViewById(R.id.etFechaSalida);
            btnEditar = itemView.findViewById(R.id.btneditarFlete);
        }
    }
}