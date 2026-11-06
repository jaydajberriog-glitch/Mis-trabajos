package model;

public class EmpleadoPlanta extends Empleado {
    private final String cargo;
    private final int horasExtra;
    private final float valorHoraExtra;
    private final float auxilioTransporte;

    public EmpleadoPlanta(String nombre, String documento, int edad, float salarioBase,
                          CategoriaEmpleado categoria, float descuentoSalud, float descuentoPension,
                          String cargo, int horasExtra, float valorHoraExtra, float auxilioTransporte) {
        super(nombre, documento, edad, salarioBase, categoria, descuentoSalud, descuentoPension);
        if (cargo == null || cargo.isBlank()) throw new IllegalArgumentException("El cargo no puede estar vacío.");
        if (horasExtra < 0 || valorHoraExtra < 0 || auxilioTransporte < 0)
            throw new IllegalArgumentException("Las horas extra, su valor y el auxilio no pueden ser negativos.");
        this.cargo = cargo.trim(); this.horasExtra = horasExtra;
        this.valorHoraExtra = valorHoraExtra; this.auxilioTransporte = auxilioTransporte;
    }

    @Override public float calcularSalarioBruto() {
        return salarioBase + calcularBonificacion() + horasExtra * valorHoraExtra + auxilioTransporte;
    }
    @Override public String obtenerTipoEmpleado() { return "Planta (" + cargo + ")"; }
}
