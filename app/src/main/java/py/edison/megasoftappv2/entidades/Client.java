package py.edison.megasoftappv2.entidades;

import java.util.List;

public class Client {
    private String id;
    private String nombre;
    private String telefono;
    private String direccion;
    private String identidad;
    private String ruc;

    public String getRuc() {
        return ruc;
    }

    public void setRuc(String ruc) {
        this.ruc = ruc;
    }

    private List<String> historialFletes;

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

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getIdentidad() {
        return identidad;
    }

    public void setIdentidad(String identidad) {
        this.identidad = identidad;
    }

    public List<String> getHistorialFletes() {
        return historialFletes;
    }

    public void setHistorialFletes(List<String> historialFletes) {
        this.historialFletes = historialFletes;
    }
}
