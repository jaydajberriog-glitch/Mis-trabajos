package co.edu.monedero.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Cliente {
    private final String documento;
    private String nombre;
    private String telefono;
    private String correo;
    private final List<Monedero> monederos = new ArrayList<>();
    private int puntos;

    public Cliente(String documento, String nombre, String telefono, String correo) {
        if (documento == null || documento.isBlank() || nombre == null || nombre.isBlank())
            throw new IllegalArgumentException("Documento y nombre son obligatorios.");
        this.documento = documento.trim();
        this.nombre = nombre.trim();
        this.telefono = telefono == null ? "" : telefono.trim();
        this.correo = correo == null ? "" : correo.trim();
    }
    public void agregarMonedero(Monedero monedero) { monederos.add(monedero); }
    public void sumarPuntos(int cantidad) {
        if (cantidad < 0) throw new IllegalArgumentException("Los puntos no pueden ser negativos.");
        puntos += cantidad;
    }
    public void restarPuntos(int cantidad) {
        if (cantidad < 0 || puntos < cantidad) throw new IllegalArgumentException("Puntos insuficientes.");
        puntos -= cantidad;
    }
    public String getDocumento() { return documento; }
    public String getNombre() { return nombre; }
    public String getTelefono() { return telefono; }
    public String getCorreo() { return correo; }
    public List<Monedero> getMonederos() { return Collections.unmodifiableList(monederos); }
    public int getPuntos() { return puntos; }
    public Rango getRango() { return Rango.desdePuntos(puntos); }
    public void setNombre(String nombre) {
        if (nombre == null || nombre.isBlank()) throw new IllegalArgumentException("El nombre es obligatorio.");
        this.nombre = nombre.trim();
    }
    public void setTelefono(String telefono) { this.telefono = telefono == null ? "" : telefono.trim(); }
    public void setCorreo(String correo) { this.correo = correo == null ? "" : correo.trim(); }
    @Override public String toString() { return nombre + " (" + documento + ")"; }
}
