package model;

import java.util.List;

public abstract class Servicio {
    private String codigo;
    private String nombre;
    private String descripcion;
    private double tarifaBase;

    public Servicio(String codigo, String nombre, String descripcion, double tarifaBase) {
        if (vacio(codigo) || vacio(nombre) || vacio(descripcion) || tarifaBase < 0) {
            throw new IllegalArgumentException("Datos del servicio inválidos.");
        }
        this.codigo = codigo.trim();
        this.nombre = nombre.trim();
        this.descripcion = descripcion.trim();
        this.tarifaBase = tarifaBase;
    }

    private boolean vacio(String s) {
        return s == null || s.trim().isEmpty();
    }

    public String getCodigo() { return codigo; }
    public String getNombre() { return nombre; }
    public String getDescripcion() { return descripcion; }
    public double getTarifaBase() { return tarifaBase; }

    public abstract double calcularCosto(Equipo equipo, double horas, List<RepuestoUtilizado> repuestos);

    @Override
    public String toString() {
        return codigo + " - " + nombre;
    }
}
