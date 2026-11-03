package co.edu.uniquindio.poo.model;

public class EnvioNacional extends Envio{

    public String ciudadDestino;
    private boolean domicilio;
    private final double COSTO_FIJO = 6000;

    public EnvioNacional(float valorFijo, String codigo, float peso, float incremento, String destinatario, EmpresaEnvios ownedByEmpresa, String ciudadDestino, boolean domicilio) {
        super(valorFijo, codigo, peso, incremento, destinatario, ownedByEmpresa);
        this.ciudadDestino = ciudadDestino;
        this.domicilio = domicilio;
    }

    public String getCiudadDestino() {
        return ciudadDestino;
    }
    public void setCiudadDestino(String ciudadDestino) {
        this.ciudadDestino = ciudadDestino;
    }
    public boolean isDomicilio() {
        return domicilio;
    }
    public void setDomicilio(boolean domicilio) {
        this.domicilio = domicilio;
    }


    @Override
    public double calcularCostoTotal() {
        double costo = calcularCostoFijo();
        if(domicilio){
            costo += 3000;
        }
        return costo;
    }
}
