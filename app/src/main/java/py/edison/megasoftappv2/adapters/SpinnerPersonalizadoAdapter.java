package py.edison.megasoftappv2.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import py.edison.megasoftappv2.R;
import py.edison.megasoftappv2.activities.Fletes.CrearFletePaso2Activity.SpinnerItem;

public class SpinnerPersonalizadoAdapter extends ArrayAdapter<SpinnerItem> {

    private LayoutInflater inflater;

    public SpinnerPersonalizadoAdapter(@NonNull Context context, @NonNull List<SpinnerItem> items) {
        super(context, 0, items);
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return createView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return createView(position, convertView, parent);
    }

    private View createView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_spinner_personalizado, parent, false);
        }

        ImageView icon = convertView.findViewById(R.id.iconSpinner);
        TextView text = convertView.findViewById(R.id.textSpinner);

        SpinnerItem item = getItem(position);
        if (item != null) {
            text.setText(item.getText());
            if (item.getIconRes() != 0) {
                icon.setImageResource(item.getIconRes());
                icon.setVisibility(View.VISIBLE);
            } else {
                icon.setVisibility(View.GONE);
            }
        }

        return convertView;
    }
}