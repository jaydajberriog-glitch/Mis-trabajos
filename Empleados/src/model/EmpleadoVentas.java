package model;

public class EmpleadoVentas extends Empleado {
    private final float totalVentas;
    private final float porcentajeComision;

    public EmpleadoVentas(String nombre, String documento, int edad, float salarioBase,
                          CategoriaEmpleado categoria, float descuentoSalud, float descuentoPension,
                          float totalVentas, float porcentajeComision) {
        super(nombre, documento, edad, salarioBase, categoria, descuentoSalud, descuentoPension);
        if (totalVentas < 0) throw new IllegalArgumentException("Las ventas no pueden ser negativas.");
        if (porcentajeComision < 0 || porcentajeComision > 100)
            throw new IllegalArgumentException("La comisión debe estar entre 0 y 100.");
        this.totalVentas = totalVentas; this.porcentajeComision = porcentajeComision;
    }

    @Override public float calcularSalarioBruto() {
        return salarioBase + calcularBonificacion() + totalVentas * porcentajeComision / 100f;
    }
    @Override public String obtenerTipoEmpleado() { return "Ventas"; }
}
