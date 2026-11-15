package test;

import exception.OperacionInvalidaException;
import exception.RecursoNoEncontradoException;
import exception.SaldoInsuficienteException;
import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.SistemaMonedero;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class SistemaMonederoTest {
    private SistemaMonedero sistema;

    @BeforeEach
    void preparar() {
        sistema = new SistemaMonedero();
        sistema.registrarCliente(new Cliente("1", "Cliente Uno", "3001234567", "uno@mail.com"));
        sistema.registrarCliente(new Cliente("2", "Cliente Dos", "3007654321", "dos@mail.com"));
        sistema.crearMonedero("1", "M1", TipoMonedero.AHORROS);
        sistema.crearMonedero("2", "M2", TipoMonedero.AHORROS);
        sistema.depositar("M1", new BigDecimal("100000"));
    }

    @Test
    void depositoAumentaSaldo() {
        sistema.depositar("M1", new BigDecimal("25000"));
        assertEquals(0, sistema.buscarMonedero("M1").getSaldo().compareTo(new BigDecimal("125000")));
    }

    @Test
    void retiroSinSaldoEsRechazado() {
        assertThrows(SaldoInsuficienteException.class, () -> sistema.retirar("M1", new BigDecimal("200000")));
    }

    @Test
    void transferenciaDebitaYAcredita() {
        sistema.transferir("M1", "M2", new BigDecimal("30000"));
        assertEquals(0, sistema.buscarMonedero("M1").getSaldo().compareTo(new BigDecimal("70000")));
        assertEquals(0, sistema.buscarMonedero("M2").getSaldo().compareTo(new BigDecimal("30000")));
    }

    @Test
    void depositoOtorgaUnPuntoPorCadaCienUnidades() {
        assertEquals(1000, sistema.buscarCliente("1").getPuntos());
    }

    @Test
    void retiroOtorgaDosPuntosPorCadaCienUnidades() {
        sistema.retirar("M1", new BigDecimal("10000"));
        assertEquals(1200, sistema.buscarCliente("1").getPuntos());
    }

    @Test
    void transferenciaOtorgaTresPuntosPorCadaCienUnidadesAlRemitente() {
        sistema.transferir("M1", "M2", new BigDecimal("10000"));
        assertEquals(1300, sistema.buscarCliente("1").getPuntos());
        assertEquals(0, sistema.buscarCliente("2").getPuntos());
    }

    @Test
    void reversarDepositoRestauraSaldoYPuntos() {
        int puntosAntes = sistema.buscarCliente("1").getPuntos();
        var transaccion = sistema.depositar("M1", new BigDecimal("20000"));
        sistema.revertir(transaccion.getId());
        assertEquals(0, sistema.buscarMonedero("M1").getSaldo().compareTo(new BigDecimal("100000")));
        assertEquals(puntosAntes, sistema.buscarCliente("1").getPuntos());
    }

    @Test
    void asignaRangosSegunLosLimitesDelPdf() {
        assertEquals(Rango.BRONCE, Rango.desdePuntos(500));
        assertEquals(Rango.PLATA, Rango.desdePuntos(501));
        assertEquals(Rango.PLATA, Rango.desdePuntos(1000));
        assertEquals(Rango.ORO, Rango.desdePuntos(1001));
        assertEquals(Rango.ORO, Rango.desdePuntos(5000));
        assertEquals(Rango.PLATINO, Rango.desdePuntos(5001));
    }

    @Test
    void detectaTransaccionesAtipicas() {
        sistema.retirar("M1", new BigDecimal("5000"));
        sistema.retirar("M1", new BigDecimal("5000"));
        sistema.retirar("M1", new BigDecimal("5000"));
        sistema.retirar("M1", new BigDecimal("5000"));
        sistema.retirar("M1", new BigDecimal("50000"));
        var atipicas = sistema.detectarAtipicas("1");
        assertEquals(1, atipicas.size());
    }

    @Test
    void ejecutaProgramadasEnOrden() {
        sistema.programar("DEPÓSITO", new BigDecimal("10000"), null, "M2", Periodicidad.MENSUAL, LocalDate.now());
        var res = sistema.ejecutarProgramadas(LocalDate.now());
        assertEquals(1, res.size());
        assertEquals(0, sistema.buscarMonedero("M2").getSaldo().compareTo(new BigDecimal("10000")));
    }

    @Test
    void lanzaExcepcionesPersonalizadas() {
        assertThrows(RecursoNoEncontradoException.class, () -> sistema.buscarCliente("999"));
        assertThrows(OperacionInvalidaException.class, () -> sistema.transferir("M1", "M1", new BigDecimal("1000")));
    }
}
