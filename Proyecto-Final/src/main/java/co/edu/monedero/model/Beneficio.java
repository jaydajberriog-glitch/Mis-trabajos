package co.edu.monedero.model;

public class Beneficio {
    private final String nombre;
    private final int puntosNecesarios;
    private final int bonoSaldo;

    public Beneficio(String nombre, int puntosNecesarios, int bonoSaldo) {
        if (nombre == null || nombre.isBlank() || puntosNecesarios <= 0 || bonoSaldo < 0)
            throw new IllegalArgumentException("Datos del beneficio no válidos.");
        this.nombre = nombre; this.puntosNecesarios = puntosNecesarios; this.bonoSaldo = bonoSaldo;
    }
    public String getNombre() { return nombre; }
    public int getPuntosNecesarios() { return puntosNecesarios; }
    public int getBonoSaldo() { return bonoSaldo; }
    @Override public String toString() { return nombre + " (" + puntosNecesarios + " puntos)"; }
}
