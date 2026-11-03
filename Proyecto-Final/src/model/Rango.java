package co.edu.monedero.model;
public enum Rango {
    BRONCE, PLATA, ORO, PLATINO;
    public static Rango desdePuntos(int puntos) {
        if (puntos >= 3000) return PLATINO;
        if (puntos >= 1500) return ORO;
        if (puntos >= 500) return PLATA;
        return BRONCE;
    }
}
