package model;

import exceptions.OperacionInvalidaException;
import exceptions.RecursoNoEncontradoException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class SistemaMonedero {
    private static final BigDecimal UMBRAL_SALDO_BAJO = new BigDecimal("20000");
    private static final BigDecimal COMISION_TRANSFERENCIA = new BigDecimal("0.02");
    private static final BigDecimal COMISION_RETIRO = new BigDecimal("0.01");

    private final Map<String, Cliente> clientes = new LinkedHashMap<>();
    private final Map<String, Monedero> monederos = new LinkedHashMap<>();
    private final List<Transaccion> transacciones = new ArrayList<>();
    private final List<Beneficio> beneficios = new ArrayList<>();
    private final List<TransaccionProgramada> programadas = new ArrayList<>();
    private final List<Alerta> alertas = new ArrayList<>();
    private final List<String> notificaciones = new ArrayList<>();
    private final Set<String> recordatoriosEnviados = new HashSet<>();
    private final List<Notificador> notificadores =
            List.of(new NotificadorCorreo(), new NotificadorWhatsApp(), new NotificadorSMS());
    private final DetectorAtipicas detector = new DetectorAtipicas();
    private final AtomicInteger consecutivo = new AtomicInteger(1);

    public SistemaMonedero() {
        beneficios.add(new Beneficio("Reducción 10% comisión transferencias", 100, 0, 0.10));
        beneficios.add(new Beneficio("Un mes sin cargos por retiros", 500, 0, 0.0, 30));
        beneficios.add(new Beneficio("Bono de saldo de $50", 1000, 50, 0.0));
    }

    // ---------- Clientes y monederos ----------
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

    // ---------- Operaciones ----------
    public Transaccion depositar(String codigo, BigDecimal monto) {
        validarMonto(monto);
        Monedero destino = buscarMonedero(codigo);
        Cliente propietario = propietarioDe(destino);
        destino.acreditar(monto);
        Deposito t = new Deposito(nuevoId(), monto, destino, propietario);
        asignarPuntos(t);
        transacciones.add(t);
        revisarSaldo(destino, propietario);
        return t;
    }

    public Transaccion retirar(String codigo, BigDecimal monto) {
        validarMonto(monto);
        Monedero origen = buscarMonedero(codigo);
        Cliente propietario = propietarioDe(origen);
        BigDecimal comision = comisionRetiro(propietario, monto, LocalDate.now());
        origen.debitar(monto.add(comision));
        Retiro t = new Retiro(nuevoId(), monto, origen, propietario);
        t.setComision(comision);
        asignarPuntos(t);
        transacciones.add(t);
        revisarSaldo(origen, propietario);
        return t;
    }

    public Transaccion transferir(String origenCodigo, String destinoCodigo, BigDecimal monto) {
        validarMonto(monto);
        if (origenCodigo.equals(destinoCodigo))
            throw new OperacionInvalidaException("No puedes transferir al mismo monedero.");
        Monedero origen = buscarMonedero(origenCodigo);
        Monedero destino = buscarMonedero(destinoCodigo);
        Cliente remitente = propietarioDe(origen);
        Cliente receptor = propietarioDe(destino);
        if (remitente == receptor)
            throw new OperacionInvalidaException("La transferencia debe ser entre clientes diferentes.");
        BigDecimal comision = comisionTransferencia(remitente, monto);
        origen.debitar(monto.add(comision));
        destino.acreditar(monto);
        Transferencia t = new Transferencia(nuevoId(), monto, origen, destino, remitente);
        t.setComision(comision);
        asignarPuntos(t);
        transacciones.add(t);
        revisarSaldo(origen, remitente);
        return t;
    }

    /** Comisión de transferencia: 2% del monto, reducida por el rango y por el beneficio canjeado. */
    public BigDecimal comisionTransferencia(Cliente remitente, BigDecimal monto) {
        BigDecimal factor = BigDecimal.ONE.subtract(remitente.getDescuentoComisionTransferencia());
        return monto.multiply(COMISION_TRANSFERENCIA).multiply(factor).setScale(2, RoundingMode.HALF_UP);
    }

    /** Comisión de retiro: 1% del monto, salvo que el cliente tenga el beneficio de retiros sin cargo vigente. */
    public BigDecimal comisionRetiro(Cliente cliente, BigDecimal monto, LocalDate fecha) {
        if (cliente.tieneRetirosSinCargo(fecha)) return BigDecimal.ZERO.setScale(2);
        return monto.multiply(COMISION_RETIRO).setScale(2, RoundingMode.HALF_UP);
    }

    private void asignarPuntos(Transaccion t) {
        int puntos = t.calcularPuntos();
        if (puntos > 0) t.getClientePuntos().sumarPuntos(puntos, t.getTipo(), t.getId());
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
            t.getOrigen().acreditar(t.getMonto().add(t.getComision()));
        } else if (t instanceof Transferencia) {
            t.getDestino().debitar(t.getMonto());
            t.getOrigen().acreditar(t.getMonto().add(t.getComision()));
        }
        Cliente cliente = t.getClientePuntos();
        int aQuitar = Math.min(t.getPuntosOtorgados(), cliente.getPuntos());
        if (aQuitar > 0) cliente.restarPuntos(aQuitar, "Reversión de " + t.getTipo(), t.getId());
        t.marcarReversada();
    }

    // ---------- Puntos y beneficios ----------
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
        cliente.restarPuntos(beneficio.getPuntosNecesarios(), "Canje: " + beneficio.getNombre(), null);
        if (beneficio.getBonoSaldo() > 0)
            monedero.acreditar(new BigDecimal(beneficio.getBonoSaldo()));
        cliente.aplicarBeneficio(beneficio, LocalDate.now());
        crearAlerta(cliente, "Canje exitoso de '" + beneficio.getNombre() + "' para " + cliente.getNombre(), TipoAlerta.CANJE);
    }

    // ---------- Transacciones programadas ----------
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

    public void desactivarProgramada(String id) {
        programadas.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe la transacción programada: " + id))
                .desactivar();
    }

    /**
     * Genera un recordatorio (una sola vez por ejecución programada) para las operaciones activas
     * que se ejecutan hoy o mañana. Devuelve los mensajes generados.
     */
    public List<String> generarRecordatorios(LocalDate hoy) {
        List<String> mensajes = new ArrayList<>();
        programadas.stream()
                .filter(p -> p.isActiva() && !p.getProximaEjecucion().isAfter(hoy.plusDays(1)))
                .sorted(Comparator.comparing(TransaccionProgramada::getProximaEjecucion))
                .forEach(p -> {
                    String clave = p.getId() + "@" + p.getProximaEjecucion();
                    if (!recordatoriosEnviados.add(clave)) return;
                    Monedero referencia = p.getOrigen() != null ? p.getOrigen() : p.getDestino();
                    String mensaje = "Recordatorio: " + p.getTipo() + " programada " + p.getId()
                            + " por $" + p.getMonto() + " para el " + p.getProximaEjecucion() + ".";
                    crearAlerta(propietarioDe(referencia), mensaje, TipoAlerta.RECORDATORIO);
                    mensajes.add(mensaje);
                });
        return mensajes;
    }

    // ---------- Análisis de gastos ----------
    public double promedioGastos(String documento) {
        return detector.promedio(gastosDe(documento));
    }

    public double desviacionGastos(String documento) {
        return detector.desviacion(gastosDe(documento));
    }

    /**
     * Revisa los gastos (retiros y transferencias no reversados) del cliente. Los montos mayores a
     * promedio + 2 x desviación quedan SOSPECHOSOS y generan una alerta (solo la primera vez).
     */
    public List<Transaccion> detectarAtipicas(String documento) {
        Cliente cliente = buscarCliente(documento);
        List<Transaccion> atipicas = detector.detectar(gastosDe(documento));
        atipicas.stream()
                .filter(t -> t.getEstado() == EstadoTransaccion.EXITOSA)
                .forEach(t -> {
                    t.marcarSospechosa();
                    crearAlerta(cliente, "Movimiento atípico detectado [" + t.getId() + "]: $" + t.getMonto(), TipoAlerta.ATIPICA);
                });
        return atipicas;
    }

    private List<Transaccion> gastosDe(String documento) {
        return transacciones.stream()
                .filter(t -> t.getClientePuntos() != null && t.getClientePuntos().getDocumento().equals(documento))
                .filter(t -> t.getEstado() != EstadoTransaccion.REVERSADA)
                .filter(t -> t instanceof Retiro || t instanceof Transferencia)
                .toList();
    }

    // ---------- Alertas y notificaciones (simuladas) ----------
    private void crearAlerta(Cliente cliente, String mensaje, TipoAlerta tipo) {
        Alerta alerta = new Alerta(mensaje, tipo, cliente);
        alertas.add(alerta);
        notificadores.forEach(n -> notificaciones.add(n.enviar(cliente, alerta)));
    }

    private void revisarSaldo(Monedero m, Cliente propietario) {
        if (m.getSaldo().compareTo(UMBRAL_SALDO_BAJO) < 0)
            crearAlerta(propietario, "Saldo bajo en el monedero " + m.getCodigo() + ": $" + m.getSaldo(), TipoAlerta.SALDO_BAJO);
    }

    // ---------- Utilidades ----------
    private void validarMonto(BigDecimal monto) {
        if (monto == null || monto.compareTo(BigDecimal.ZERO) <= 0)
            throw new OperacionInvalidaException("El monto debe ser mayor que cero.");
    }

    private Cliente propietarioDe(Monedero monedero) {
        return clientes.values().stream()
                .filter(c -> c.getMonederos().contains(monedero))
                .findFirst()
                .orElseThrow(() -> new RecursoNoEncontradoException("El monedero no tiene propietario."));
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
    public List<String> getNotificaciones() { return new ArrayList<>(notificaciones); }
}
