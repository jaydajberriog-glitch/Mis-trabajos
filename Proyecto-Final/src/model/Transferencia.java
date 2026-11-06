package co.edu.monedero.model;
import java.math.BigDecimal;
public class Transferencia extends Transaccion {
    public Transferencia(String id, BigDecimal monto, Monedero origen, Monedero destino, Cliente remitente) {
        super(id, monto, origen, destino, remitente);
    }
    @Override public String getTipo() { return "TRANSFERENCIA"; }
}
