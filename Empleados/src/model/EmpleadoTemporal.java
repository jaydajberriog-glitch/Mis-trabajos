package model;

public class EmpleadoTemporal extends Empleado {
    private final int diasTrabajados;
    private final float valorDia;

    public EmpleadoTemporal(String nombre, String documento, int edad, float salarioBase,
                            CategoriaEmpleado categoria, float descuentoSalud, float descuentoPension,
                            int diasTrabajados, float valorDia) {
        super(nombre, documento, edad, salarioBase, categoria, descuentoSalud, descuentoPension);
        if (diasTrabajados < 0 || valorDia < 0)
            throw new IllegalArgumentException("Los días trabajados y el valor por día no pueden ser negativos.");
        this.diasTrabajados = diasTrabajados; this.valorDia = valorDia;
    }

    @Override public float calcularSalarioBruto() {
        return diasTrabajados * valorDia + calcularBonificacion();
    }
    @Override public String obtenerTipoEmpleado() { return "Temporal"; }
}
