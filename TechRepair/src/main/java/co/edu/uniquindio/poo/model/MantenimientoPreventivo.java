package co.edu.uniquindio.poo.model;
import java.util.List;

public class MantenimientoPreventivo extends Servicio {
    public MantenimientoPreventivo(){super("SER-MANT","Mantenimiento preventivo","Limpieza, revisión y optimización",0);}
    @Override public double calcularCosto(Equipo equipo,double horas,List<RepuestoUtilizado> repuestos){switch(equipo.getTipoEquipo()){case PORTATIL:return 60000;case ESCRITORIO:return 70000;default:return 40000;}}
}
