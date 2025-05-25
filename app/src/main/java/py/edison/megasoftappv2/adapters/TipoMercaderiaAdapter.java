package py.edison.megasoftappv2.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import py.edison.megasoftappv2.R;

import py.edison.megasoftappv2.entidades.TipoMercancia;

public class TipoMercaderiaAdapter extends RecyclerView.Adapter<TipoMercaderiaAdapter.TipoMercaderiaViewHolder> {
    private List<TipoMercancia> tiposMercaderia;
    private OnTipoMercaderiaClickListener listener;

    public interface OnTipoMercaderiaClickListener {
        void onTipoMercaderiaClick(int position);
        void onTipoMercaderiaLongClick(int position);
    }

    public TipoMercaderiaAdapter(List<TipoMercancia> tiposMercaderia, OnTipoMercaderiaClickListener listener) {
        this.tiposMercaderia = tiposMercaderia;
        this.listener = listener;
    }

    @NonNull
    @Override
    public TipoMercaderiaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_tipo_mercaderia_lista, parent, false);
        return new TipoMercaderiaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TipoMercaderiaViewHolder holder, int position) {
        TipoMercancia tipoMercaderia = tiposMercaderia.get(position);
        holder.tvDescripcion.setText(tipoMercaderia.getDescripcion());

        // Si tienes más campos en TipoMercaderia, puedes mostrarlos aquí
        // holder.tvOtroCampo.setText(tipoMercaderia.getOtroCampo());

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onTipoMercaderiaClick(position);
            }
        });

        holder.itemView.setOnLongClickListener(v -> {
            if (listener != null) {
                listener.onTipoMercaderiaLongClick(position);
            }
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return tiposMercaderia != null ? tiposMercaderia.size() : 0;
    }

    public void actualizarLista(List<TipoMercancia> nuevaLista) {
        this.tiposMercaderia = nuevaLista;
        notifyDataSetChanged();
    }

    public static class TipoMercaderiaViewHolder extends RecyclerView.ViewHolder {
        TextView tvDescripcion;
        // Agrega más TextViews si necesitas mostrar más campos

        public TipoMercaderiaViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDescripcion = itemView.findViewById(R.id.tvDescripcion);
            // Inicializa otros views aquí si los tienes
        }
    }
}