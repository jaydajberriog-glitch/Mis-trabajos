package model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Alerta {
    private final String mensaje;
    private final LocalDateTime fecha = LocalDateTime.now();

    public Alerta(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getMensaje() { return mensaje; }
    public LocalDateTime getFecha() { return fecha; }

    @Override
    public String toString() {
        return fecha.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + " - " + mensaje;
    }
}
