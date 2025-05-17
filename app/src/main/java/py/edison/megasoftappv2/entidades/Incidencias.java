package py.edison.megasoftappv2.entidades;

import com.google.firebase.Timestamp;

import java.util.Date;

public class Incidencias {
    private String id;
    private String fleteId;
    private String descripcion;  // Será: Salida, Llegada, Parada...
    private Timestamp fecha;
    private String gravedad;
    private boolean resuelta;

    public Incidencias(String fleteId, String descripcion, Date date, String leve, boolean resuelta) {
    }

    public Incidencias(String fleteId, String descripcion, Timestamp fecha, String gravedad, boolean resuelta) {
        this.fleteId = fleteId;
        this.descripcion = descripcion;
        this.fecha = fecha;
        this.gravedad = gravedad;
        this.resuelta = resuelta;


    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFleteId() {
        return fleteId;
    }

    public void setFleteId(String fleteId) {
        this.fleteId = fleteId;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Timestamp getFecha() {
        return fecha;
    }

    public void setFecha(Timestamp fecha) {
        this.fecha = fecha;
    }

    public String getGravedad() {
        return gravedad;
    }

    public void setGravedad(String gravedad) {
        this.gravedad = gravedad;
    }

    public boolean isResuelta() {
        return resuelta;
    }

    public void setResuelta(boolean resuelta) {
        this.resuelta = resuelta;
    }

}
