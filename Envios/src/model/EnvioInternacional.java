package model;

public class EnvioInternacional extends Envio {
    private static final double COSTO_FIJO = 15000;
    private static final double COSTO_POR_KG = 9000;
    private static final double RECARGO_SEGURO = 20000;

    private String paisDestino;
    private boolean seguro;

    public EnvioInternacional(String codigo, String destinatario, double pesoKg,
                              String paisDestino, boolean seguro) {
        super(codigo, destinatario, pesoKg);
        this.paisDestino = paisDestino;
        this.seguro = seguro;
    }

    @Override
    public double calcularCosto() {
        double costo = COSTO_FIJO + COSTO_POR_KG * getPesoKg();
        if (seguro) {
            costo += RECARGO_SEGURO;
        }
        return costo;
    }

    public String getPaisDestino() { return paisDestino; }
    public boolean isSeguro() { return seguro; }

    @Override
    public String toString() {
        String textoSeguro;
        if (seguro) {
            textoSeguro = "Sí";
        } else {
            textoSeguro = "No";
        }
        return "[INTERNACIONAL] " + super.toString() + " | País: " + paisDestino
                + " | Seguro: " + textoSeguro;
    }
}
