package model;

public class Habitacion {

    private int numero;
    private String tipo;            // Individual, Doble, Suite
    private int piso;
    private int capacidadMaxima;
    private double precioPorNoche;
    private String estado;          // Disponible, Reservada, Ocupada, Mantenimiento

    public Habitacion(int numero, String tipo, int piso, int capacidadMaxima, double precioPorNoche, String estado) {
        this.numero = numero;
        this.tipo = tipo;
        this.piso = piso;
        this.capacidadMaxima = capacidadMaxima;
        this.precioPorNoche = precioPorNoche;
        this.estado = estado;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public int getPiso() {
        return piso;
    }

    public void setPiso(int piso) {
        this.piso = piso;
    }

    public int getCapacidadMaxima() {
        return capacidadMaxima;
    }

    public void setCapacidadMaxima(int capacidadMaxima) {
        this.capacidadMaxima = capacidadMaxima;
    }

    public double getPrecioPorNoche() {
        return precioPorNoche;
    }

    public void setPrecioPorNoche(double precioPorNoche) {
        this.precioPorNoche = precioPorNoche;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return "Habitación " + numero + " (" + tipo + ") - Piso " + piso + " - $" + precioPorNoche + " - Estado: " + estado;
    }
}