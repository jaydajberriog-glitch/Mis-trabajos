package co.edu.monedero.service;

import co.edu.monedero.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

class SistemaMonederoTest {
    private SistemaMonedero sistema;
    @BeforeEach void preparar() {
        sistema = new SistemaMonedero();
        sistema.registrarCliente(new Cliente("1", "Cliente Uno", "", ""));
        sistema.registrarCliente(new Cliente("2", "Cliente Dos", "", ""));
        sistema.crearMonedero("1", "M1", TipoMonedero.AHORROS);
        sistema.crearMonedero("2", "M2", TipoMonedero.AHORROS);
        sistema.depositar("M1", new BigDecimal("100000"));
    }
    @Test void depositoAumentaSaldo() {
        sistema.depositar("M1", new BigDecimal("25000"));
        assertEquals(0, sistema.buscarMonedero("M1").getSaldo().compareTo(new BigDecimal("125000")));
    }
    @Test void retiroSinSaldoEsRechazado() {
        assertThrows(IllegalArgumentException.class, () -> sistema.retirar("M1", new BigDecimal("200000")));
    }
    @Test void transferenciaDebitaYAcredita() {
        sistema.transferir("M1", "M2", new BigDecimal("30000"));
        assertEquals(0, sistema.buscarMonedero("M1").getSaldo().compareTo(new BigDecimal("70000")));
        assertEquals(0, sistema.buscarMonedero("M2").getSaldo().compareTo(new BigDecimal("30000")));
    }
    @Test void depositoOtorgaPuntos() {
        assertEquals(10, sistema.buscarCliente("1").getPuntos());
    }
    @Test void reversarDepositoRestauraSaldoYPuntos() {
        int puntosAntes = sistema.buscarCliente("1").getPuntos();
        var transaccion = sistema.depositar("M1", new BigDecimal("20000"));
        sistema.revertir(transaccion.getId());
        assertEquals(0, sistema.buscarMonedero("M1").getSaldo().compareTo(new BigDecimal("100000")));
        assertEquals(puntosAntes, sistema.buscarCliente("1").getPuntos());
    }
}
