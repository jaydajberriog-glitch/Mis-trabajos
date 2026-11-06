package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PanaderiaTest {

    @Test
    void registraPedidoPendiente() {
        Panaderia panaderia = new Panaderia("El Trigal");
        Pedido pedido = panaderia.registrarPedido("P1", "Café Central", TipoPan.TRADICIONAL, 10);

        assertTrue(pedido.estaPendiente());
        assertEquals(1, panaderia.getPedidos().size());
    }

    @Test
    void noPermiteReutilizarCodigoAunquePedidoEsteCancelado() {
        Panaderia panaderia = new Panaderia("El Trigal");
        panaderia.registrarPedido("P1", "Café Central", TipoPan.TRADICIONAL, 10);
        panaderia.cancelarPedido("P1");

        assertThrows(IllegalArgumentException.class,
                () -> panaderia.registrarPedido("P1", "Otro cliente", TipoPan.INTEGRAL, 5));
    }

    @Test
    void asignaAlPanaderoQueTardaMenos() {
        Panaderia panaderia = new Panaderia("El Trigal");
        PanaderoArtesanal artesanal = new PanaderoArtesanal("PA1", "Carlos", 200);
        PanaderoIndustrial industrial = new PanaderoIndustrial("PI1", "María", 200);
        panaderia.agregarEmpleado(artesanal);
        panaderia.agregarEmpleado(industrial);
        panaderia.registrarPedido("P1", "Café Central", TipoPan.TRADICIONAL, 12);

        Pedido pedido = panaderia.asignarPedido("P1");

        assertEquals("PA1", pedido.getCodigoPanaderoAsignado());
        assertEquals(170, artesanal.getMinutosDisponibles());
        assertEquals(200, industrial.getMinutosDisponibles());
    }

    @Test
    void enEmpateSeleccionaAlPrimeroRegistrado() {
        Panaderia panaderia = new Panaderia("El Trigal");
        PanaderoArtesanal primero = new PanaderoArtesanal("PA1", "Carlos", 200);
        PanaderoArtesanal segundo = new PanaderoArtesanal("PA2", "Luis", 200);
        panaderia.agregarEmpleado(primero);
        panaderia.agregarEmpleado(segundo);
        panaderia.registrarPedido("P1", "Café Central", TipoPan.TRADICIONAL, 12);

        Pedido pedido = panaderia.asignarPedido("P1");

        assertEquals("PA1", pedido.getCodigoPanaderoAsignado());
        assertEquals(170, primero.getMinutosDisponibles());
        assertEquals(200, segundo.getMinutosDisponibles());
    }

    @Test
    void ignoraCajerosAlAsignar() {
        Panaderia panaderia = new Panaderia("El Trigal");
        panaderia.agregarEmpleado(new Cajero("CA1", "Ana"));
        panaderia.registrarPedido("P1", "Café Central", TipoPan.TRADICIONAL, 12);

        assertThrows(IllegalStateException.class, () -> panaderia.asignarPedido("P1"));
    }

    @Test
    void cancelarPedidoAsignadoDevuelveMinutosExactos() {
        Panaderia panaderia = new Panaderia("El Trigal");
        PanaderoArtesanal panadero = new PanaderoArtesanal("PA1", "Carlos", 200);
        panaderia.agregarEmpleado(panadero);
        panaderia.registrarPedido("P1", "Café Central", TipoPan.TRADICIONAL, 13);

        panaderia.asignarPedido("P1");
        assertEquals(140, panadero.getMinutosDisponibles());

        panaderia.cancelarPedido("P1");

        assertEquals(200, panadero.getMinutosDisponibles());
        assertTrue(panaderia.buscarPedido("P1").estaCancelado());
    }

    @Test
    void cancelarPedidoPendienteNoModificaMinutos() {
        Panaderia panaderia = new Panaderia("El Trigal");
        PanaderoArtesanal panadero = new PanaderoArtesanal("PA1", "Carlos", 200);
        panaderia.agregarEmpleado(panadero);
        panaderia.registrarPedido("P1", "Café Central", TipoPan.TRADICIONAL, 13);

        panaderia.cancelarPedido("P1");

        assertEquals(200, panadero.getMinutosDisponibles());
    }

    @Test
    void noAsignaSiNingunPanaderoTieneTiempoSuficiente() {
        Panaderia panaderia = new Panaderia("El Trigal");
        panaderia.agregarEmpleado(new PanaderoArtesanal("PA1", "Carlos", 20));
        panaderia.registrarPedido("P1", "Café Central", TipoPan.TRADICIONAL, 12);

        assertThrows(IllegalStateException.class, () -> panaderia.asignarPedido("P1"));
    }

    @Test
    void rechazaCodigoDeEmpleadoRepetido() {
        Panaderia panaderia = new Panaderia("El Trigal");
        panaderia.agregarEmpleado(new Cajero("E1", "Ana"));

        assertThrows(IllegalArgumentException.class,
                () -> panaderia.agregarEmpleado(new Cajero("E1", "Luis")));
    }
}
