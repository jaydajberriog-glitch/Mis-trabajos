package co.edu.monedero.model;
import java.time.LocalDateTime;
public class Alerta {
    private final String mensaje;
    private final LocalDateTime fecha = LocalDateTime.now();
    public Alerta(String mensaje) { this.mensaje = mensaje; }
    public String getMensaje() { return mensaje; }
    public LocalDateTime getFecha() { return fecha; }
    @Override public String toString() { return fecha.toString().replace('T', ' ') + " - " + mensaje; }
}
