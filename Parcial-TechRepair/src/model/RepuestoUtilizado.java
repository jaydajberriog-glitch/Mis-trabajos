package model;

public record RepuestoUtilizado(Repuesto repuesto, int cantidad, double precioUnitarioAplicado) {
    public RepuestoUtilizado {
        if (repuesto == null || cantidad <= 0) {
            throw new IllegalArgumentException("Repuesto y cantidad válidos son obligatorios.");
        }
    }

    public RepuestoUtilizado(Repuesto repuesto, int cantidad) {
        this(repuesto, cantidad, repuesto.getPrecioUnitario());
    }

    public double calcularSubtotal() {
        return cantidad * precioUnitarioAplicado;
    }

    @Override
    public String toString() {
        return repuesto.getNombre() + " x " + cantidad + " = $" + String.format("%.0f", calcularSubtotal());
    }
}
