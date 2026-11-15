package model;

import java.util.List;

/**
 * Detecta montos atípicos: un monto es atípico si supera promedio + 2 x desviación estándar
 * (desviación poblacional) del historial recibido.
 * Complejidad: promedio O(n), desviación O(n) y filtrado O(n), es decir O(n) en total.
 */
public class DetectorAtipicas {
    private static final double FACTOR = 2.0;

    public double promedio(List<Transaccion> historial) {
        return historial.stream().mapToDouble(t -> t.getMonto().doubleValue()).average().orElse(0);
    }

    public double desviacion(List<Transaccion> historial) {
        return desviacion(historial, promedio(historial));
    }

    private double desviacion(List<Transaccion> historial, double media) {
        return Math.sqrt(historial.stream()
                .mapToDouble(t -> Math.pow(t.getMonto().doubleValue() - media, 2))
                .average().orElse(0));
    }

    public double limite(List<Transaccion> historial) {
        double media = promedio(historial);
        return media + FACTOR * desviacion(historial, media);
    }

    public List<Transaccion> detectar(List<Transaccion> historial) {
        if (historial.size() < 2) return List.of();
        double limite = limite(historial);
        return historial.stream().filter(t -> t.getMonto().doubleValue() > limite).toList();
    }
}
