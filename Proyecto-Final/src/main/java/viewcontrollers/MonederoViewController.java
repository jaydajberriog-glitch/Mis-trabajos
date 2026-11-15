package viewcontrollers;

import controllers.MonederoController;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import model.*;

import java.math.BigDecimal;
import java.time.LocalDate;

public class MonederoViewController {

    private final MonederoController controller = new MonederoController();

    @FXML private Label lblClientesCount;
    @FXML private Label lblMonederosCount;
    @FXML private Label lblAlertasCount;
    @FXML private TextArea txtSalida;

    // Tab Clientes
    @FXML private TextField txtClienteDoc;
    @FXML private TextField txtClienteNombre;
    @FXML private TextField txtClienteTel;
    @FXML private TextField txtClienteCorreo;

    // Tab Monederos
    @FXML private TextField txtMonDoc;
    @FXML private TextField txtMonCodigo;
    @FXML private ComboBox<TipoMonedero> cmbMonTipo;

    // Tab Operaciones
    @FXML private TextField txtOpOrigen;
    @FXML private TextField txtOpDestino;
    @FXML private TextField txtOpMonto;
    @FXML private TextField txtOpIdReversar;

    // Tab Puntos
    @FXML private TextField txtPtsDoc;
    @FXML private TextField txtPtsMonedero;
    @FXML private ComboBox<Beneficio> cmbPtsBeneficio;

    // Tab Análisis
    @FXML private TextField txtAnalisisDoc;

    // Tab Programadas
    @FXML private ComboBox<String> cmbProgTipo;
    @FXML private TextField txtProgMonto;
    @FXML private ComboBox<Periodicidad> cmbProgPeriodicidad;
    @FXML private DatePicker dpProgFecha;
    @FXML private TextField txtProgOrigen;
    @FXML private TextField txtProgDestino;

    @FXML
    public void initialize() {
        cmbMonTipo.setItems(FXCollections.observableArrayList(TipoMonedero.values()));
        cmbPtsBeneficio.setItems(FXCollections.observableArrayList(controller.getBeneficios()));
        cmbProgTipo.setItems(FXCollections.observableArrayList("DEPÓSITO", "RETIRO", "TRANSFERENCIA"));
        cmbProgPeriodicidad.setItems(FXCollections.observableArrayList(Periodicidad.values()));
        dpProgFecha.setValue(LocalDate.now());

        actualizarResumen();
        mensaje("✔ Bienvenido al Monedero Virtual. Clientes iniciales cargados:");
        onListarClientes();
    }

    private void ejecutar(Runnable accion) {
        int notificacionesPrevias = controller.getNotificaciones().size();
        try {
            accion.run();
            mostrarNotificacionesNuevas(notificacionesPrevias);
            actualizarResumen();
        } catch (NumberFormatException ex) {
            mensaje("✖ Error: Ingrese un valor numérico válido para el monto.");
        } catch (Exception ex) {
            mensaje("✖ Error: " + ex.getMessage());
        }
    }

    private void mostrarNotificacionesNuevas(int desde) {
        var todas = controller.getNotificaciones();
        if (todas.size() > desde) {
            mensaje("📨 Notificaciones simuladas enviadas:");
            todas.subList(desde, todas.size()).forEach(n -> mensaje("   " + n));
        }
    }

    private void actualizarResumen() {
        lblClientesCount.setText("👤 Clientes: " + controller.getClientes().size());
        lblMonederosCount.setText("💳 Monederos: " + controller.getMonederos().size());
        lblAlertasCount.setText("🔔 Alertas: " + controller.getAlertas().size());
    }

    private void mensaje(String texto) {
        txtSalida.appendText(texto + System.lineSeparator());
    }

    private BigDecimal dinero(TextField campo) {
        return new BigDecimal(campo.getText().trim());
    }

