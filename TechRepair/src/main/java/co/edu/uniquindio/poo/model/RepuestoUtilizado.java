package co.edu.uniquindio.poo.model;
public class RepuestoUtilizado {
    private Repuesto repuesto; private int cantidad; private double precioUnitarioAplicado;
    public RepuestoUtilizado(Repuesto repuesto,int cantidad){if(repuesto==null||cantidad<=0)throw new IllegalArgumentException("Repuesto y cantidad válidos son obligatorios.");this.repuesto=repuesto;this.cantidad=cantidad;this.precioUnitarioAplicado=repuesto.getPrecioUnitario();}
    public Repuesto getRepuesto(){return repuesto;}public int getCantidad(){return cantidad;}public double getPrecioUnitarioAplicado(){return precioUnitarioAplicado;}public double calcularSubtotal(){return cantidad*precioUnitarioAplicado;}
    @Override public String toString(){return repuesto.getNombre()+" x "+cantidad+" = $"+String.format("%.0f",calcularSubtotal());}
}
