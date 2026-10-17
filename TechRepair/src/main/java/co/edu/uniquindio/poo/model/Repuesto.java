package co.edu.uniquindio.poo.model;
public class Repuesto {
    private String codigo,nombre,descripcion; private double precioUnitario; private int cantidadDisponible; private CategoriaRepuesto categoria;
    public Repuesto(String codigo,String nombre,String descripcion,double precio,int cantidad,CategoriaRepuesto categoria){
        if(vacio(codigo)||vacio(nombre)||vacio(descripcion)||precio<0||cantidad<0||categoria==null)throw new IllegalArgumentException("Datos del repuesto inválidos.");
        this.codigo=codigo.trim();this.nombre=nombre.trim();this.descripcion=descripcion.trim();this.precioUnitario=precio;this.cantidadDisponible=cantidad;this.categoria=categoria;
    }
    private boolean vacio(String s){return s==null||s.trim().isEmpty();}
    public String getCodigo(){return codigo;}public String getNombre(){return nombre;}public String getDescripcion(){return descripcion;}public double getPrecioUnitario(){return precioUnitario;}public int getCantidadDisponible(){return cantidadDisponible;}public CategoriaRepuesto getCategoria(){return categoria;}
    public void descontar(int cantidad){if(cantidad<=0)throw new IllegalArgumentException("La cantidad debe ser mayor que cero.");if(cantidad>cantidadDisponible)throw new IllegalArgumentException("No hay suficientes unidades disponibles.");cantidadDisponible-=cantidad;}
    public void devolver(int cantidad){if(cantidad>0)cantidadDisponible+=cantidad;}
    @Override public String toString(){return codigo+" - "+nombre+" | $"+String.format("%.0f",precioUnitario)+" | Existencias: "+cantidadDisponible+" | "+categoria;}
}
