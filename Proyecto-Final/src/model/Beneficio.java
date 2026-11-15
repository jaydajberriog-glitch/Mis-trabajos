package model;

import exception.OperacionInvalidaException;

public class Beneficio {
    private final String nombre;
    private final int puntosNecesarios;
    private final int bonoSaldo;
    private final double descuentoComision;

    public Beneficio(String nombre, int puntosNecesarios, int bonoSaldo) {
        this(nombre, puntosNecesarios, bonoSaldo, 0.0);
    }

    public Beneficio(String nombre, int puntosNecesarios, int bonoSaldo, double descuentoComision) {
        if (nombre == null || nombre.isBlank() || puntosNecesarios <= 0 || bonoSaldo < 0)
            throw new OperacionInvalidaException("Datos del beneficio no válidos.");
        this.nombre = nombre;
        this.puntosNecesarios = puntosNecesarios;
        this.bonoSaldo = bonoSaldo;
        this.descuentoComision = descuentoComision;
    }

    public String getNombre() { return nombre; }
    public int getPuntosNecesarios() { return puntosNecesarios; }
    public int getBonoSaldo() { return bonoSaldo; }
    public double getDescuentoComision() { return descuentoComision; }

    @Override
    public String toString() {
        return nombre + " (" + puntosNecesarios + " puntos)";
    }
}
