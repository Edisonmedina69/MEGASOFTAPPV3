package py.edison.megasoftappv2.entidades;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@IgnoreExtraProperties
public class Conductor {
    private String id;
    private String nombre;
    private String telefono;
    private String numeroLicencia;
    private String vehiculoAsignado;
    private boolean estado; // true=activo, false=inactivo
    private String fechaRegistro;
    private String fechaUltimaActualizacion;

    // Constructor vacío necesario para Firebase
    public Conductor() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault());
        this.fechaRegistro = sdf.format(new Date());
        this.estado = true;
    }

    // Constructor con parámetros básicos
    public Conductor(String nombre, String telefono, String numeroLicencia) {
        this();
        this.nombre = nombre;
        this.telefono = telefono;
        this.numeroLicencia = numeroLicencia;
    }

    @Exclude
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
        actualizarFechaModificacion();
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
        actualizarFechaModificacion();
    }

    public String getNumeroLicencia() {
        return numeroLicencia;
    }

    public void setNumeroLicencia(String numeroLicencia) {
        this.numeroLicencia = numeroLicencia;
        actualizarFechaModificacion();
    }

    public String getVehiculoAsignado() {
        return vehiculoAsignado;
    }

    public void setVehiculoAsignado(String vehiculoAsignado) {
        this.vehiculoAsignado = vehiculoAsignado;
        actualizarFechaModificacion();
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
        actualizarFechaModificacion();
    }

    public String getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(String fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public String getFechaUltimaActualizacion() {
        return fechaUltimaActualizacion;
    }

    public void setFechaUltimaActualizacion(String fechaUltimaActualizacion) {
        this.fechaUltimaActualizacion = fechaUltimaActualizacion;
    }

    @Exclude
    private void actualizarFechaModificacion() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault());
        this.fechaUltimaActualizacion = sdf.format(new Date());
    }

    @Exclude
    public String getEstadoTexto() {
        return estado ? "Activo" : "Inactivo";
    }

    @Exclude
    @Override
    public String toString() {
        return "Conductor{" +
                "id='" + id + '\'' +
                ", nombre='" + nombre + '\'' +
                ", telefono='" + telefono + '\'' +
                ", licencia='" + numeroLicencia + '\'' +
                ", vehiculo='" + vehiculoAsignado + '\'' +
                ", estado=" + getEstadoTexto() +
                ", registro='" + fechaRegistro + '\'' +
                ", última actualización='" + fechaUltimaActualizacion + '\'' +
                '}';
    }
}