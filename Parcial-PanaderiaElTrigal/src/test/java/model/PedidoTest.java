package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PedidoTest {

    @Test
    void pedidoNuevoQuedaPendiente() {
        Pedido pedido = new Pedido("P1", "Cafetería Sol", TipoPan.TRADICIONAL, 20);
        assertTrue(pedido.estaPendiente());
        assertNull(pedido.getCodigoPanaderoAsignado());
        assertEquals(0, pedido.getMinutosReservados());
    }

    @Test
    void asignarCambiaEstadoYGuardaReserva() {
        Pedido pedido = new Pedido("P1", "Cafetería Sol", TipoPan.TRADICIONAL, 20);
        pedido.asignar("PA1", 60);

        assertTrue(pedido.estaAsignado());
        assertEquals("PA1", pedido.getCodigoPanaderoAsignado());
        assertEquals(60, pedido.getMinutosReservados());
    }

    @Test
    void noPermiteAsignarPedidoDosVeces() {
        Pedido pedido = new Pedido("P1", "Cafetería Sol", TipoPan.TRADICIONAL, 20);
        pedido.asignar("PA1", 60);

        assertThrows(IllegalStateException.class, () -> pedido.asignar("PA2", 30));
    }

    @Test
    void rechazaCantidadCero() {
        assertThrows(IllegalArgumentException.class,
                () -> new Pedido("P1", "Cafetería Sol", TipoPan.TRADICIONAL, 0));
    }
}
