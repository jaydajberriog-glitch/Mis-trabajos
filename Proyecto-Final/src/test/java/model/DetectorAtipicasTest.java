package model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DetectorAtipicasTest {
    private final DetectorAtipicas detector = new DetectorAtipicas();
    private final Cliente cliente = new Cliente("1", "Ana", "300", "a@b.com");
    private final Monedero monedero = new Monedero("M1", TipoMonedero.AHORROS);

    private List<Transaccion> retiros(String... montos) {
        List<Transaccion> lista = new ArrayList<>();
        int i = 0;
        for (String m : montos) lista.add(new Retiro("T" + i++, new BigDecimal(m), monedero, cliente));
        return lista;
    }

    @Test
    void calculaPromedioYDesviacionPoblacional() {
        var h = retiros("2", "4", "4", "4", "5", "5", "7", "9");
        assertEquals(5.0, detector.promedio(h), 1e-9);
        assertEquals(2.0, detector.desviacion(h), 1e-9);
        assertEquals(9.0, detector.limite(h), 1e-9);
    }

    @Test
    void detectaSoloLosMontosQueSuperanPromedioMasDosDesviaciones() {
        var h = retiros("100", "100", "100", "100", "100", "100", "1000");
        var atipicas = detector.detectar(h);
        assertEquals(1, atipicas.size());
        assertEquals(0, atipicas.get(0).getMonto().compareTo(new BigDecimal("1000")));
    }

    @Test
    void sinSuficienteHistorialNoDetectaNada() {
        assertTrue(detector.detectar(List.of()).isEmpty());
        assertTrue(detector.detectar(retiros("100")).isEmpty());
    }

    @Test
    void montosIgualesNoSonAtipicos() {
        assertTrue(detector.detectar(retiros("50", "50", "50", "50")).isEmpty());
    }
}
