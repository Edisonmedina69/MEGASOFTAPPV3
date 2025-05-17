package py.edison.megasoftappv2.entidades;

public class Vehiculo {
    private String id;
    private String matricula;
    private String marca;
    private String modelo;
    private int capacidadKg;
    private boolean disponible;

    // Getters y Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public int getCapacidadKg() {
        return capacidadKg;
    }

    public void setCapacidadKg(int capacidadKg) {
        this.capacidadKg = capacidadKg;
    }

    public boolean isDisponible() {
        return disponible;
    }

    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }
}