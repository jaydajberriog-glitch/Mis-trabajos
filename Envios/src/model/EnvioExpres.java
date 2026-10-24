package model;

public class EnvioExpres extends EnvioNacional {
    private static final double INCREMENTO = 0.30;
    private static final double RECARGO_24H = 5000;
    private static final int LIMITE_HORAS = 24;

    private int horasMaximas;

    public EnvioExpres(String codigo, String destinatario, double pesoKg,
                       String ciudadDestino, boolean domicilio, int horasMaximas) {
        super(codigo, destinatario, pesoKg, ciudadDestino, domicilio);
        this.horasMaximas = horasMaximas;
    }

    @Override
    public double calcularCosto() {
        // Costo nacional + 30 %
        double costo = super.calcularCosto() * (1 + INCREMENTO);
        if (horasMaximas <= LIMITE_HORAS) {
            costo += RECARGO_24H;
        }
        return costo;
    }

    public int getHorasMaximas() { return horasMaximas; }

    @Override
    public String toString() {
        String textoDomicilio;
        if (isDomicilio()) {
            textoDomicilio = "Sí";
        } else {
            textoDomicilio = "No";
        }
        return "[EXPRÉS] Código: " + getCodigo() + " | Destinatario: " + getDestinatario()
                + " | Peso: " + getPesoKg() + " kg | Ciudad: " + getCiudadDestino()
                + " | Domicilio: " + textoDomicilio + " | Horas máx.: " + horasMaximas;
    }
}
