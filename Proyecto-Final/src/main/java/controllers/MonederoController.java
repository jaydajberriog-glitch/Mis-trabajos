package controllers;

import model.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class MonederoController {
    private final SistemaMonedero sistema;

    public MonederoController() {
        this.sistema = new SistemaMonedero();
        cargarDatosIniciales();
    }

    public MonederoController(SistemaMonedero sistema) {
        this.sistema = sistema;
    }

    private void cargarDatosIniciales() {
        Cliente ana = new Cliente("1001", "Ana Pérez", "3001112233", "ana@email.com");
        Cliente luis = new Cliente("1002", "Luis Gómez", "3004445566", "luis@email.com");
        sistema.registrarCliente(ana);
        sistema.registrarCliente(luis);
        sistema.crearMonedero("1001", "AH-001", TipoMonedero.AHORROS);
        sistema.crearMonedero("1001", "GD-001", TipoMonedero.GASTOS_DIARIOS);
        sistema.crearMonedero("1002", "AH-002", TipoMonedero.AHORROS);
        sistema.depositar("AH-001", new BigDecimal("150000"));
        sistema.depositar("AH-002", new BigDecimal("120000"));
    }

    public void registrarCliente(String documento, String nombre, String telefono, String correo) {
        sistema.registrarCliente(new Cliente(documento, nombre, telefono, correo));
    }

    public Monedero crearMonedero(String documento, String codigo, TipoMonedero tipo) {
        return sistema.crearMonedero(documento, codigo, tipo);
    }

    public Cliente buscarCliente(String documento) {
        return sistema.buscarCliente(documento);
    }

    public Transaccion depositar(String codigo, BigDecimal monto) {
        return sistema.depositar(codigo, monto);
    }

    public Transaccion retirar(String codigo, BigDecimal monto) {
        return sistema.retirar(codigo, monto);
    }

    public Transaccion transferir(String origen, String destino, BigDecimal monto) {
        return sistema.transferir(origen, destino, monto);
    }

    public void revertir(String id) {
        sistema.revertir(id);
    }

    public void canjearBeneficio(String documento, String nombreBeneficio, String codigoMonedero) {
        sistema.canjearBeneficio(documento, nombreBeneficio, codigoMonedero);
    }

    public void programar(String tipo, BigDecimal monto, String origen, String destino,
                          Periodicidad periodicidad, LocalDate fecha) {
        sistema.programar(tipo, monto, origen, destino, periodicidad, fecha);
    }

    public List<String> ejecutarProgramadas(LocalDate fecha) {
        return sistema.ejecutarProgramadas(fecha);
    }

    public double promedioGastos(String documento) {
        return sistema.promedioGastos(documento);
    }

    public double desviacionGastos(String documento) {
        return sistema.desviacionGastos(documento);
    }

    public List<Transaccion> detectarAtipicas(String documento) {
        return sistema.detectarAtipicas(documento);
    }

    public List<Cliente> getClientes() { return sistema.getClientes(); }
    public List<Monedero> getMonederos() { return sistema.getMonederos(); }
    public List<Transaccion> getTransacciones() { return sistema.getTransacciones(); }
    public List<Beneficio> getBeneficios() { return sistema.getBeneficios(); }
    public List<TransaccionProgramada> getProgramadas() { return sistema.getProgramadas(); }
    public List<Alerta> getAlertas() { return sistema.getAlertas(); }
}
