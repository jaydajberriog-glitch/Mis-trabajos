package co.edu.uniquindio.poo.model;

public abstract class Envio {


    private String codigo;
    protected float peso;
    private float incremento;
    public float valorFijo;

    private String destinatario;
    private EmpresaEnvios ownedByEmpresa;

    public Envio(float valorFijo,String codigo, float peso,float incremento, String destinatario,EmpresaEnvios ownedByEmpresa) {
        this.codigo = codigo;
        this.peso = peso;
        this.destinatario = destinatario;
        this.ownedByEmpresa = ownedByEmpresa;
        this.incremento = incremento;
        this.valorFijo = valorFijo;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public float getPeso() {
        return peso;
    }

    public void setPeso(float peso) {
        this.peso = peso;
    }

    public String getDestinatario() {
        return destinatario;
    }

    public float getIncremento() {
        return incremento;
    }

    public void setIncremento(float incremento) {
        this.incremento = incremento;
    }

    public float getValorFijo() {
        return valorFijo;
    }

    public void setValorFijo(float valorFijo) {
        this.valorFijo = valorFijo;
    }

    public void setDestinatario(String destinatario) {
        this.destinatario = destinatario;
    }

    public EmpresaEnvios getOwnedByEmpresa() {
        return ownedByEmpresa;
    }

    public void setOwnedByEmpresa(EmpresaEnvios ownedByEmpresa) {
        this.ownedByEmpresa = ownedByEmpresa;
    }

    public double calcularCostoFijo(){
        return valorFijo + peso * incremento;
    }


    public abstract double calcularCostoTotal();
}
