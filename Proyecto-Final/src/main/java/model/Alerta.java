package model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Alerta {
    private final String mensaje;
    private final TipoAlerta tipo;
    private final Cliente cliente;
    private final LocalDateTime fecha = LocalDateTime.now();

    public Alerta(String mensaje, TipoAlerta tipo, Cliente cliente) {
        this.mensaje = mensaje;
        this.tipo = tipo;
        this.cliente = cliente;
    }

    public String getMensaje() { return mensaje; }
    public TipoAlerta getTipo() { return tipo; }
    public Cliente getCliente() { return cliente; }
    public LocalDateTime getFecha() { return fecha; }

    @Override
    public String toString() {
        return fecha.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + " - [" + tipo + "] " + mensaje;
    }
}
