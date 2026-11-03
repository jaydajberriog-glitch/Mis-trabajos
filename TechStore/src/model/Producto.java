package model;

public class Producto {

    private String nombre;
    private final String codigo;
    private String descripcion;
    private int cantidadDisponible;
    private double valor;
    private Categoria categoria;
    private final Tienda ownedByTienda;

    public Producto(String nombre, String codigo, String descripcion,
                    int cantidadDisponible, double valor,
                    Categoria categoria, Tienda ownedByTienda) {
        this.nombre = nombre;
        this.codigo = codigo;
        this.descripcion = descripcion;
        this.cantidadDisponible = cantidadDisponible;
        this.valor = valor;
        this.categoria = categoria;
        this.ownedByTienda = ownedByTienda;
    }

    public String getNombre() {
        return nombre;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public int getCantidadDisponible() {
        return cantidadDisponible;
    }

    public double getValor() {
        return valor;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public Tienda getOwnedByTienda() {
        return ownedByTienda;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setCantidadDisponible(int cantidadDisponible) {
        this.cantidadDisponible = cantidadDisponible;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }
}