package model;

public class EnvioNacional extends Envio {
    private static final double COSTO_FIJO = 6000;
    private static final double COSTO_POR_KG = 2000;
    private static final double RECARGO_DOMICILIO = 3000;

    private String ciudadDestino;
    private boolean domicilio;

    public EnvioNacional(String codigo, String destinatario, double pesoKg,
                         String ciudadDestino, boolean domicilio) {
        super(codigo, destinatario, pesoKg);
        this.ciudadDestino = ciudadDestino;
        this.domicilio = domicilio;
    }

    @Override
    public double calcularCosto() {
        double costo = COSTO_FIJO + COSTO_POR_KG * getPesoKg();
        if (domicilio) {
            costo += RECARGO_DOMICILIO;
        }
        return costo;
    }

    public String getCiudadDestino() { return ciudadDestino; }
    public boolean isDomicilio() { return domicilio; }

    @Override
    public String toString() {
        String textoDomicilio;
        if (domicilio) {
            textoDomicilio = "Sí";
        } else {
            textoDomicilio = "No";
        }
        return "[NACIONAL] " + super.toString() + " | Ciudad: " + ciudadDestino
                + " | Domicilio: " + textoDomicilio;
    }
}
