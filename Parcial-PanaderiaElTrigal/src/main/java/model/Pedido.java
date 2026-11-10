package model;

/**
 * Información y estado de un pedido de pan.
 */
public class Pedido {

    private final String codigo;
    private final String nombreCliente;
    private final TipoPan tipoPan;
    private final int cantidad;
    private EstadoPedido estado;
    private String codigoPanaderoAsignado;
    private int minutosReservados;

    public Pedido(String codigo, String nombreCliente, TipoPan tipoPan, int cantidad) {
        if (codigo == null || codigo.isBlank()) {
            throw new IllegalArgumentException("El código del pedido es obligatorio.");
        }
        if (nombreCliente == null || nombreCliente.isBlank()) {
            throw new IllegalArgumentException("El nombre del cliente es obligatorio.");
        }
        if (tipoPan == null) {
            throw new IllegalArgumentException("El tipo de pan es obligatorio.");
        }
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor que cero.");
        }

        this.codigo = codigo.trim();
        this.nombreCliente = nombreCliente.trim();
        this.tipoPan = tipoPan;
        this.cantidad = cantidad;
        this.estado = EstadoPedido.PENDIENTE;
        this.codigoPanaderoAsignado = null;
        this.minutosReservados = 0;
    }

    void asignar(String codigoPanadero, int minutos) {
        if (estado != EstadoPedido.PENDIENTE) {
            throw new IllegalStateException("Solo se pueden asignar pedidos pendientes.");
        }
        if (codigoPanadero == null || codigoPanadero.isBlank()) {
            throw new IllegalArgumentException("El código del panadero es obligatorio.");
        }
        if (minutos <= 0) {
            throw new IllegalArgumentException("Los minutos reservados deben ser mayores que cero.");
        }

        this.codigoPanaderoAsignado = codigoPanadero;
        this.minutosReservados = minutos;
        this.estado = EstadoPedido.ASIGNADO;
    }

    void cancelar() {
        if (estado == EstadoPedido.CANCELADO) {
            throw new IllegalStateException("El pedido ya está cancelado.");
        }

        estado = EstadoPedido.CANCELADO;
    }

    public boolean estaPendiente() {
        return estado == EstadoPedido.PENDIENTE;
    }

    public boolean estaAsignado() {
        return estado == EstadoPedido.ASIGNADO;
    }

    public boolean estaCancelado() {
        return estado == EstadoPedido.CANCELADO;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public TipoPan getTipoPan() {
        return tipoPan;
    }

    public int getCantidad() {
        return cantidad;
    }

    public EstadoPedido getEstado() {
        return estado;
    }

    public String getCodigoPanaderoAsignado() {
        return codigoPanaderoAsignado;
    }

    public int getMinutosReservados() {
        return minutosReservados;
    }
}
