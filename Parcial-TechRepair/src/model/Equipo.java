package model;

public abstract class Equipo {
    public enum TipoEquipo { PORTATIL, ESCRITORIO, TELEFONO_INTELIGENTE }

    private String codigo;
    private String numeroSerie;
    private String marca;
    private String modelo;
    private int anioFabricacion;
    private String estadoFisico;
    private Cliente cliente;

    public Equipo(String codigo, String numeroSerie, String marca, String modelo, int anioFabricacion, String estadoFisico, Cliente cliente) {
        if (vacio(codigo) || vacio(numeroSerie) || vacio(marca) || vacio(modelo) || vacio(estadoFisico) || cliente == null) {
            throw new IllegalArgumentException("Los datos del equipo son obligatorios.");
        }
        if (anioFabricacion < 1970 || anioFabricacion > java.time.Year.now().getValue()) {
            throw new IllegalArgumentException("Año de fabricación no válido.");
        }
        this.codigo = codigo.trim();
        this.numeroSerie = numeroSerie.trim();
        this.marca = marca.trim();
        this.modelo = modelo.trim();
        this.anioFabricacion = anioFabricacion;
        this.estadoFisico = estadoFisico.trim();
        this.cliente = cliente;
    }

    private boolean vacio(String s) {
        return s == null || s.trim().isEmpty();
    }

    public abstract TipoEquipo getTipoEquipo();

    public String getCodigo() { return codigo; }
    public String getNumeroSerie() { return numeroSerie; }
    public String getMarca() { return marca; }
    public String getModelo() { return modelo; }
    public int getAnioFabricacion() { return anioFabricacion; }
    public String getEstadoFisico() { return estadoFisico; }
    public Cliente getCliente() { return cliente; }

    @Override
    public String toString() {
        return codigo + " | " + getTipoEquipo() + " | " + marca + " " + modelo + " | Serie: " + numeroSerie + " | Cliente: " + cliente.getNombreCompleto();
    }
}
