package co.edu.uniquindio.poo.empleadosjfx.model;

public enum CategoriaEmpleado {
    JUNIOR(1.0),
    MID(1.15),
    SENIOR(1.3);

    private final double multiplicador;

    CategoriaEmpleado(double multiplicador) {
        this.multiplicador = multiplicador;
    }

    public double getMultiplicador() {
        return multiplicador;
    }
}
