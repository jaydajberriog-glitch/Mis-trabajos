package model;

import java.util.List;

public class Diagnostico extends Servicio {
    public Diagnostico() {
        super("SER-DIAG", "Diagnóstico", "Identificación de fallas del equipo", 35000);
    }

    @Override
    public double calcularCosto(Equipo equipo, double horas, List<RepuestoUtilizado> repuestos) {
        return 35000;
    }
}
