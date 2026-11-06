package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PanaderoIndustrialTest {

    @Test
    void incluyePreparacionYUnLoteTradicional() {
        PanaderoIndustrial panadero = new PanaderoIndustrial("PI1", "María", 200);
        assertEquals(45, panadero.calcularTiempoProduccion(TipoPan.TRADICIONAL, 30));
    }

    @Test
    void dosLotesTradicionalesIncluyenUnaSolaPreparacion() {
        PanaderoIndustrial panadero = new PanaderoIndustrial("PI1", "María", 200);
        assertEquals(70, panadero.calcularTiempoProduccion(TipoPan.TRADICIONAL, 31));
    }

    @Test
    void calculaLoteIntegral() {
        PanaderoIndustrial panadero = new PanaderoIndustrial("PI1", "María", 200);
        assertEquals(55, panadero.calcularTiempoProduccion(TipoPan.INTEGRAL, 30));
    }
}