    // --- ACCIONES CLIENTES ---
    @FXML
    private void onRegistrarCliente() {
        ejecutar(() -> {
            controller.registrarCliente(txtClienteDoc.getText(), txtClienteNombre.getText(),
                    txtClienteTel.getText(), txtClienteCorreo.getText());
            mensaje("✔ Cliente registrado: " + txtClienteNombre.getText() + " (" + txtClienteDoc.getText() + ")");
            txtClienteDoc.clear(); txtClienteNombre.clear(); txtClienteTel.clear(); txtClienteCorreo.clear();
            onListarClientes();
        });
    }

    @FXML
    private void onListarClientes() {
        ejecutar(() -> {
            mensaje("--- LISTADO DE CLIENTES ---");
            controller.getClientes().forEach(c ->
                    mensaje("• " + c + " | Puntos: " + c.getPuntos() + " | Rango: " + c.getRango()));
        });
    }

    // --- ACCIONES MONEDEROS ---
    @FXML
    private void onCrearMonedero() {
        ejecutar(() -> {
            controller.crearMonedero(txtMonDoc.getText(), txtMonCodigo.getText(), cmbMonTipo.getValue());
            mensaje("✔ Monedero creado: " + txtMonCodigo.getText() + " (" + cmbMonTipo.getValue() + ")");
            txtMonCodigo.clear();
        });
    }

    @FXML
    private void onConsultarMonederos() {
        ejecutar(() -> {
            mensaje("--- SALDOS DE MONEDEROS ---");
            controller.getMonederos().forEach(m -> mensaje("• " + m));
        });
    }

    // --- ACCIONES OPERACIONES ---
    @FXML
    private void onDepositar() {
        ejecutar(() -> {
            var t = controller.depositar(txtOpDestino.getText(), dinero(txtOpMonto));
            mensaje("✔ Depósito exitoso [" + t.getId() + "] por $" + t.getMonto() + " (+" + t.getPuntosOtorgados() + " pts)");
        });
    }

    @FXML
    private void onRetirar() {
        ejecutar(() -> {
            var t = controller.retirar(txtOpOrigen.getText(), dinero(txtOpMonto));
            mensaje("✔ Retiro exitoso [" + t.getId() + "] por $" + t.getMonto() + " | comisión $" + t.getComision()
                    + " (+" + t.getPuntosOtorgados() + " pts)");
        });
    }

    @FXML
    private void onTransferir() {
        ejecutar(() -> {
            var t = controller.transferir(txtOpOrigen.getText(), txtOpDestino.getText(), dinero(txtOpMonto));
            mensaje("✔ Transferencia exitosa [" + t.getId() + "] por $" + t.getMonto() + " | comisión $" + t.getComision()
                    + " (+" + t.getPuntosOtorgados() + " pts)");
        });
    }

    @FXML
    private void onReversar() {
        ejecutar(() -> {
            String id = txtOpIdReversar.getText().trim();
            controller.revertir(id);
            mensaje("✔ Transacción " + id + " reversada. Saldos y puntos restaurados.");
            txtOpIdReversar.clear();
        });
    }

    // --- ACCIONES PUNTOS ---
    @FXML
    private void onConsultarPuntos() {
        ejecutar(() -> {
            Cliente c = controller.buscarCliente(txtPtsDoc.getText());
            mensaje("★ " + c.getNombre() + " | Puntos acumulados: " + c.getPuntos() + " | Rango: " + c.getRango());
        });
    }

    @FXML
    private void onCanjearBeneficio() {
        ejecutar(() -> {
            if (cmbPtsBeneficio.getValue() == null) throw new IllegalArgumentException("Seleccione un beneficio.");
            controller.canjearBeneficio(txtPtsDoc.getText(), cmbPtsBeneficio.getValue().getNombre(), txtPtsMonedero.getText());
            mensaje("✔ Beneficio '" + cmbPtsBeneficio.getValue().getNombre() + "' canjeado con éxito.");
        });
    }

