package co.edu.uniquindio.poo.model;
public class Tecnico {
    private String codigo; private String nombre; private String documento; private EspecialidadTecnico especialidad; private EstadoTecnico estado;
    public Tecnico(String codigo,String nombre,String documento,EspecialidadTecnico especialidad,EstadoTecnico estado){
        if(vacio(codigo)||vacio(nombre)||vacio(documento)||especialidad==null||estado==null) throw new IllegalArgumentException("Todos los datos del técnico son obligatorios.");
        this.codigo=codigo.trim();this.nombre=nombre.trim();this.documento=documento.trim();this.especialidad=especialidad;this.estado=estado;
    }
    private boolean vacio(String s){return s==null||s.trim().isEmpty();}
    public String getCodigo(){return codigo;} public String getNombre(){return nombre;} public String getDocumento(){return documento;} public EspecialidadTecnico getEspecialidad(){return especialidad;} public EstadoTecnico getEstado(){return estado;}
    public void setEstado(EstadoTecnico estado){if(estado==null)throw new IllegalArgumentException("Estado inválido.");this.estado=estado;}
    public boolean puedeAtender(Equipo equipo){if(estado!=EstadoTecnico.ACTIVO)return false;return especialidad==EspecialidadTecnico.SOPORTE_GENERAL||(especialidad==EspecialidadTecnico.COMPUTADORES&&equipo.getTipoEquipo()!=TipoEquipo.TELEFONO_INTELIGENTE)||(especialidad==EspecialidadTecnico.DISPOSITIVOS_MOVILES&&equipo.getTipoEquipo()==TipoEquipo.TELEFONO_INTELIGENTE);}
    @Override public String toString(){return codigo+" - "+nombre+" | "+especialidad+" | "+estado;}
}
