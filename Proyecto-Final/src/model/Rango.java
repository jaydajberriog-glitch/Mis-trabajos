package co.edu.monedero.model;

public enum Rango {
    BRONCE, PLATA, ORO, PLATINO;

    public static Rango desdePuntos(int puntos) {
        if (puntos > 5000) return PLATINO;
        if (puntos >= 1001) return ORO;
        if (puntos >= 501) return PLATA;
        return BRONCE;
    }
}
