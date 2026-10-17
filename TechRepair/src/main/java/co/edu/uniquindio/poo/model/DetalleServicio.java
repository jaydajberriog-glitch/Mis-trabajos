package co.edu.uniquindio.poo.model;
import java.util.ArrayList;import java.util.List;
public class DetalleServicio {
    private String codigo;private Equipo equipo;private Servicio servicio;private String fallaReportada;private Tecnico tecnico;private double horasTrabajo;private String observacionesTecnicas;private EstadoDetalle estado;private List<RepuestoUtilizado> repuestosUtilizados=new ArrayList<>();
    public DetalleServicio(String codigo,Equipo equipo,Servicio servicio,String falla){if(vacio(codigo)||equipo==null||servicio==null||vacio(falla))throw new IllegalArgumentException("Datos del detalle inválidos.");this.codigo=codigo;this.equipo=equipo;this.servicio=servicio;this.fallaReportada=falla;this.estado=EstadoDetalle.PENDIENTE;this.observacionesTecnicas="Sin observaciones";}
    private boolean vacio(String s){return s==null||s.trim().isEmpty();}
    public String getCodigo(){return codigo;}public Equipo getEquipo(){return equipo;}public Servicio getServicio(){return servicio;}public String getFallaReportada(){return fallaReportada;}public Tecnico getTecnico(){return tecnico;}public double getHorasTrabajo(){return horasTrabajo;}public String getObservacionesTecnicas(){return observacionesTecnicas;}public EstadoDetalle getEstado(){return estado;}public List<RepuestoUtilizado> getRepuestosUtilizados(){return repuestosUtilizados;}
    public void asignarTecnico(Tecnico tecnico){if(tecnico==null||!tecnico.puedeAtender(equipo))throw new IllegalArgumentException("El técnico no está activo o no tiene la especialidad necesaria.");this.tecnico=tecnico;}
    public void actualizarEstado(EstadoDetalle estado){if(estado==null)throw new IllegalArgumentException("Estado inválido.");if(this.estado==EstadoDetalle.FINALIZADO||this.estado==EstadoDetalle.CANCELADO)throw new IllegalStateException("La intervención ya está cerrada.");this.estado=estado;}
    public void registrarHoras(double horas){if(!(servicio instanceof ReparacionCorrectiva))throw new IllegalArgumentException("Las horas solo aplican a reparación correctiva.");if(horas<=0)throw new IllegalArgumentException("Las horas deben ser mayores que cero.");this.horasTrabajo=horas;}
    public void agregarRepuesto(Repuesto repuesto,int cantidad){
        if(!(servicio instanceof ReparacionCorrectiva)) throw new IllegalArgumentException("Solo una reparación correctiva puede usar repuestos.");
        if(repuesto==null) throw new IllegalArgumentException("Seleccione un repuesto válido.");
        boolean compatible = repuesto.getCategoria()==CategoriaRepuesto.UNIVERSAL
                || (repuesto.getCategoria()==CategoriaRepuesto.COMPUTADORES && equipo.getTipoEquipo()!=TipoEquipo.TELEFONO_INTELIGENTE)
                || (repuesto.getCategoria()==CategoriaRepuesto.DISPOSITIVOS_MOVILES && equipo.getTipoEquipo()==TipoEquipo.TELEFONO_INTELIGENTE);
        if(!compatible) throw new IllegalArgumentException("La categoría del repuesto no es compatible con el equipo.");
        repuesto.descontar(cantidad);
        try { repuestosUtilizados.add(new RepuestoUtilizado(repuesto,cantidad)); }
        catch(RuntimeException e) { repuesto.devolver(cantidad); throw e; }
    }
    public void setObservacionesTecnicas(String observaciones){if(vacio(observaciones))throw new IllegalArgumentException("Escriba una observación.");this.observacionesTecnicas=observaciones.trim();}
    public double calcularValor(){return servicio.calcularCosto(equipo,horasTrabajo,repuestosUtilizados);}
    @Override public String toString(){return "Detalle "+codigo+" | Equipo: "+equipo.getCodigo()+" | Servicio: "+servicio.getNombre()+" | Estado: "+estado+" | Técnico: "+(tecnico==null?"Sin asignar":tecnico.getNombre())+" | Valor: $"+String.format("%.0f",calcularValor());}
}
