package co.edu.uniquindio.poo.model;

public class EnvioInternacional extends Envio{

    public String paiseDestino;
    private boolean seguro;
    private final double COSTO_FIJO = 15_000;

    public EnvioInternacional(float valorFijo, String codigo, float peso, float incremento, String destinatario, EmpresaEnvios ownedByEmpresa, String paiseDestino, boolean seguro) {
        super(valorFijo, codigo, peso, incremento, destinatario, ownedByEmpresa);
        this.paiseDestino = paiseDestino;
        this.seguro = seguro;
    }

    public String getPaiseDestino() {
        return paiseDestino;
    }

    public void setPaiseDestino(String paiseDestino) {
        this.paiseDestino = paiseDestino;
    }

    public boolean isSeguro() {
        return seguro;
    }

    public void setSeguro(boolean seguro) {
        this.seguro = seguro;
    }

    @Override
    public double calcularCostoTotal() {
        double costo = calcularCostoFijo();
        if(seguro){
            costo += 20000;
        }
        return costo;
    }
}
