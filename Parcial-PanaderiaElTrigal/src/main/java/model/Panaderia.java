package model;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Administra los empleados y los pedidos de la panadería.
 */
public class Panaderia {

    private final String nombre;
    private final List<Empleado> empleados;
    private final Map<String, Pedido> pedidos;

    public Panaderia(String nombre) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre de la panadería es obligatorio.");
        }

        this.nombre = nombre.trim();
        this.empleados = new ArrayList<>();
        this.pedidos = new LinkedHashMap<>();
    }

    public void agregarEmpleado(Empleado empleado) {
        if (empleado == null) {
            throw new IllegalArgumentException("El empleado no puede ser null.");
        }

        for (Empleado empleadoRegistrado : empleados) {
            if (empleadoRegistrado.getCodigo().equalsIgnoreCase(empleado.getCodigo())) {
                throw new IllegalArgumentException("Ya existe un empleado con ese código.");
            }
        }

        empleados.add(empleado);
    }

    public Pedido registrarPedido(String codigo, String nombreCliente, TipoPan tipoPan, int cantidad) {
        if (codigo == null || codigo.isBlank()) {
            throw new IllegalArgumentException("El código del pedido es obligatorio.");
        }

        String codigoNormalizado = codigo.trim();
        if (pedidos.containsKey(codigoNormalizado)) {
            throw new IllegalArgumentException("Ya existe un pedido con ese código.");
        }

        Pedido pedido = new Pedido(codigoNormalizado, nombreCliente, tipoPan, cantidad);
        pedidos.put(codigoNormalizado, pedido);
        return pedido;
    }

    public Pedido asignarPedido(String codigoPedido) {
        Pedido pedido = buscarPedido(codigoPedido);

        if (!pedido.estaPendiente()) {
            throw new IllegalStateException("El pedido no está pendiente.");
        }

        Panadero panaderoSeleccionado = seleccionarPanaderoMasRapido(
                pedido.getTipoPan(), pedido.getCantidad()
        );

        if (panaderoSeleccionado == null) {
            throw new IllegalStateException("Ningún panadero tiene tiempo suficiente para este pedido.");
        }

        int minutos = panaderoSeleccionado.calcularTiempoProduccion(
                pedido.getTipoPan(), pedido.getCantidad()
        );

        panaderoSeleccionado.descontarMinutos(minutos);
        pedido.asignar(panaderoSeleccionado.getCodigo(), minutos);

        return pedido;
    }

    public void cancelarPedido(String codigoPedido) {
        Pedido pedido = buscarPedido(codigoPedido);

        if (pedido.estaCancelado()) {
            throw new IllegalStateException("El pedido ya está cancelado.");
        }

        if (pedido.estaAsignado()) {
            Panadero panadero = buscarPanaderoPorCodigo(pedido.getCodigoPanaderoAsignado());
            panadero.devolverMinutos(pedido.getMinutosReservados());
        }

        pedido.cancelar();
    }

    public Pedido buscarPedido(String codigo) {
        if (codigo == null || codigo.isBlank()) {
            throw new IllegalArgumentException("El código del pedido es obligatorio.");
        }

        Pedido pedido = pedidos.get(codigo.trim());
        if (pedido == null) {
            throw new IllegalArgumentException("No existe un pedido con ese código.");
        }

        return pedido;
    }

    public Panadero buscarPanaderoPorCodigo(String codigo) {
        if (codigo == null || codigo.isBlank()) {
            throw new IllegalArgumentException("El código del panadero es obligatorio.");
        }

        for (Empleado empleado : empleados) {
            if (empleado instanceof Panadero panadero
                    && panadero.getCodigo().equalsIgnoreCase(codigo)) {
                return panadero;
            }
        }

        throw new IllegalStateException("No se encontró el panadero asociado al pedido.");
    }

    private Panadero seleccionarPanaderoMasRapido(TipoPan tipoPan, int cantidad) {
        Panadero mejorPanadero = null;
        int menorTiempo = Integer.MAX_VALUE;

        for (Empleado empleado : empleados) {
            if (empleado instanceof Panadero panadero) {
                int tiempo = panadero.calcularTiempoProduccion(tipoPan, cantidad);

                if (panadero.tieneTiempoSuficiente(tiempo) && tiempo < menorTiempo) {
                    mejorPanadero = panadero;
                    menorTiempo = tiempo;
                }
            }
        }

        return mejorPanadero;
    }

    public String getNombre() {
        return nombre;
    }

    public List<Empleado> getEmpleados() {
        return List.copyOf(empleados);
    }

    public List<Pedido> getPedidos() {
        return List.copyOf(pedidos.values());
    }
}