    @FXML
    private void onVerHistorialPuntos() {
        ejecutar(() -> {
            String doc = txtPtsDoc.getText();
            mensaje("--- HISTORIAL DE PUNTOS (" + doc + ") ---");
            var historial = controller.getHistorialPuntos(doc);
            if (historial.isEmpty()) mensaje("Sin movimientos de puntos.");
            else historial.forEach(m -> mensaje("• " + m));
        });
    }

    // --- ACCIONES ANÁLISIS ---
    @FXML
    private void onAnalizarGastos() {
        ejecutar(() -> {
            String doc = txtAnalisisDoc.getText();
            mensaje("--- ANÁLISIS ESTADÍSTICO DE GASTOS (" + doc + ") ---");
            mensaje("• Promedio de gastos: $" + String.format("%.2f", controller.promedioGastos(doc)));
            mensaje("• Desviación estándar: $" + String.format("%.2f", controller.desviacionGastos(doc)));
            var atipicas = controller.detectarAtipicas(doc);
            if (atipicas.isEmpty()) mensaje("✔ No se detectaron movimientos atípicos.");
            else atipicas.forEach(t -> mensaje("⚠ MOVIMIENTO ATÍPICO DETECTADO: " + t));
        });
    }

    // --- ACCIONES PROGRAMADAS ---
    @FXML
    private void onProgramarOperacion() {
        ejecutar(() -> {
            controller.programar(cmbProgTipo.getValue(), dinero(txtProgMonto), txtProgOrigen.getText(),
                    txtProgDestino.getText(), cmbProgPeriodicidad.getValue(), dpProgFecha.getValue());
            mensaje("✔ Transacción programada para el " + dpProgFecha.getValue() + " (" + cmbProgPeriodicidad.getValue() + ")");
        });
    }

    @FXML
    private void onEjecutarProgramadas() {
        ejecutar(() -> {
            mensaje("--- EJECUTANDO PROGRAMADAS ---");
            var res = controller.ejecutarProgramadas(LocalDate.now());
            if (res.isEmpty()) mensaje("No hay transacciones programadas pendientes para hoy.");
            else res.forEach(r -> mensaje("• " + r));
        });
    }

    @FXML
    private void onGenerarRecordatorios() {
        ejecutar(() -> {
            mensaje("--- RECORDATORIOS DE PROGRAMADAS (hoy y mañana) ---");
            var res = controller.generarRecordatorios(LocalDate.now());
            if (res.isEmpty()) mensaje("No hay recordatorios nuevos.");
            else res.forEach(r -> mensaje("🔔 " + r));
        });
    }

    @FXML
    private void onListarProgramadas() {
        ejecutar(() -> {
            mensaje("--- TRANSACCIONES PROGRAMADAS ---");
            controller.getProgramadas().forEach(p -> mensaje("• " + p));
        });
    }

    // --- ACCIONES HISTORIAL ---
    @FXML
    private void onMostrarHistorial() {
        ejecutar(() -> {
            mensaje("--- HISTORIAL DE TRANSACCIONES ---");
            controller.getTransacciones().forEach(t -> mensaje("• " + t));
        });
    }

    @FXML
    private void onMostrarAlertas() {
        ejecutar(() -> {
            mensaje("--- ALERTAS Y NOTIFICACIONES ---");
            if (controller.getAlertas().isEmpty()) mensaje("No hay alertas registradas.");
            else controller.getAlertas().forEach(a -> mensaje("⚠ " + a));
        });
    }

    @FXML
    private void onMostrarNotificaciones() {
        ejecutar(() -> {
            mensaje("--- NOTIFICACIONES SIMULADAS (correo, WhatsApp, SMS) ---");
            if (controller.getNotificaciones().isEmpty()) mensaje("No se han enviado notificaciones.");
            else controller.getNotificaciones().forEach(n -> mensaje("📨 " + n));
        });
    }

    @FXML
    private void onLimpiarConsola() {
        txtSalida.clear();
    }
}