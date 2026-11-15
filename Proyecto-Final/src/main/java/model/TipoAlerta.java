package model;

public enum TipoAlerta {
    SALDO_BAJO("Saldo bajo"),
    RECORDATORIO("Recordatorio de transacción programada"),
    ATIPICA("Movimiento atípico"),
    CANJE("Canje de beneficio");

    private final String titulo;

    TipoAlerta(String titulo) { this.titulo = titulo; }

    public String getTitulo() { return titulo; }
}
