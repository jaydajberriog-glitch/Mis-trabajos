package test.java.co.edu.uniquindio.poo.model;

import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;
import java.util.logging.Logger;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Clase para probar el envio
 * @author Robinson Arias
 * @since 2026-10
 *
 * Licencia GNU/GPL V3.0 (https://raw.githubusercontent.com/grid-uq/poo/main/LICENSE)
 */
public class EnvioTest {
    /**
     * Instancia para el manejo de logs
     */
    private static final Logger LOG = Logger.getLogger(EnvioTest.class.getName());

    @Test
    public void calcularCostoFijo() {
        LOG.info("Inicio de prueba calcularCostoFijo...");

        Envio envio = new EnvioNacional(10000,"001",10,2000,"",
                null,"",false);

        double actual = envio.calcularCostoFijo();
        double esperado = 30000;

        assertEquals(esperado,actual);

        LOG.info("Fin de prueba calcularCostoFijo...");

    }
    // definir el conjunto de metodos que va a probar


}