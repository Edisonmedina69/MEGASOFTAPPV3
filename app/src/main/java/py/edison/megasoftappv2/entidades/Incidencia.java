package py.edison.megasoftappv2.entidades;

public class Incidencia {
    private final String fechaHora;
    private String id;
    private String descripcion;
    private String texto;
    private String estado;

//    public Incidencia(String fleteId, String descripcion, String s, String fechaHora) {}

    public Incidencia(String id, String descripcion, String fechaHora) {
        this.id = id;

        this.descripcion = descripcion;
        this.fechaHora = fechaHora;
    }


    // Getters y setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getTexto() { return texto; }
    public void setTexto(String texto) { this.texto = texto; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}
