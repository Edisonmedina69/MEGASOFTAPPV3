package py.edison.megasoftappv2.entidades;

import androidx.annotation.NonNull;

import java.util.HashMap;
import java.util.Map;

public class TipoMercancia {
    private String id;
    private String descripcion;

    // Constructor vacío necesario para Firebase
    public TipoMercancia() {}

    public TipoMercancia(String descripcion) {
        this.descripcion = descripcion;
    }

    // Getters y Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    // Método toString() mejorado
    @NonNull
    @Override
    public String toString() {
        return descripcion;
    }

    // Método para convertir a Map (útil para Firebase)
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("descripcion", descripcion);
        return map;
    }

    // Método estático para crear desde un Map (opcional)
    public static TipoMercancia fromMap(Map<String, Object> map) {
        TipoMercancia tipo = new TipoMercancia();
        if (map.containsKey("id")) {
            tipo.id = (String) map.get("id");
        }
        if (map.containsKey("descripcion")) {
            tipo.descripcion = (String) map.get("descripcion");
        }
        return tipo;
    }
}