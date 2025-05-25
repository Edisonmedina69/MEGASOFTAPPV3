package py.edison.megasoftappv2.entidades;

public class IncidenciaRegistrada {
    private String descripcion;
    private String fechaHora;

    public IncidenciaRegistrada() {}

    public IncidenciaRegistrada(String descripcion, String fechaHora) {
        this.descripcion = descripcion;
        this.fechaHora = fechaHora;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(String fechaHora) {
        this.fechaHora = fechaHora;
    }
}
