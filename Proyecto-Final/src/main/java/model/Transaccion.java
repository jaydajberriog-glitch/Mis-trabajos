package model;

import exceptions.OperacionInvalidaException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public abstract class Transaccion implements OperacionFinanciera {
    private final String id;
    private final BigDecimal monto;
    private final LocalDateTime fecha;
    private final Monedero origen;
    private final Monedero destino;
    private final Cliente clientePuntos;
    private int puntosOtorgados;
    private BigDecimal comision = BigDecimal.ZERO;
    private EstadoTransaccion estado = EstadoTransaccion.EXITOSA;

    protected Transaccion(String id, BigDecimal monto, Monedero origen, Monedero destino, Cliente clientePuntos) {
        if (monto == null || monto.compareTo(BigDecimal.ZERO) <= 0)
            throw new OperacionInvalidaException("El monto debe ser mayor que cero.");
        this.id = id;
        this.monto = monto;
        this.origen = origen;
        this.destino = destino;
        this.clientePuntos = clientePuntos;
        this.fecha = LocalDateTime.now();
    }

    @Override
    public abstract String getTipo();

    @Override
    public abstract int calcularPuntos();

    protected int bloquesDeCien() {
        return monto.divideToIntegralValue(new BigDecimal("100")).intValue();
    }

    public String getId() { return id; }
    @Override
    public BigDecimal getMonto() { return monto; }
    public LocalDateTime getFecha() { return fecha; }
    public Monedero getOrigen() { return origen; }
    public Monedero getDestino() { return destino; }
    public Cliente getClientePuntos() { return clientePuntos; }
    public int getPuntosOtorgados() { return puntosOtorgados; }
    public void setPuntosOtorgados(int puntos) { puntosOtorgados = puntos; }
    public EstadoTransaccion getEstado() { return estado; }
    public BigDecimal getComision() { return comision; }
    public void setComision(BigDecimal comision) { this.comision = comision == null ? BigDecimal.ZERO : comision; }
    public void marcarReversada() { estado = EstadoTransaccion.REVERSADA; }
    public void marcarSospechosa() {
        if (estado == EstadoTransaccion.EXITOSA) estado = EstadoTransaccion.SOSPECHOSA;
    }

    @Override
    public String toString() {
        return fecha.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + " | " + getTipo() +
                " | $" + monto + (comision.signum() > 0 ? " (comisión $" + comision + ")" : "")
                + " | " + estado + " | " + id;
    }
}
