package exceptions;

public class SaldoInsuficienteException extends MonederoException {
    public SaldoInsuficienteException(String mensaje) {
        super(mensaje);
    }
}
