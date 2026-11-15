package model;

import exceptions.OperacionInvalidaException;

public class Beneficio {
    private final String nombre;
    private final int puntosNecesarios;
    private final int bonoSaldo;
    private final double descuentoComision;
    private final int diasSinCargosRetiro;

    public Beneficio(String nombre, int puntosNecesarios, int bonoSaldo) {
        this(nombre, puntosNecesarios, bonoSaldo, 0.0, 0);
    }

    public Beneficio(String nombre, int puntosNecesarios, int bonoSaldo, double descuentoComision) {
        this(nombre, puntosNecesarios, bonoSaldo, descuentoComision, 0);
    }

    public Beneficio(String nombre, int puntosNecesarios, int bonoSaldo, double descuentoComision, int diasSinCargosRetiro) {
        if (nombre == null || nombre.isBlank() || puntosNecesarios <= 0 || bonoSaldo < 0
                || descuentoComision < 0 || descuentoComision > 1 || diasSinCargosRetiro < 0)
            throw new OperacionInvalidaException("Datos del beneficio no válidos.");
        this.nombre = nombre;
        this.puntosNecesarios = puntosNecesarios;
        this.bonoSaldo = bonoSaldo;
        this.descuentoComision = descuentoComision;
        this.diasSinCargosRetiro = diasSinCargosRetiro;
    }

    public String getNombre() { return nombre; }
    public int getPuntosNecesarios() { return puntosNecesarios; }
    public int getBonoSaldo() { return bonoSaldo; }
    public double getDescuentoComision() { return descuentoComision; }
    public int getDiasSinCargosRetiro() { return diasSinCargosRetiro; }

    @Override
    public String toString() {
        return nombre + " (" + puntosNecesarios + " puntos)";
    }
}
