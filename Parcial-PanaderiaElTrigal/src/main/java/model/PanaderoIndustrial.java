package model;

/**
 * Panadero que produce en lotes de hasta 30 panes.
 */
public class PanaderoIndustrial extends Panadero {

    private static final int CAPACIDAD_LOTE = 30;
    private static final int MINUTOS_PREPARACION = 20;
    private static final int MINUTOS_LOTE_TRADICIONAL = 25;
    private static final int MINUTOS_LOTE_INTEGRAL = 35;

    public PanaderoIndustrial(String codigo, String nombre, int minutosDisponibles) {
        super(codigo, nombre, minutosDisponibles);
    }

    @Override
    public int calcularTiempoProduccion(TipoPan tipoPan, int cantidad) {
        if (tipoPan == null) {
            throw new IllegalArgumentException("El tipo de pan es obligatorio.");
        }
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor que cero.");
        }

        int numeroLotes = (cantidad + CAPACIDAD_LOTE - 1) / CAPACIDAD_LOTE;
        int minutosPorLote;

        if (tipoPan == TipoPan.TRADICIONAL) {
            minutosPorLote = MINUTOS_LOTE_TRADICIONAL;
        } else {
            minutosPorLote = MINUTOS_LOTE_INTEGRAL;
        }

        return MINUTOS_PREPARACION + (numeroLotes * minutosPorLote);
    }
}
