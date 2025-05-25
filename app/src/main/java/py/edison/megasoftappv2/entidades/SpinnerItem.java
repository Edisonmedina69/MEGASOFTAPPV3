package py.edison.megasoftappv2.entidades;

public class SpinnerItem {
    private String id;
    private String descripcion;
    private int icono; // Opcional, si quieres mostrar íconos

    public SpinnerItem(String id, String descripcion) {
        this.id = id;
        this.descripcion = descripcion;
    }

    public SpinnerItem(String id, String descripcion, int icono) {
        this.id = id;
        this.descripcion = descripcion;
        this.icono = icono;
    }

    // Getters y Setters
    public String getId() {
        return id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public int getIcono() {
        return icono;
    }

    @Override
    public String toString() {
        return descripcion;
    }
}