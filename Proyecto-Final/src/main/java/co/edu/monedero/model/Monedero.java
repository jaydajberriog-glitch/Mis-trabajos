package co.edu.monedero.model;

import java.math.BigDecimal;

public class Monedero {
    private final String codigo;
    private final TipoMonedero tipo;
    private BigDecimal saldo = BigDecimal.ZERO;

    public Monedero(String codigo, TipoMonedero tipo) {
        if (codigo == null || codigo.isBlank() || tipo == null)
            throw new IllegalArgumentException("Código y tipo de monedero son obligatorios.");
        this.codigo = codigo.trim();
        this.tipo = tipo;
    }
    public void acreditar(BigDecimal monto) { validarMonto(monto); saldo = saldo.add(monto); }
    public void debitar(BigDecimal monto) {
        validarMonto(monto);
        if (saldo.compareTo(monto) < 0) throw new IllegalArgumentException("Saldo insuficiente.");
        saldo = saldo.subtract(monto);
    }
    private void validarMonto(BigDecimal monto) {
        if (monto == null || monto.compareTo(BigDecimal.ZERO) <= 0)
            throw new IllegalArgumentException("El monto debe ser mayor que cero.");
    }
    public String getCodigo() { return codigo; }
    public TipoMonedero getTipo() { return tipo; }
    public BigDecimal getSaldo() { return saldo; }
    @Override public String toString() { return codigo + " - " + tipo + " - $" + saldo; }
}
