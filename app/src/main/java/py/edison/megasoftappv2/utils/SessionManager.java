package py.edison.megasoftappv2.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {
    private final SharedPreferences prefs;

    public SessionManager(Context context) {
        prefs = context.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);
    }

    public void guardarUsuario(String email) {
        prefs.edit().putString("user_email", email).apply();
    }

    public String obtenerUsuario() {
        return prefs.getString("user_email", null);
    }
}