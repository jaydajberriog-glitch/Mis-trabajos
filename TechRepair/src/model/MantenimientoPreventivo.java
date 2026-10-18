package model;

import java.util.List;

public class MantenimientoPreventivo extends Servicio {
    public MantenimientoPreventivo() {
        super("SER-MANT", "Mantenimiento preventivo", "Limpieza, revisión y optimización", 0);
    }

    @Override
    public double calcularCosto(Equipo equipo, double horas, List<RepuestoUtilizado> repuestos) {
        return switch (equipo.getTipoEquipo()) {
            case PORTATIL -> 60000;
            case ESCRITORIO -> 70000;
            case TELEFONO_INTELIGENTE -> 40000;
        };
    }
}
