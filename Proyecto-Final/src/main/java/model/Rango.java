package model;

import java.math.BigDecimal;

public enum Rango {
    BRONCE("0"), PLATA("0.05"), ORO("0.10"), PLATINO("0.20");

    private final BigDecimal descuentoTarifa;

    Rango(String descuentoTarifa) { this.descuentoTarifa = new BigDecimal(descuentoTarifa); }

    /** Fracción de descuento sobre la comisión de transferencias propia del rango. */
    public BigDecimal getDescuentoTarifa() { return descuentoTarifa; }

    public static Rango desdePuntos(int puntos) {
        if (puntos > 5000) return PLATINO;
        if (puntos >= 1001) return ORO;
        if (puntos >= 501) return PLATA;
        return BRONCE;
    }
}
