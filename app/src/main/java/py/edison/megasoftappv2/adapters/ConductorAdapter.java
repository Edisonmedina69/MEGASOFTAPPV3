package py.edison.megasoftappv2.adapters;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import py.edison.megasoftappv2.entidades.Client;
import py.edison.megasoftappv2.entidades.Conductor;

public class ConductorAdapter {

    private List<Conductor> conductors;
    private ConductorAdapter.ItemClickListener clickListener;

    @NonNull
  //  @Override
    public ConductorAdapter.ConductorViewHoler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    // @Override
    public void onBindViewHolder(@NonNull ClientAdapter.ClientViewHolder holder, int position) {

    }

    public interface ItemClickListener {

        void onItemClick(Conductor conductor);
        void onEditClick(Conductor conductor);
        void onDeleteClick(Conductor conductor);
}
    public void setOnItemClickListener(ConductorAdapter.ItemClickListener listener) {
        this.clickListener = listener;
    }

    public class ConductorViewHoler extends RecyclerView.ViewHolder {
        public ConductorViewHoler(@NonNull View itemView) {
            super(itemView);
        }
    }
}
