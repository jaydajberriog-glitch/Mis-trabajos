package model;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

class TransaccionTest {
    @Test
    void calculaPuntosSegunTipoDeOperacion() {
        Cliente c = new Cliente("10", "Test", "300", "a@b.com");
        Monedero m1 = new Monedero("M1", TipoMonedero.AHORROS);
        Monedero m2 = new Monedero("M2", TipoMonedero.GASTOS_DIARIOS);

        Transaccion dep = new Deposito("T1", new BigDecimal("10000"), m1, c);
        Transaccion ret = new Retiro("T2", new BigDecimal("10000"), m1, c);
        Transaccion tra = new Transferencia("T3", new BigDecimal("10000"), m1, m2, c);

        assertEquals(100, dep.calcularPuntos());
        assertEquals(200, ret.calcularPuntos());
        assertEquals(300, tra.calcularPuntos());
    }
}
