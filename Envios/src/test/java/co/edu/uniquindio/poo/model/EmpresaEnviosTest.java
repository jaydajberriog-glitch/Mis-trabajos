package test.java.co.edu.uniquindio.poo.model;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class EmpresaEnviosTest {

    EmpresaEnvios empresaEnvios;
    EnvioNacional nacional;
    EnvioInternacional internacional;

    @BeforeEach
    public void setUp(){
        empresaEnvios = new EmpresaEnvios("Envios UQ");

        nacional = new EnvioNacional(10000,"001",14,2000,"Juan",
                 empresaEnvios,"Pereira",true);

        internacional = new EnvioInternacional(0000,"002",15,2000,"Juan",
                empresaEnvios,"Pereira",true);

    }

    @Test
    public void registrarEnvio() {
        String actual = empresaEnvios.registrarEnvio(nacional);
        String esperado = "Registrado exitosamente";
        assertEquals(esperado,actual);
    }
    @Test
    public void calcularCostoFijoNacional() {
        double resultado = nacional.calcularCostoFijo();
        assertEquals(10000,resultado);
    }
    @Test
    public void calcularCostoFijoInterNacional() {
        double resultado = internacional.calcularCostoFijo();
        assertEquals(10000,resultado);
    }

    @Test
    public void buscarEnvioTest(){

        empresaEnvios.registrarEnvio(nacional);
        Optional<Envio> envio = empresaEnvios.buscarEnvio("002");
        //boolean esperado = true;
        boolean actual = envio.isPresent();
        assertFalse(actual);
    }
    @Test
    public void buscarEnviosMayoresTest(){

        empresaEnvios.registrarEnvio(nacional);
        empresaEnvios.registrarEnvio(internacional);

        Envio nacional1 = new EnvioNacional(10000,"003",8,2000,"Juan",
                empresaEnvios,"Pereira",true);

        Envio internacional2 = new EnvioInternacional(0000,"004",10,2000,"Juan",
                empresaEnvios,"Pereira",true);

        empresaEnvios.registrarEnvio(nacional1);
        empresaEnvios.registrarEnvio(internacional2);

        List<Envio> enviosMayores = empresaEnvios.obtenerEnviosMayores(10);
        assertIterableEquals(List.of(nacional,internacional),enviosMayores);

    }







}