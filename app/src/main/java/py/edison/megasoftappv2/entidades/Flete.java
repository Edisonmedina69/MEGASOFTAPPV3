package py.edison.megasoftappv2.entidades;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.Timestamp;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.List;

@IgnoreExtraProperties
public class Flete implements Parcelable {
    public static final String PENDIENTE = "PENDIENTE";
    public static final String EN_PROCESO = "EN_PROCESO";
    public static final String COMPLETADO = "COMPLETADO";
    public static final String CANCELADO = "CANCELADO";

    private String id;
    private String clienteId;
    private String conductorId;
    private String vehiculoId;
    private String adminId;
    private String tipoMercanciaId;
    private String origen;
    private String destino;
    private Long fechaSalida; // Cambiado de long a Long
    private Timestamp fechaEntrega;
    private double distancia;
    private double peso;
    private double tarifa;
    private double tarifaTotal;
    private String estado;
    private boolean urgente;
    private List<String> incidenciasIds;

    public Flete() {
        this.estado = PENDIENTE;
        this.urgente = false;
        Log.d("Flete", "Nueva instancia de Flete creada");
    }

    protected Flete(Parcel in) {
        id = in.readString();
        clienteId = in.readString();
        conductorId = in.readString();
        vehiculoId = in.readString();
        adminId = in.readString();
        tipoMercanciaId = in.readString();
        origen = in.readString();
        destino = in.readString();
        if (in.readByte() == 0) {
            fechaSalida = null;
        } else {
            fechaSalida = in.readLong();
        }
        fechaEntrega = in.readParcelable(Timestamp.class.getClassLoader());
        distancia = in.readDouble();
        peso = in.readDouble();
        tarifa = in.readDouble();
        tarifaTotal = in.readDouble();
        estado = in.readString();
        urgente = in.readByte() != 0;
        incidenciasIds = in.createStringArrayList();
    }

    public static final Creator<Flete> CREATOR = new Creator<Flete>() {
        @Override
        public Flete createFromParcel(Parcel in) {
            return new Flete(in);
        }

        @Override
        public Flete[] newArray(int size) {
            return new Flete[size];
        }
    };

    public Long getFechaSalida() {
        return fechaSalida;
    }

    public void setFechaSalida(Long fechaSalida) {
        this.fechaSalida = fechaSalida;
    }

    public Timestamp getFechaEntrega() {
        return fechaEntrega;
    }

    public void setFechaEntrega(Timestamp fechaEntrega) {
        this.fechaEntrega = fechaEntrega;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClienteId() {
        return clienteId;
    }

    public void setClienteId(String clienteId) {
        this.clienteId = clienteId;
    }

    public String getConductorId() {
        return conductorId;
    }

    public void setConductorId(String conductorId) {
        this.conductorId = conductorId;
    }

    public String getVehiculoId() {
        return vehiculoId;
    }

    public void setVehiculoId(String vehiculoId) {
        this.vehiculoId = vehiculoId;
    }

    public String getAdminId() {
        return adminId;
    }

    public void setAdminId(String adminId) {
        this.adminId = adminId;
    }

    public String getTipoMercanciaId() {
        return tipoMercanciaId;
    }

    public void setTipoMercanciaId(String tipoMercanciaId) {
        this.tipoMercanciaId = tipoMercanciaId;
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

    public double getTarifaTotal() {
        return tarifaTotal;
    }

    public void setTarifaTotal(double tarifaTotal) {
        this.tarifaTotal = tarifaTotal;
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

    public List<String> getIncidenciasIds() {
        return incidenciasIds;
    }

    public void setIncidenciasIds(List<String> incidenciasIds) {
        this.incidenciasIds = incidenciasIds;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(clienteId);
        dest.writeString(conductorId);
        dest.writeString(vehiculoId);
        dest.writeString(adminId);
        dest.writeString(tipoMercanciaId);
        dest.writeString(origen);
        dest.writeString(destino);

        if (fechaSalida == null) {
            dest.writeByte((byte) 0); // flag para null
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(fechaSalida);
        }

        dest.writeParcelable(fechaEntrega, flags);
        dest.writeDouble(distancia);
        dest.writeDouble(peso);
        dest.writeDouble(tarifa);
        dest.writeDouble(tarifaTotal);
        dest.writeString(estado);
        dest.writeByte((byte) (urgente ? 1 : 0));
        dest.writeStringList(incidenciasIds);
    }
}
