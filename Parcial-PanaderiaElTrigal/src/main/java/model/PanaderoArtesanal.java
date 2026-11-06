package model;

/**
 * Panadero que produce en bandejas de hasta 12 panes.
 */
public class PanaderoArtesanal extends Panadero {

    private static final int CAPACIDAD_BANDEJA = 12;
    private static final int MINUTOS_BANDEJA_TRADICIONAL = 30;
    private static final int MINUTOS_BANDEJA_INTEGRAL = 40;

    public PanaderoArtesanal(String codigo, String nombre, int minutosDisponibles) {
        super(codigo, nombre, minutosDisponibles);
    }

    @Override
    public int calcularTiempoProduccion(TipoPan tipoPan, int cantidad) {
        validarDatosProduccion(tipoPan, cantidad);

        int numeroBandejas = (cantidad + CAPACIDAD_BANDEJA - 1) / CAPACIDAD_BANDEJA;
        int minutosPorBandeja;

        if (tipoPan == TipoPan.TRADICIONAL) {
            minutosPorBandeja = MINUTOS_BANDEJA_TRADICIONAL;
        } else {
            minutosPorBandeja = MINUTOS_BANDEJA_INTEGRAL;
        }

        return numeroBandejas * minutosPorBandeja;
    }

    private void validarDatosProduccion(TipoPan tipoPan, int cantidad) {
        if (tipoPan == null) {
            throw new IllegalArgumentException("El tipo de pan es obligatorio.");
        }
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor que cero.");
        }
    }
}
