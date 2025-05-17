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

import java.util.ArrayList;
import java.util.List;

import py.edison.megasoftappv2.R;
import py.edison.megasoftappv2.activities.Fletes.DetalleFleteActivity;
import py.edison.megasoftappv2.entidades.Flete;

public class FleteAdapter extends RecyclerView.Adapter<FleteAdapter.FleteViewHolder> {
    private List<Flete> listaFletes;
    private Context context;
    private OnItemClickListener itemClickListener;
    


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

        // Setear los datos en los TextView
        holder.tvOrigen.setText(flete.getOrigen());
        holder.tvDestino.setText(flete.getDestino());
        holder.tvEstado.setText(flete.getEstado());
        holder.tvFecha.setText(flete.getFechaSalida());

        // Cambiar color según estado
        int colorRes, textColorRes;
        switch (flete.getEstado()) {
            case "ENTREGADO":
                colorRes = R.color.verde_claro;
                textColorRes = R.color.verde_oscuro;
                break;
            case "CANCELADO":
                colorRes = R.color.rojo_claro;
                textColorRes = R.color.rojo_oscuro;
                break;
            case "EN_PROCESO":
                colorRes = R.color.azul_claro;
                textColorRes = R.color.azul_oscuro;
                break;
            default: // PENDIENTE u otro
                colorRes = R.color.amarillo_claro;
                textColorRes = R.color.amarillo_oscuro;
        }

        holder.itemView.setBackgroundColor(ContextCompat.getColor(context, colorRes));
        holder.tvEstado.setTextColor(ContextCompat.getColor(context, textColorRes));

        // Manejar clic en el item
        holder.itemView.setOnClickListener(v -> {
            if (itemClickListener != null) {
                itemClickListener.onItemClick(flete);
            }
        });

        // En FleteAdapter
        holder.btnEditar.setOnClickListener(v -> {
            Intent intent = new Intent(context, DetalleFleteActivity.class);
            intent.putExtra("FLETE_ID", listaFletes.get(position).getId());
            intent.putExtra("MODO_EDICION", true);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return listaFletes.size();
    }

    public void actualizarLista(List<Flete> nuevosFletes) {
        this.listaFletes.clear();
        this.listaFletes.addAll(nuevosFletes != null ? nuevosFletes : new ArrayList<>());
        notifyDataSetChanged();
    }

    static class FleteViewHolder extends RecyclerView.ViewHolder {
        public Button btnEditar;
        TextView tvOrigen, tvDestino, tvEstado, tvFecha;

        public FleteViewHolder(@NonNull View itemView) {
            super(itemView);
            tvOrigen = itemView.findViewById(R.id.tvOrigen);
            tvDestino = itemView.findViewById(R.id.tvDestino);
            tvEstado = itemView.findViewById(R.id.tvEstado);
            tvFecha = itemView.findViewById(R.id.tvFecha);
        }
    }

}
