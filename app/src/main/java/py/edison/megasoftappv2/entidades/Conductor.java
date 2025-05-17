package py.edison.megasoftappv2.entidades;

public class Conductor {
    private String id;
    private String nombre;
    private String telefono;
    private String numeroLicencia;
    private String vehiculoAsignado;
    private boolean estado; // true=activo, false=inactivo
    private String fechaRegistro;

    // Constructor vacío necesario para Firebase y otras librerías
    public Conductor() {
    }

    // Constructor con parámetros básicos
    public Conductor(String nombre, String telefono, String numeroLicencia) {
        this.nombre = nombre;
        this.telefono = telefono;
        this.numeroLicencia = numeroLicencia;
        this.estado = true; // Por defecto activo al crear
    }

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

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getNumeroLicencia() {
        return numeroLicencia;
    }

    public void setNumeroLicencia(String numeroLicencia) {
        this.numeroLicencia = numeroLicencia;
    }

    public String getVehiculoAsignado() {
        return vehiculoAsignado;
    }

    public void setVehiculoAsignado(String vehiculoAsignado) {
        this.vehiculoAsignado = vehiculoAsignado;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public String getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(String fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    // Método toString para debugging
    @Override
    public String toString() {
        return "Conductor{" +
                "id='" + id + '\'' +
                ", nombre='" + nombre + '\'' +
                ", telefono='" + telefono + '\'' +
                ", numeroLicencia='" + numeroLicencia + '\'' +
                ", vehiculoAsignado='" + vehiculoAsignado + '\'' +
                ", estado=" + estado +
                ", fechaRegistro='" + fechaRegistro + '\'' +
                '}';
    }
}