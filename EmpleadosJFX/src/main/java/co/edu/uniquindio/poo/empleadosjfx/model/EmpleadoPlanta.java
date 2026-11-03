package co.edu.uniquindio.poo.empleadosjfx.model;

public class EmpleadoPlanta extends Empleado {
    private double salarioBase;
    private double bonificacionFija;

    public EmpleadoPlanta(String id, String nombre, CategoriaEmpleado categoria, double salarioBase, double bonificacionFija) {
        super(id, nombre, categoria);
        this.salarioBase = salarioBase;
        this.bonificacionFija = bonificacionFija;
    }

    @Override
    public double calcularSalarioTotal() {
        return (salarioBase * categoria.getMultiplicador()) + bonificacionFija;
    }

    @Override
    public String getTipoEmpleado() { return "Planta"; }

    public double getSalarioBase() { return salarioBase; }
    public void setSalarioBase(double salarioBase) { this.salarioBase = salarioBase; }

    public double getBonificacionFija() { return bonificacionFija; }
    public void setBonificacionFija(double bonificacionFija) { this.bonificacionFija = bonificacionFija; }
}
