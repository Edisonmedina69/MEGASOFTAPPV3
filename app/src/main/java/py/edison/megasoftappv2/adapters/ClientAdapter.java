package py.edison.megasoftappv2.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import py.edison.megasoftappv2.R;
import py.edison.megasoftappv2.entidades.Client;

public class ClientAdapter extends RecyclerView.Adapter<ClientAdapter.ClientViewHolder> {
    private List<Client> clients;
    private OnClientClickListener listener;

    public interface OnClientClickListener {
        void onClientClick(int position);
        void onClientLongClick(int position);
    }

    public ClientAdapter(List<Client> clients, OnClientClickListener listener) {
        this.clients = clients;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ClientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_cliente_lista, parent, false); // Usa el layout de ítem visual
        return new ClientViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ClientViewHolder holder, int position) {
        Client client = clients.get(position);
        holder.tvNombre.setText(client.getNombre());
        holder.tvTelefono.setText(client.getTelefono());

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onClientClick(position);
            }
        });

        holder.itemView.setOnLongClickListener(v -> {
            if (listener != null) {
                listener.onClientLongClick(position);
            }
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return clients.size();
    }

    public static class ClientViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombre, tvTelefono;

        public ClientViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombre = itemView.findViewById(R.id.tvNombre);
            tvTelefono = itemView.findViewById(R.id.tvTelefono);
        }
    }
}
