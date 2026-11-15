package model;

import java.math.BigDecimal;

public interface OperacionFinanciera {
    BigDecimal getMonto();
    String getTipo();
    int calcularPuntos();
}
