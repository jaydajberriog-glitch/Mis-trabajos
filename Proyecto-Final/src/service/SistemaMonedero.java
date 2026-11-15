package service;

import exception.MonederoException;
import exception.OperacionInvalidaException;
import exception.RecursoNoEncontradoException;
import model.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class SistemaMonedero {
    private final Map<String, Cliente> clientes = new LinkedHashMap<>();
    private final Map<String, Monedero> monederos = new LinkedHashMap<>();
    private final List<Transaccion> transacciones = new ArrayList<>();
    private final List<Beneficio> beneficios = new ArrayList<>();
    private final List<TransaccionProgramada> programadas = new ArrayList<>();
    private final List<Alerta> alertas = new ArrayList<>();
    private final AtomicInteger consecutivo = new AtomicInteger(1);
    private static final BigDecimal UMBRAL_SALDO_BAJO = new BigDecimal("20000");

    public SistemaMonedero() {
        beneficios.add(new Beneficio("Reducción 10% comisión transferencias", 100, 0, 0.10));
        beneficios.add(new Beneficio("Un mes sin cargos por retiros", 500, 0, 0.0));
        beneficios.add(new Beneficio("Bono de saldo de $50", 1000, 50, 0.0));
        beneficios.add(new Beneficio("Bono de saldo de $10.000", 1500, 10000, 0.0));
    }

    public void registrarCliente(Cliente cliente) {
        if (clientes.containsKey(cliente.getDocumento()))
            throw new OperacionInvalidaException("Ya existe un cliente con ese documento.");
        clientes.put(cliente.getDocumento(), cliente);
    }

    public Monedero crearMonedero(String documento, String codigo, TipoMonedero tipo) {
        Cliente cliente = buscarCliente(documento);
        if (monederos.containsKey(codigo))
            throw new OperacionInvalidaException("El código de monedero ya existe.");
        Monedero monedero = new Monedero(codigo, tipo);
        monederos.put(codigo, monedero);
        cliente.agregarMonedero(monedero);
        return monedero;
    }

    public Cliente buscarCliente(String documento) {
        Cliente cliente = clientes.get(documento);
        if (cliente == null)
            throw new RecursoNoEncontradoException("No se encontró el cliente con documento: " + documento);
        return cliente;
    }

    public Monedero buscarMonedero(String codigo) {
        Monedero monedero = monederos.get(codigo);
        if (monedero == null)
            throw new RecursoNoEncontradoException("No se encontró el monedero con código: " + codigo);
        return monedero;
    }

    public Transaccion depositar(String codigo, BigDecimal monto) {
        Monedero destino = buscarMonedero(codigo);
        Cliente propietario = propietarioDe(destino);
        destino.acreditar(monto);
        Deposito t = new Deposito(nuevoId(), monto, destino, propietario);
        asignarPuntos(t);
        transacciones.add(t);
        revisarSaldo(destino);
        return t;
    }

    public Transaccion retirar(String codigo, BigDecimal monto) {
        Monedero origen = buscarMonedero(codigo);
        Cliente propietario = propietarioDe(origen);
        origen.debitar(monto);
        Retiro t = new Retiro(nuevoId(), monto, origen, propietario);
        asignarPuntos(t);
        transacciones.add(t);
        revisarSaldo(origen);
        return t;
    }

    public Transaccion transferir(String origenCodigo, String destinoCodigo, BigDecimal monto) {
        if (origenCodigo.equals(destinoCodigo))
            throw new OperacionInvalidaException("No puedes transferir al mismo monedero.");
        Monedero origen = buscarMonedero(origenCodigo);
        Monedero destino = buscarMonedero(destinoCodigo);
        Cliente remitente = propietarioDe(origen);
        Cliente receptor = propietarioDe(destino);
        if (remitente == receptor)
            throw new OperacionInvalidaException("La transferencia debe ser entre clientes diferentes.");
        origen.debitar(monto);
        destino.acreditar(monto);
        Transferencia t = new Transferencia(nuevoId(), monto, origen, destino, remitente);
        asignarPuntos(t);
        transacciones.add(t);
        revisarSaldo(origen);
        return t;
    }

    private void asignarPuntos(Transaccion t) {
        int puntos = t.calcularPuntos();
        t.getClientePuntos().sumarPuntos(puntos);
        t.setPuntosOtorgados(puntos);
    }

    public void revertir(String id) {
        Transaccion t = transacciones.stream()
                .filter(x -> x.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe la transacción: " + id));
        if (t.getEstado() == EstadoTransaccion.REVERSADA)
            throw new OperacionInvalidaException("La transacción ya fue reversada.");
        if (t instanceof Deposito) {
            t.getDestino().debitar(t.getMonto());
        } else if (t instanceof Retiro) {
            t.getOrigen().acreditar(t.getMonto());
        } else if (t instanceof Transferencia) {
            t.getDestino().debitar(t.getMonto());
            t.getOrigen().acreditar(t.getMonto());
        }
        if (t.getPuntosOtorgados() > 0)
            t.getClientePuntos().restarPuntos(t.getPuntosOtorgados());
        t.marcarReversada();
    }

    public void canjearBeneficio(String documento, String nombreBeneficio, String codigoMonedero) {
        Cliente cliente = buscarCliente(documento);
        Beneficio beneficio = beneficios.stream()
                .filter(b -> b.getNombre().equalsIgnoreCase(nombreBeneficio))
                .findFirst()
                .orElseThrow(() -> new RecursoNoEncontradoException("Beneficio no encontrado."));
        if (cliente.getPuntos() < beneficio.getPuntosNecesarios())
            throw new OperacionInvalidaException("No tienes puntos suficientes para este beneficio.");
        Monedero monedero = buscarMonedero(codigoMonedero);
        if (!cliente.getMonederos().contains(monedero))
            throw new OperacionInvalidaException("El monedero no pertenece al cliente.");
        cliente.restarPuntos(beneficio.getPuntosNecesarios());
        if (beneficio.getBonoSaldo() > 0)
            monedero.acreditar(new BigDecimal(beneficio.getBonoSaldo()));
        alertas.add(new Alerta("Canje exitoso de '" + beneficio.getNombre() + "' para " + cliente.getNombre()));
    }

    public void programar(String tipo, BigDecimal monto, String origen, String destino,
                          Periodicidad periodicidad, LocalDate fecha) {
        Monedero mOrigen = origen == null || origen.isBlank() ? null : buscarMonedero(origen);
        Monedero mDestino = destino == null || destino.isBlank() ? null : buscarMonedero(destino);
        if (tipo == null) throw new OperacionInvalidaException("Debe seleccionar el tipo de operación.");
        if (tipo.equals("RETIRO") && mOrigen == null)
            throw new OperacionInvalidaException("El retiro necesita monedero de origen.");
        if (tipo.equals("DEPÓSITO") && mDestino == null)
            throw new OperacionInvalidaException("El depósito necesita monedero destino.");
        if (tipo.equals("TRANSFERENCIA") && (mOrigen == null || mDestino == null))
            throw new OperacionInvalidaException("La transferencia necesita origen y destino.");
        if (monto == null || monto.compareTo(BigDecimal.ZERO) <= 0 || fecha == null || periodicidad == null)
            throw new OperacionInvalidaException("Datos de programación no válidos.");
        programadas.add(new TransaccionProgramada(nuevoId(), tipo, monto, mOrigen, mDestino, periodicidad, fecha));
    }

    public List<String> ejecutarProgramadas(LocalDate fecha) {
        List<TransaccionProgramada> ordenadas = programadas.stream()
                .filter(p -> p.isActiva() && !p.getProximaEjecucion().isAfter(fecha))
                .sorted(Comparator.comparing(TransaccionProgramada::getProximaEjecucion))
                .collect(Collectors.toList());
        List<String> resultados = new ArrayList<>();
        for (TransaccionProgramada p : ordenadas) {
            try {
                switch (p.getTipo()) {
                    case "DEPÓSITO" -> depositar(p.getDestino().getCodigo(), p.getMonto());
                    case "RETIRO" -> retirar(p.getOrigen().getCodigo(), p.getMonto());
                    case "TRANSFERENCIA" -> transferir(p.getOrigen().getCodigo(), p.getDestino().getCodigo(), p.getMonto());
                    default -> throw new OperacionInvalidaException("Tipo programado desconocido.");
                }
                resultados.add("Ejecutada: " + p.getId());
                p.avanzarFecha();
            } catch (RuntimeException ex) {
                resultados.add("No se pudo ejecutar " + p.getId() + ": " + ex.getMessage());
            }
        }
        return resultados;
    }

    public double promedioGastos(String documento) {
        List<BigDecimal> gastos = transacciones.stream()
                .filter(t -> t.getClientePuntos() != null && t.getClientePuntos().getDocumento().equals(documento))
                .filter(t -> t.getEstado() == EstadoTransaccion.EXITOSA && (t instanceof Retiro || t instanceof Transferencia))
                .map(Transaccion::getMonto)
                .toList();
        return gastos.stream().mapToDouble(BigDecimal::doubleValue).average().orElse(0);
    }

    public double desviacionGastos(String documento) {
        List<Double> valores = transacciones.stream()
                .filter(t -> t.getClientePuntos() != null && t.getClientePuntos().getDocumento().equals(documento))
                .filter(t -> t.getEstado() == EstadoTransaccion.EXITOSA && (t instanceof Retiro || t instanceof Transferencia))
                .map(t -> t.getMonto().doubleValue())
                .toList();
        if (valores.isEmpty()) return 0;
        double media = valores.stream().mapToDouble(Double::doubleValue).average().orElse(0);
        return Math.sqrt(valores.stream().mapToDouble(v -> Math.pow(v - media, 2)).average().orElse(0));
    }

    public List<Transaccion> detectarAtipicas(String documento) {
        buscarCliente(documento);
        List<Transaccion> gastos = transacciones.stream()
                .filter(t -> t.getClientePuntos() != null && t.getClientePuntos().getDocumento().equals(documento))
                .filter(t -> t.getEstado() == EstadoTransaccion.EXITOSA && (t instanceof Retiro || t instanceof Transferencia))
                .toList();
        if (gastos.size() < 2) return List.of();
        double limite = promedioGastos(documento) + 2 * desviacionGastos(documento);
        List<Transaccion> atipicas = gastos.stream()
                .filter(t -> t.getMonto().doubleValue() > limite)
                .toList();
        atipicas.forEach(t -> alertas.add(new Alerta("Movimiento atípico detectado en cliente " + documento + ": $" + t.getMonto())));
        return atipicas;
    }

    private Cliente propietarioDe(Monedero monedero) {
        return clientes.values().stream()
                .filter(c -> c.getMonederos().contains(monedero))
                .findFirst()
                .orElseThrow(() -> new RecursoNoEncontradoException("El monedero no tiene propietario."));
    }

    private void revisarSaldo(Monedero m) {
        if (m.getSaldo().compareTo(UMBRAL_SALDO_BAJO) < 0)
            alertas.add(new Alerta("Saldo bajo en el monedero " + m.getCodigo() + ": $" + m.getSaldo()));
    }

    private String nuevoId() { return "TR-" + consecutivo.getAndIncrement(); }

    public List<Cliente> getClientes() { return new ArrayList<>(clientes.values()); }
    public List<Monedero> getMonederos() { return new ArrayList<>(monederos.values()); }
    public List<Transaccion> getTransacciones() {
        return transacciones.stream().sorted(Comparator.comparing(Transaccion::getFecha).reversed()).toList();
    }
    public List<Beneficio> getBeneficios() { return new ArrayList<>(beneficios); }
    public List<TransaccionProgramada> getProgramadas() {
        return programadas.stream().sorted(Comparator.comparing(TransaccionProgramada::getProximaEjecucion)).toList();
    }
    public List<Alerta> getAlertas() { return new ArrayList<>(alertas); }
}
