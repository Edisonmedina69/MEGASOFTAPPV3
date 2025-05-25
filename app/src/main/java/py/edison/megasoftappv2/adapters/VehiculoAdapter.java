package py.edison.megasoftappv2.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import py.edison.megasoftappv2.R;
import py.edison.megasoftappv2.entidades.Vehiculo;

public class VehiculoAdapter extends RecyclerView.Adapter<VehiculoAdapter.VehiculoViewHolder> {

    private List<Vehiculo> vehiculos;
    private OnVehiculoClickListener listener;

    public interface OnVehiculoClickListener {
        void onVehiculoClick(int position);
        void onVehiculoLongClick(int position);
    }

    public VehiculoAdapter(List<Vehiculo> vehiculos, OnVehiculoClickListener listener) {
        this.vehiculos = vehiculos;
        this.listener = listener;
    }

    @NonNull
    @Override
    public VehiculoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_vehiculo_lista, parent, false);
        return new VehiculoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VehiculoViewHolder holder, int position) {
        Vehiculo vehiculo = vehiculos.get(position);
        holder.tvMatricula.setText(vehiculo.getMatricula());
        holder.tvMarcaModelo.setText(vehiculo.getMarca() + " " + vehiculo.getModelo());
        holder.tvCapacidad.setText(vehiculo.getCapacidadKg() + " kg");
        holder.tvDisponible.setText(vehiculo.isDisponible() ? "Disponible" : "No disponible");

        holder.itemView.setOnClickListener(v -> listener.onVehiculoClick(position));
        holder.itemView.setOnLongClickListener(v -> {
            listener.onVehiculoLongClick(position);
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return vehiculos.size();
    }

    static class VehiculoViewHolder extends RecyclerView.ViewHolder {
        TextView tvMatricula, tvMarcaModelo, tvCapacidad, tvDisponible;

        public VehiculoViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMatricula = itemView.findViewById(R.id.tvMatricula);
            tvMarcaModelo = itemView.findViewById(R.id.tvMarcaModelo);
            tvCapacidad = itemView.findViewById(R.id.tvCapacidad);
            tvDisponible = itemView.findViewById(R.id.tvDisponible);
        }
    }
}