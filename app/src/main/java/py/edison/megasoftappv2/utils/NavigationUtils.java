package py.edison.megasoftappv2.utils;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import py.edison.megasoftappv2.activities.MainActivity;

public class NavigationUtils {
    public static void safeNavigate(Context context, Class<?> destination) {
        try {
            Intent intent = new Intent(context, destination);
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            context.startActivity(intent);
        } catch (Exception e) {
            Log.e("NAV_ERROR", "Falló navegación a " + destination.getSimpleName(), e);
            Toast.makeText(context, "Error al cargar pantalla", Toast.LENGTH_SHORT).show();

            // Reinicia la app como fallback
            Intent restart = new Intent(context, MainActivity.class);
            restart.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            context.startActivity(restart);
        }
    }
}