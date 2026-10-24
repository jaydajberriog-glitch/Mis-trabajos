package model;

public abstract class Envio {
    private String codigo;
    private String destinatario;
    private double pesoKg;

    public Envio(String codigo, String destinatario, double pesoKg) {
        this.codigo = codigo;
        this.destinatario = destinatario;
        this.pesoKg = pesoKg;
    }

    // Cada tipo de envío calcula su costo a su manera
    public abstract double calcularCosto();

    public String getCodigo() { return codigo; }
    public String getDestinatario() { return destinatario; }
    public double getPesoKg() { return pesoKg; }

    @Override
    public String toString() {
        return "Código: " + codigo + " | Destinatario: " + destinatario
                + " | Peso: " + pesoKg + " kg";
    }
}
