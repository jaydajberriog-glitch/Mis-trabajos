package model;

/**
 * Clase base de los empleados que participan en la producción.
 */
public abstract class Panadero extends Empleado implements Produccion {

    private int minutosDisponibles;

    public Panadero(String codigo, String nombre, int minutosDisponibles) {
        super(codigo, nombre);

        if (minutosDisponibles < 0) {
            throw new IllegalArgumentException("Los minutos disponibles no pueden ser negativos.");
        }

        this.minutosDisponibles = minutosDisponibles;
    }

    public int getMinutosDisponibles() {
        return minutosDisponibles;
    }

    public boolean tieneTiempoSuficiente(int minutos) {
        if (minutos < 0) {
            throw new IllegalArgumentException("El tiempo requerido no puede ser negativo.");
        }
        return minutosDisponibles >= minutos;
    }

    public void descontarMinutos(int minutos) {
        if (minutos <= 0) {
            throw new IllegalArgumentException("Los minutos a descontar deben ser mayores que cero.");
        }
        if (!tieneTiempoSuficiente(minutos)) {
            throw new IllegalStateException("El panadero no tiene suficientes minutos disponibles.");
        }

        minutosDisponibles -= minutos;
    }

    public void devolverMinutos(int minutos) {
        if (minutos <= 0) {
            throw new IllegalArgumentException("Los minutos a devolver deben ser mayores que cero.");
        }

        minutosDisponibles += minutos;
    }

    @Override
    public abstract int calcularTiempoProduccion(TipoPan tipoPan, int cantidad);
}
