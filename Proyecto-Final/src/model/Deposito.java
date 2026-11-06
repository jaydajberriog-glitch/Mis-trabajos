package co.edu.monedero.model;
import java.math.BigDecimal;
public class Deposito extends Transaccion {
    public Deposito(String id, BigDecimal monto, Monedero destino, Cliente cliente) {
        super(id, monto, null, destino, cliente);
    }
    @Override public String getTipo() { return "DEPÓSITO"; }
}
