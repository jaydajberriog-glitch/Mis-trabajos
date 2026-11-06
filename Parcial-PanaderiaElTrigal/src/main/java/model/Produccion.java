package model;

/**
 * Define el comportamiento de cálculo del tiempo de producción.
 */
public interface Produccion {

    int calcularTiempoProduccion(TipoPan tipoPan, int cantidad);
}
