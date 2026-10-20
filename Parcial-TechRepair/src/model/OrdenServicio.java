package model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class OrdenServicio {
    public enum EstadoOrden { ABIERTA, EN_PROCESO, FINALIZADA, ENTREGADA, CANCELADA }

    private String codigo;
    private LocalDate fechaCreacion;
    private Cliente cliente;
    private String observaciones;
    private EstadoOrden estado;
    private List<DetalleServicio> detalles = new ArrayList<>();

    public OrdenServicio(String codigo, Cliente cliente, String observaciones) {
        if (codigo == null || codigo.trim().isEmpty() || cliente == null) {
            throw new IllegalArgumentException("Código y cliente son obligatorios.");
        }
        this.codigo = codigo.trim();
        this.cliente = cliente;
        this.observaciones = observaciones == null ? "" : observaciones.trim();
        this.fechaCreacion = LocalDate.now();
        this.estado = EstadoOrden.ABIERTA;
    }

    public String getCodigo() { return codigo; }
    public LocalDate getFechaCreacion() { return fechaCreacion; }
    public Cliente getCliente() { return cliente; }
    public String getObservaciones() { return observaciones; }
    public EstadoOrden getEstado() { return estado; }
    public List<DetalleServicio> getDetalles() { return detalles; }

    public void agregarDetalle(DetalleServicio detalle) {
        if (estado != EstadoOrden.ABIERTA && estado != EstadoOrden.EN_PROCESO) {
            throw new IllegalStateException("No se pueden agregar detalles a esta orden.");
        }
        if (detalle == null) throw new IllegalArgumentException("Detalle obligatorio.");
        for (DetalleServicio d : detalles) {
            if (d.getCodigo().equalsIgnoreCase(detalle.getCodigo())) {
                throw new IllegalArgumentException("Código de detalle repetido en esta orden.");
            }
        }
        if (!detalle.getEquipo().getCliente().getDocumento().equals(cliente.getDocumento())) {
            throw new IllegalArgumentException("El equipo no pertenece al cliente de la orden.");
        }
        detalles.add(detalle);
        if (this.estado == EstadoOrden.ABIERTA) {
            this.estado = EstadoOrden.EN_PROCESO;
        }
    }

    public double calcularValorTotal() {
        double total = 0;
        for (DetalleServicio d : detalles) total += d.calcularValor();
        return total;
    }

    public void finalizar() {
        if (detalles.isEmpty()) throw new IllegalStateException("La orden no tiene detalles.");
        for (DetalleServicio d : detalles) {
            if (d.getEstado() != DetalleServicio.EstadoDetalle.FINALIZADO && d.getEstado() != DetalleServicio.EstadoDetalle.CANCELADO) {
                throw new IllegalStateException("Todos los detalles deben estar finalizados o cancelados.");
            }
        }
        this.estado = EstadoOrden.FINALIZADA;
    }

    public void actualizarEstado(EstadoOrden nuevo) {
        if (nuevo == null) throw new IllegalArgumentException("Estado inválido.");
        if (nuevo == EstadoOrden.FINALIZADA) {
            finalizar();
            return;
        }
        if (estado == EstadoOrden.ENTREGADA || estado == EstadoOrden.CANCELADA || estado == EstadoOrden.FINALIZADA) {
            throw new IllegalStateException("La orden ya está cerrada.");
        }
        if (nuevo == EstadoOrden.ENTREGADA && estado != EstadoOrden.FINALIZADA) {
            throw new IllegalStateException("Primero debe finalizar la orden.");
        }
        this.estado = nuevo;
    }

    @Override
    public String toString() {
        return "Orden " + codigo + " | Fecha: " + fechaCreacion + " | Cliente: " + cliente.getNombreCompleto() + " | Estado: " + estado + " | Detalles: " + detalles.size() + " | Total: $" + String.format("%.0f", calcularValorTotal());
    }
}
