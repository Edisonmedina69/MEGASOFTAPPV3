package py.edison.megasoftappv2.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import py.edison.megasoftappv2.R;
import py.edison.megasoftappv2.entidades.Conductor;

public class ConductorAdapter extends RecyclerView.Adapter<ConductorAdapter.ConductorViewHolder> {
    private List<Conductor> conductores;
    private OnConductorClickListener listener;

    public interface OnConductorClickListener {
        void onConductorClick(int position);
        void onConductorLongClick(int position);
    }

    public ConductorAdapter(List<Conductor> conductores, OnConductorClickListener listener) {
        this.conductores = conductores;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ConductorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_conductor_lista, parent, false);
        return new ConductorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ConductorViewHolder holder, int position) {
        Conductor conductor = conductores.get(position);
        holder.tvNombre.setText(conductor.getNombre());
        holder.tvTelefono.setText(conductor.getTelefono());
        holder.tvLicencia.setText("Lic: " + conductor.getNumeroLicencia());
        holder.tvVehiculo.setText(conductor.getVehiculoAsignado());

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onConductorClick(position);
            }
        });

        holder.itemView.setOnLongClickListener(v -> {
            if (listener != null) {
                listener.onConductorLongClick(position);
            }
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return conductores != null ? conductores.size() : 0;
    }

    public void actualizarLista(List<Conductor> nuevaLista) {
        this.conductores = nuevaLista;
        notifyDataSetChanged();
    }

    public static class ConductorViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombre, tvTelefono, tvLicencia, tvVehiculo;

        public ConductorViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombre = itemView.findViewById(R.id.tvNombre);
            tvTelefono = itemView.findViewById(R.id.tvTelefono);
            tvLicencia = itemView.findViewById(R.id.tvLicencia);
            tvVehiculo = itemView.findViewById(R.id.tvVehiculo);
        }
    }
}