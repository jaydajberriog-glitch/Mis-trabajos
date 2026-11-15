package exception;

public class OperacionInvalidaException extends MonederoException {
    public OperacionInvalidaException(String mensaje) {
        super(mensaje);
    }
}
