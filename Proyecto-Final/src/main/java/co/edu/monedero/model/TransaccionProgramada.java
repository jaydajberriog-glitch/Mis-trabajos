package co.edu.monedero.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class TransaccionProgramada {
    private final String id;
    private final String tipo;
    private final BigDecimal monto;
    private final Monedero origen;
    private final Monedero destino;
    private final Periodicidad periodicidad;
    private LocalDate proximaEjecucion;
    private boolean activa = true;

    public TransaccionProgramada(String id, String tipo, BigDecimal monto, Monedero origen,
                                 Monedero destino, Periodicidad periodicidad, LocalDate fecha) {
        this.id=id; this.tipo=tipo; this.monto=monto; this.origen=origen; this.destino=destino;
        this.periodicidad=periodicidad; this.proximaEjecucion=fecha;
    }
    public String getId() { return id; }
    public String getTipo() { return tipo; }
    public BigDecimal getMonto() { return monto; }
    public Monedero getOrigen() { return origen; }
    public Monedero getDestino() { return destino; }
    public Periodicidad getPeriodicidad() { return periodicidad; }
    public LocalDate getProximaEjecucion() { return proximaEjecucion; }
    public boolean isActiva() { return activa; }
    public void avanzarFecha() {
        proximaEjecucion = switch (periodicidad) {
            case DIARIA -> proximaEjecucion.plusDays(1);
            case SEMANAL -> proximaEjecucion.plusWeeks(1);
            case MENSUAL -> proximaEjecucion.plusMonths(1);
        };
    }
    public void desactivar() { activa=false; }
    @Override public String toString() { return id + " | " + tipo + " | $" + monto + " | " + proximaEjecucion + " | " + (activa ? "ACTIVA" : "INACTIVA"); }
}
