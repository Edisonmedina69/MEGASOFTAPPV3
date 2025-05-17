package py.edison.megasoftappv2.entidades;

public class TipoMercancia {
    private String id;
    private String nombre;
    private String descripcion;
    private double tarifaEspecial;

    // Getters y Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public double getTarifaEspecial() {
        return tarifaEspecial;
    }

    public void setTarifaEspecial(double tarifaEspecial) {
        this.tarifaEspecial = tarifaEspecial;
    }
}