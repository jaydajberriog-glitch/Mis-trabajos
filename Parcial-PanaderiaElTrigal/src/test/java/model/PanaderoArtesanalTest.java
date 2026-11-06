package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PanaderoArtesanalTest {

    @Test
    void calculaUnaBandejaTradicional() {
        PanaderoArtesanal panadero = new PanaderoArtesanal("PA1", "Carlos", 200);
        assertEquals(30, panadero.calcularTiempoProduccion(TipoPan.TRADICIONAL, 12));
    }

    @Test
    void bandejaIncompletaCuentaComoCompleta() {
        PanaderoArtesanal panadero = new PanaderoArtesanal("PA1", "Carlos", 200);
        assertEquals(60, panadero.calcularTiempoProduccion(TipoPan.TRADICIONAL, 13));
    }

    @Test
    void calculaPanIntegral() {
        PanaderoArtesanal panadero = new PanaderoArtesanal("PA1", "Carlos", 200);
        assertEquals(80, panadero.calcularTiempoProduccion(TipoPan.INTEGRAL, 13));
    }

    @Test
    void rechazaCantidadInvalida() {
        PanaderoArtesanal panadero = new PanaderoArtesanal("PA1", "Carlos", 200);
        assertThrows(IllegalArgumentException.class,
                () -> panadero.calcularTiempoProduccion(TipoPan.TRADICIONAL, 0));
    }
}
