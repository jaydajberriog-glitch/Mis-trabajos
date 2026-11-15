package model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/** Registro inmutable de un cambio en los puntos de un cliente (positivo suma, negativo resta). */
public class MovimientoPuntos {
    private final int puntos;
    private final String motivo;
    private final String idTransaccion;
    private final LocalDateTime fecha = LocalDateTime.now();

    public MovimientoPuntos(int puntos, String motivo, String idTransaccion) {
        this.puntos = puntos;
        this.motivo = motivo;
        this.idTransaccion = idTransaccion;
    }

    public int getPuntos() { return puntos; }
    public String getMotivo() { return motivo; }
    public String getIdTransaccion() { return idTransaccion; }
    public LocalDateTime getFecha() { return fecha; }

    @Override
    public String toString() {
        return fecha.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + " | "
                + (puntos >= 0 ? "+" : "") + puntos + " pts | " + motivo
                + (idTransaccion == null ? "" : " (" + idTransaccion + ")");
    }
}
