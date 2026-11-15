package model;

import exceptions.OperacionInvalidaException;
import exceptions.RecursoNoEncontradoException;
import exceptions.SaldoInsuficienteException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
        // Cliente 1 tiene 1000 puntos (PLATA): comisión 2% de 30000 = 600, menos 5% por rango = 570
        sistema.transferir("M1", "M2", new BigDecimal("30000"));
        assertEquals(0, sistema.buscarMonedero("M1").getSaldo().compareTo(new BigDecimal("69430")));
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
        for (int i = 0; i < 5; i++) sistema.retirar("M1", new BigDecimal("5000"));
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

    @Test
    void atipicaQuedaSospechosaYAlertaSoloUnaVez() {
        for (int i = 0; i < 5; i++) sistema.retirar("M1", new BigDecimal("5000"));
        var grande = sistema.retirar("M1", new BigDecimal("50000"));
        sistema.detectarAtipicas("1");
        sistema.detectarAtipicas("1");
        assertEquals(EstadoTransaccion.SOSPECHOSA, grande.getEstado());
        long alertasAtipicas = sistema.getAlertas().stream().filter(a -> a.getTipo() == TipoAlerta.ATIPICA).count();
        assertEquals(1, alertasAtipicas);
    }

    @Test
    void retiroCobraComisionDelUnoPorCiento() {
        var t = sistema.retirar("M1", new BigDecimal("10000"));
        assertEquals(0, t.getComision().compareTo(new BigDecimal("100")));
        assertEquals(0, sistema.buscarMonedero("M1").getSaldo().compareTo(new BigDecimal("89900")));
    }

    @Test
    void reversarRetiroDevuelveMontoYComision() {
        var t = sistema.retirar("M1", new BigDecimal("10000"));
        sistema.revertir(t.getId());
        assertEquals(0, sistema.buscarMonedero("M1").getSaldo().compareTo(new BigDecimal("100000")));
    }

    @Test
    void reversarTransferenciaDevuelveMontoYComision() {
        var t = sistema.transferir("M1", "M2", new BigDecimal("30000"));
        sistema.revertir(t.getId());
        assertEquals(0, sistema.buscarMonedero("M1").getSaldo().compareTo(new BigDecimal("100000")));
        assertEquals(0, sistema.buscarMonedero("M2").getSaldo().compareTo(BigDecimal.ZERO));
        assertEquals(EstadoTransaccion.REVERSADA, t.getEstado());
    }

    @Test
    void beneficioDeDescuentoReduceLaComisionDeTransferencias() {
        sistema.canjearBeneficio("1", "Reducción 10% comisión transferencias", "M1");
        // 100 puntos menos: sigue PLATA (5%) + 10% del beneficio = 15% -> 600 * 0.85 = 510
        var t = sistema.transferir("M1", "M2", new BigDecimal("30000"));
        assertEquals(0, t.getComision().compareTo(new BigDecimal("510")));
    }

    @Test
    void mesSinCargosPorRetirosEliminaLaComision() {
        sistema.canjearBeneficio("1", "Un mes sin cargos por retiros", "M1");
        var t = sistema.retirar("M1", new BigDecimal("10000"));
        assertEquals(0, t.getComision().signum());
        assertEquals(0, sistema.buscarMonedero("M1").getSaldo().compareTo(new BigDecimal("90000")));
    }

    @Test
    void bonoDeSaldoSeAcredita() {
        sistema.canjearBeneficio("1", "Bono de saldo de $50", "M1");
        assertEquals(0, sistema.buscarMonedero("M1").getSaldo().compareTo(new BigDecimal("100050")));
        assertEquals(0, sistema.buscarCliente("1").getPuntos());
    }

    @Test
    void historialDePuntosSeAjustaAlReversar() {
        var t = sistema.depositar("M1", new BigDecimal("20000"));
        sistema.revertir(t.getId());
        var historial = sistema.buscarCliente("1").getHistorialPuntos();
        int suma = historial.stream().mapToInt(MovimientoPuntos::getPuntos).sum();
        assertEquals(sistema.buscarCliente("1").getPuntos(), suma);
        assertEquals(-200, historial.get(historial.size() - 1).getPuntos());
    }

    @Test
    void reversarNoFallaSiLosPuntosYaSeCanjearon() {
        var t = sistema.depositar("M1", new BigDecimal("20000"));   // +200 puntos (total 1200)
        sistema.canjearBeneficio("1", "Bono de saldo de $50", "M1"); // -1000 puntos (quedan 200)
        sistema.retirar("M1", new BigDecimal("1000"));               // +20 puntos
        sistema.canjearBeneficio("1", "Reducción 10% comisión transferencias", "M1");
        assertDoesNotThrow(() -> sistema.revertir(t.getId()));
        assertTrue(sistema.buscarCliente("1").getPuntos() >= 0);
    }

    @Test
    void saldoBajoGeneraAlertaYNotificaPorLosTresCanales() {
        int antes = sistema.getNotificaciones().size();
        sistema.retirar("M1", new BigDecimal("90000")); // queda por debajo de 20.000
        assertTrue(sistema.getAlertas().stream().anyMatch(a -> a.getTipo() == TipoAlerta.SALDO_BAJO));
        var nuevas = sistema.getNotificaciones().subList(antes, sistema.getNotificaciones().size());
        assertEquals(3, nuevas.size());
        assertTrue(nuevas.get(0).startsWith("[CORREO"));
        assertTrue(nuevas.get(1).startsWith("[WHATSAPP"));
        assertTrue(nuevas.get(2).startsWith("[SMS"));
    }

    @Test
    void generaRecordatoriosUnaSolaVezPorEjecucion() {
        sistema.programar("DEPÓSITO", new BigDecimal("10000"), null, "M2", Periodicidad.MENSUAL, LocalDate.now().plusDays(1));
        assertEquals(1, sistema.generarRecordatorios(LocalDate.now()).size());
        assertEquals(0, sistema.generarRecordatorios(LocalDate.now()).size());
        assertTrue(sistema.getAlertas().stream().anyMatch(a -> a.getTipo() == TipoAlerta.RECORDATORIO));
    }

    @Test
    void noRecuerdaProgramadasLejanasNiDesactivadas() {
        sistema.programar("DEPÓSITO", new BigDecimal("10000"), null, "M2", Periodicidad.MENSUAL, LocalDate.now().plusDays(10));
        assertEquals(0, sistema.generarRecordatorios(LocalDate.now()).size());
    }

    @Test
    void montoNuloOCeroEsInvalido() {
        assertThrows(OperacionInvalidaException.class, () -> sistema.depositar("M1", null));
        assertThrows(OperacionInvalidaException.class, () -> sistema.retirar("M1", BigDecimal.ZERO));
    }
}
