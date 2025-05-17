    package py.edison.megasoftappv2.fragments.Fletes;

    import android.os.Bundle;
    import android.view.LayoutInflater;
    import android.view.View;
    import android.view.ViewGroup;
    import android.widget.CheckBox;
    import android.widget.EditText;

    import androidx.fragment.app.Fragment;

    import py.edison.megasoftappv2.R;
    import py.edison.megasoftappv2.servicios.FleteService;

    public class FleteFormFragment extends Fragment {
        private EditText etOrigen, etDestino, etDistancia, etPeso, etTarifa;
        private CheckBox cbUrgente;
        private FleteService fleteService;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_flete_form, container, false);

            // Inicializar vistas
            inicializarVistas(view);

            fleteService = new FleteService();

            return view;
        }

        private void inicializarVistas(View view) {
            etOrigen = view.findViewById(R.id.etOrigen);
            etDestino = view.findViewById(R.id.etDestino);
            etDistancia = view.findViewById(R.id.etDistancia);
            etPeso = view.findViewById(R.id.etPeso);
            etTarifa = view.findViewById(R.id.etTarifa);
            cbUrgente = view.findViewById(R.id.cbUrgente);

            view.findViewById(R.id.btnGuardarFlete).setOnClickListener(v -> guardarFlete());
        }

        private void guardarFlete() {
            // Validaciones y lógica de guardado
            // Similar a tu CrearFleteActivity
        }
    }