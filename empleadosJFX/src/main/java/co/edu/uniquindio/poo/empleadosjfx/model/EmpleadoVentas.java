package co.edu.uniquindio.poo.empleadosjfx.model;

public class EmpleadoVentas extends Empleado {
    private double salarioBase;
    private double totalVentas;
    private double porcentajeComision;

    public EmpleadoVentas(String id, String nombre, CategoriaEmpleado categoria, double salarioBase, double totalVentas, double porcentajeComision) {
        super(id, nombre, categoria);
        this.salarioBase = salarioBase;
        this.totalVentas = totalVentas;
        this.porcentajeComision = porcentajeComision;
    }

    @Override
    public double calcularSalarioTotal() {
        return (salarioBase * categoria.getMultiplicador()) + (totalVentas * porcentajeComision);
    }

    @Override
    public String getTipoEmpleado() { return "Ventas"; }

    public double getSalarioBase() { return salarioBase; }
    public void setSalarioBase(double salarioBase) { this.salarioBase = salarioBase; }

    public double getTotalVentas() { return totalVentas; }
    public void setTotalVentas(double totalVentas) { this.totalVentas = totalVentas; }

    public double getPorcentajeComision() { return porcentajeComision; }
    public void setPorcentajeComision(double porcentajeComision) { this.porcentajeComision = porcentajeComision; }
}
