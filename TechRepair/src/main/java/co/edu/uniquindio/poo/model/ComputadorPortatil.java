package co.edu.uniquindio.poo.model;
public class ComputadorPortatil extends Equipo {
    private double tamanoPantalla; private int memoriaRamGB; private int almacenamientoGB; private boolean bateriaExtraible;
    public ComputadorPortatil(String codigo,String serie,String marca,String modelo,int anio,String estado,Cliente cliente,double pantalla,int ram,int almacenamiento,boolean bateriaExtraible) {
        super(codigo,serie,marca,modelo,anio,estado,cliente);
        if(pantalla<=0 || ram<=0 || almacenamiento<=0) throw new IllegalArgumentException("Pantalla, RAM y almacenamiento deben ser mayores que cero.");
        this.tamanoPantalla=pantalla; this.memoriaRamGB=ram; this.almacenamientoGB=almacenamiento; this.bateriaExtraible=bateriaExtraible;
    }
    @Override public TipoEquipo getTipoEquipo(){return TipoEquipo.PORTATIL;}
    public double getTamanoPantalla(){return tamanoPantalla;} public int getMemoriaRamGB(){return memoriaRamGB;} public int getAlmacenamientoGB(){return almacenamientoGB;} public boolean isBateriaExtraible(){return bateriaExtraible;}
    @Override public String toString(){return super.toString()+" | Pantalla: "+tamanoPantalla+" pulgadas | RAM: "+memoriaRamGB+" GB | Almacenamiento: "+almacenamientoGB+" GB | Batería extraíble: "+bateriaExtraible;}
}
