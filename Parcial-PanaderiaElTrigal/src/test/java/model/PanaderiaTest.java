package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PanaderiaTest {

    @Test
    void registraPedidoPendienteSinModificarMinutosDeEmpleados() {
        Panaderia panaderia = new Panaderia("El Trigal");
        PanaderoArtesanal ana = new PanaderoArtesanal("PA1", "Ana", 100);
        PanaderoIndustrial carlos = new PanaderoIndustrial("PI1", "Carlos", 45);
        panaderia.agregarEmpleado(ana);
        panaderia.agregarEmpleado(carlos);

        Pedido pedido = panaderia.registrarPedido("P01", "Café Central", TipoPan.TRADICIONAL, 25);

        assertTrue(pedido.estaPendiente());
        assertEquals(25, pedido.getCantidad());
        assertEquals(1, panaderia.getPedidos().size());
        assertEquals(100, ana.getMinutosDisponibles());
        assertEquals(45, carlos.getMinutosDisponibles());
    }

    @Test
    void noPermiteReutilizarCodigoAunquePedidoEsteCancelado() {
        Panaderia panaderia = new Panaderia("El Trigal");
        panaderia.registrarPedido("P01", "Café Central", TipoPan.TRADICIONAL, 10);
        panaderia.cancelarPedido("P01");

        assertThrows(IllegalArgumentException.class,
                () -> panaderia.registrarPedido("P01", "Otro cliente", TipoPan.INTEGRAL, 5));
        assertEquals(1, panaderia.getPedidos().size());
        assertTrue(panaderia.buscarPedido("P01").estaCancelado());
    }

    @Test
    void noRegistraCantidadCeroCuandoElHistorialEstaVacio() {
        Panaderia panaderia = new Panaderia("El Trigal");

        assertThrows(IllegalArgumentException.class,
                () -> panaderia.registrarPedido("P01", "Café Central", TipoPan.TRADICIONAL, 0));
        assertTrue(panaderia.getPedidos().isEmpty());
    }

    @Test
    void asignaACarlosCuandoEsElUnicoQuePuedeAtenderElPedido() {
        Panaderia panaderia = new Panaderia("El Trigal");
        PanaderoArtesanal ana = new PanaderoArtesanal("PA1", "Ana", 100);
        PanaderoIndustrial carlos = new PanaderoIndustrial("PI1", "Carlos", 45);
        panaderia.agregarEmpleado(ana);
        panaderia.agregarEmpleado(carlos);
        panaderia.registrarPedido("P01", "Café Central", TipoPan.TRADICIONAL, 25);

        Pedido pedido = panaderia.asignarPedido("P01");

        assertEquals("PI1", pedido.getCodigoPanaderoAsignado());
        assertEquals(45, pedido.getMinutosReservados());
        assertEquals(0, carlos.getMinutosDisponibles());
        assertEquals(100, ana.getMinutosDisponibles());
        assertTrue(pedido.estaAsignado());
    }

    @Test
    void asignaPorOrdenDeRegistroCuandoLosTiemposSonIgualesYExcluyeAlSinDisponibilidad() {
        Panaderia panaderia = new Panaderia("El Trigal");
        PanaderoArtesanal ana = new PanaderoArtesanal("PA1", "Ana", 80);
        PanaderoArtesanal diana = new PanaderoArtesanal("PA2", "Diana", 80);
        PanaderoIndustrial carlos = new PanaderoIndustrial("PI1", "Carlos", 54);
        panaderia.agregarEmpleado(ana);
        panaderia.agregarEmpleado(diana);
        panaderia.agregarEmpleado(carlos);
        panaderia.registrarPedido("P01", "Café Central", TipoPan.INTEGRAL, 13);

        Pedido pedido = panaderia.asignarPedido("P01");

        assertEquals("PA1", pedido.getCodigoPanaderoAsignado());
        assertEquals(80, pedido.getMinutosReservados());
        assertEquals(0, ana.getMinutosDisponibles());
        assertEquals(80, diana.getMinutosDisponibles());
        assertEquals(54, carlos.getMinutosDisponibles());
    }

    @Test
    void noAsignaSiCarlosNoTieneTiempoYLuciaEsCajera() {
        Panaderia panaderia = new Panaderia("El Trigal");
        PanaderoIndustrial carlos = new PanaderoIndustrial("PI1", "Carlos", 44);
        Cajero lucia = new Cajero("CA1", "Lucía");
        panaderia.agregarEmpleado(carlos);
        panaderia.agregarEmpleado(lucia);
        Pedido pedido = panaderia.registrarPedido("P01", "Café Central", TipoPan.TRADICIONAL, 25);

        assertThrows(IllegalStateException.class, () -> panaderia.asignarPedido("P01"));
        assertTrue(pedido.estaPendiente());
        assertEquals(44, carlos.getMinutosDisponibles());
        assertEquals(1, panaderia.getPedidos().size());
    }

    @Test
    void cancelarPrimeroDeDosPedidosDevuelveSoloSusMinutos() {
        Panaderia panaderia = new Panaderia("El Trigal");
        PanaderoIndustrial carlos = new PanaderoIndustrial("PI1", "Carlos", 120);
        panaderia.agregarEmpleado(carlos);
        panaderia.registrarPedido("P01", "Cliente uno", TipoPan.TRADICIONAL, 25);
        panaderia.registrarPedido("P02", "Cliente dos", TipoPan.TRADICIONAL, 25);

        panaderia.asignarPedido("P01");
        panaderia.asignarPedido("P02");
        panaderia.cancelarPedido("P01");

        assertEquals(75, carlos.getMinutosDisponibles());
        assertTrue(panaderia.buscarPedido("P01").estaCancelado());
        assertTrue(panaderia.buscarPedido("P02").estaAsignado());
    }

    @Test
    void cancelarPedidoPendienteLoConservaEnHistorialYSinCambiarMinutos() {
        Panaderia panaderia = new Panaderia("El Trigal");
        PanaderoArtesanal ana = new PanaderoArtesanal("PA1", "Ana", 100);
        panaderia.agregarEmpleado(ana);
        Pedido pedido = panaderia.registrarPedido("P01", "Café Central", TipoPan.TRADICIONAL, 13);

        panaderia.cancelarPedido("P01");

        assertTrue(pedido.estaCancelado());
        assertEquals(1, panaderia.getPedidos().size());
        assertSame(pedido, panaderia.getPedidos().get(0));
        assertEquals(100, ana.getMinutosDisponibles());
    }

    @Test
    void cancelarDosVecesNoDevuelveLosMinutosMasDeUnaVez() {
        Panaderia panaderia = new Panaderia("El Trigal");
        PanaderoIndustrial carlos = new PanaderoIndustrial("PI1", "Carlos", 120);
        panaderia.agregarEmpleado(carlos);
        panaderia.registrarPedido("P01", "Café Central", TipoPan.TRADICIONAL, 25);

        panaderia.asignarPedido("P01");
        panaderia.cancelarPedido("P01");
        assertEquals(120, carlos.getMinutosDisponibles());

        assertThrows(IllegalStateException.class, () -> panaderia.cancelarPedido("P01"));
        assertEquals(120, carlos.getMinutosDisponibles());
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
