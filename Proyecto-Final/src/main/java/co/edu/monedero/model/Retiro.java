package co.edu.monedero.model;
import java.math.BigDecimal;
public class Retiro extends Transaccion {
    public Retiro(String id, BigDecimal monto, Monedero origen, Cliente cliente) {
        super(id, monto, origen, null, cliente);
    }
    @Override public String getTipo() { return "RETIRO"; }
}
