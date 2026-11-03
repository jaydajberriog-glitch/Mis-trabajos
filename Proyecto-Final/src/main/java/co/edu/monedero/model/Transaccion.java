package co.edu.monedero.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public abstract class Transaccion {
    private final String id;
    private final BigDecimal monto;
    private final LocalDateTime fecha;
    private final Monedero origen;
    private final Monedero destino;
    private final Cliente clientePuntos;
    private int puntosOtorgados;
    private EstadoTransaccion estado = EstadoTransaccion.EXITOSA;

    protected Transaccion(String id, BigDecimal monto, Monedero origen, Monedero destino, Cliente clientePuntos) {
        if (monto == null || monto.compareTo(BigDecimal.ZERO) <= 0)
            throw new IllegalArgumentException("El monto debe ser mayor que cero.");
        this.id = id; this.monto = monto; this.origen = origen; this.destino = destino;
        this.clientePuntos = clientePuntos; this.fecha = LocalDateTime.now();
    }
    public abstract String getTipo();
    public String getId() { return id; }
    public BigDecimal getMonto() { return monto; }
    public LocalDateTime getFecha() { return fecha; }
    public Monedero getOrigen() { return origen; }
    public Monedero getDestino() { return destino; }
    public Cliente getClientePuntos() { return clientePuntos; }
    public int getPuntosOtorgados() { return puntosOtorgados; }
    public void setPuntosOtorgados(int puntos) { puntosOtorgados = puntos; }
    public EstadoTransaccion getEstado() { return estado; }
    public void marcarReversada() { estado = EstadoTransaccion.REVERSADA; }
    @Override public String toString() {
        return fecha.toString().replace('T', ' ') + " | " + getTipo() + " | $" + monto +
                " | " + estado + " | " + id;
    }
}
