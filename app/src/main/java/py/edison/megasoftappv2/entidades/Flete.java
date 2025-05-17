package py.edison.megasoftappv2.entidades;

import java.util.List;

public class Flete {

    // Estados posibles
    public static final String PENDIENTE = "PENDIENTE";
    public static final String EN_PROCESO = "EN_PROCESO";
    public static final String ENTREGADO = "ENTREGADO";
    public static final String CANCELADO = "CANCELADO";

    private String id;
    private String origen;
    private String destino;
    private String fechaSalida;
    private String fechaEntrega;
    private String firmaDestinatario;
    private String fotoEntrega;
    private double distancia;
    private double peso;
    private double tarifa;
    private String estado;
    private boolean urgente;
    private List<Incidencias> incidencias;

    // Estados permitidos
    public boolean puedeCambiarA(String nuevoEstado) {
        switch(this.estado) {
            case PENDIENTE:
                return EN_PROCESO.equals(nuevoEstado) || CANCELADO.equals(nuevoEstado);
            case EN_PROCESO:
                return ENTREGADO.equals(nuevoEstado) || CANCELADO.equals(nuevoEstado);
            default:
                return false;
        }
    }

    // Getters y Setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrigen() {
        return origen;
    }

    public void setOrigen(String origen) {
        this.origen = origen;
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public String getFechaSalida() {
        return fechaSalida;
    }

    public void setFechaSalida(String fechaSalida) {
        this.fechaSalida = fechaSalida;
    }

    public String getFechaEntrega() {
        return fechaEntrega;
    }

    public void setFechaEntrega(String fechaEntrega) {
        this.fechaEntrega = fechaEntrega;
    }

    public String getFirmaDestinatario() {
        return firmaDestinatario;
    }

    public void setFirmaDestinatario(String firmaDestinatario) {
        this.firmaDestinatario = firmaDestinatario;
    }

    public String getFotoEntrega() {
        return fotoEntrega;
    }

    public void setFotoEntrega(String fotoEntrega) {
        this.fotoEntrega = fotoEntrega;
    }

    public double getDistancia() {
        return distancia;
    }

    public void setDistancia(double distancia) {
        this.distancia = distancia;
    }

    public double getPeso() {
        return peso;
    }

    public void setPeso(double peso) {
        this.peso = peso;
    }

    public double getTarifa() {
        return tarifa;
    }

    public void setTarifa(double tarifa) {
        this.tarifa = tarifa;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public boolean isUrgente() {
        return urgente;
    }

    public void setUrgente(boolean urgente) {
        this.urgente = urgente;
    }

    public List<Incidencias> getIncidencias() {
        return incidencias;
    }

    public void setIncidencias(List<Incidencias> incidencias) {
        this.incidencias = incidencias;
    }
}
