package co.edu.uniquindio.poo.empleadosjfx.model;

public class EmpleadoTemporal extends Empleado {
    private int horasTrabajadas;
    private double valorHora;

    public EmpleadoTemporal(String id, String nombre, CategoriaEmpleado categoria, int horasTrabajadas, double valorHora) {
        super(id, nombre, categoria);
        this.horasTrabajadas = horasTrabajadas;
        this.valorHora = valorHora;
    }

    @Override
    public double calcularSalarioTotal() {
        return (horasTrabajadas * valorHora) * categoria.getMultiplicador();
    }

    @Override
    public String getTipoEmpleado() { return "Temporal"; }

    public int getHorasTrabajadas() { return horasTrabajadas; }
    public void setHorasTrabajadas(int horasTrabajadas) { this.horasTrabajadas = horasTrabajadas; }

    public double getValorHora() { return valorHora; }
    public void setValorHora(double valorHora) { this.valorHora = valorHora; }
}
