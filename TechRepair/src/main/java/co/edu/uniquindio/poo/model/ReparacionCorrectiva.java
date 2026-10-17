package co.edu.uniquindio.poo.model;
import java.util.List;
public class ReparacionCorrectiva extends Servicio {
    public static final double COSTO_HORA=45000;
    public ReparacionCorrectiva(){super("SER-REP","Reparación correctiva","Solución de fallas identificadas",COSTO_HORA);}
    @Override public double calcularCosto(Equipo equipo,double horas,List<RepuestoUtilizado> repuestos){if(horas<0)throw new IllegalArgumentException("Las horas no pueden ser negativas.");double total=horas*COSTO_HORA;for(RepuestoUtilizado r:repuestos)total+=r.calcularSubtotal();return total;}
}
