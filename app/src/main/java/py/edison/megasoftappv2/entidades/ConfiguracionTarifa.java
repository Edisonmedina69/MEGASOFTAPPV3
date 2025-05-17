package py.edison.megasoftappv2.entidades;

public class ConfiguracionTarifa {
    private double precioPorKm;
    private double precioPorKg;
    private double recargoUrgente; // Porcentaje

    public ConfiguracionTarifa(double precioPorKm, double precioPorKg, double recargoUrgente) {
        this.precioPorKm = precioPorKm;
        this.precioPorKg = precioPorKg;
        this.recargoUrgente = recargoUrgente;
    }

    public double getPrecioPorKm() {
        return precioPorKm;
    }

    public void setPrecioPorKm(double precioPorKm) {
        this.precioPorKm = precioPorKm;
    }

    public double getPrecioPorKg() {
        return precioPorKg;
    }

    public void setPrecioPorKg(double precioPorKg) {
        this.precioPorKg = precioPorKg;
    }

    public double getRecargoUrgente() {
        return recargoUrgente;
    }

    public void setRecargoUrgente(double recargoUrgente) {
        this.recargoUrgente = recargoUrgente;
    }

    public double aplicarRecargoSiUrgente(double precioBase, boolean esUrgente) {
        return esUrgente ? precioBase * (1 + recargoUrgente / 100) : precioBase;
    }
}