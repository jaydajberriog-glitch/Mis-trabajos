package app;

import model.*;
import service.SistemaMonedero;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.math.BigDecimal;
import java.time.LocalDate;

public class MainApp extends Application {
    private final SistemaMonedero sistema = new SistemaMonedero();
    private final TextArea salida = new TextArea();
    private Label lblClientesCount;
    private Label lblMonederosCount;
    private Label lblAlertasCount;

    // Paleta de colores Fintech moderna (Verde Esmeralda + Dorado + Gris Pizarra)
    private static final String PRIMARIO = "#1B4332";
    private static final String SECUNDARIO = "#2D6A4F";
    private static final String ACENTO = "#40916C";
    private static final String DORADO = "#D97706";
    private static final String FONDO = "#EEF3F0";
    private static final String TARJETA = "#FFFFFF";
    private static final String BORDE = "#D5E0DA";

    @Override
    public void start(Stage stage) {
        cargarDatosEjemplo();

        TabPane pestanas = new TabPane(
                crearTab("👤 Clientes", panelClientes()),
                crearTab("💳 Monederos", panelMonederos()),
                crearTab("💸 Operaciones", panelOperaciones()),
                crearTab("⭐ Puntos y Rangos", panelPuntos()),
                crearTab("📊 Análisis", panelAnalisis()),
                crearTab("🗓 Programadas", panelProgramadas()),
                crearTab("🔔 Historial y Alertas", panelHistorial())
        );
        pestanas.setStyle("-fx-background-color: transparent; -fx-font-size: 13px;");

        salida.setEditable(false);
        salida.setPrefRowCount(8);
        salida.setWrapText(true);
        salida.setStyle(
                "-fx-control-inner-background: #102A20;" +
                "-fx-text-fill: #D8F3DC;" +
                "-fx-font-family: 'Consolas', 'Monospaced';" +
                "-fx-font-size: 13px;" +
                "-fx-background-radius: 10;" +
                "-fx-border-radius: 10;" +
                "-fx-border-color: #2D6A4F;" +
                "-fx-border-width: 1.5;"
        );

        HBox barraConsola = new HBox(10);
        barraConsola.setAlignment(Pos.CENTER_LEFT);
        Label lblConsola = new Label("📋 Consola de resultados y actividad");
        lblConsola.setStyle("-fx-font-weight: bold; -fx-font-size: 14px; -fx-text-fill: " + PRIMARIO + ";");
        Region espaciador = new Region();
        HBox.setHgrow(espaciador, Priority.ALWAYS);
        Button btnLimpiar = botonSecundario("🧹 Limpiar consola", () -> salida.clear());
        barraConsola.getChildren().addAll(lblConsola, espaciador, btnLimpiar);

        VBox raiz = new VBox(14, encabezado(), pestanas, barraConsola, salida);
        raiz.setPadding(new Insets(18));
        raiz.setStyle("-fx-background-color: " + FONDO + "; -fx-font-family: 'Segoe UI', sans-serif;");

        Scene escena = new Scene(raiz, 1060, 800);
        stage.setTitle("Monedero Virtual - Sistema Financiero y de Puntos");
        stage.setScene(escena);
        stage.show();

        actualizarResumen();
        mensaje("✔ Bienvenido al Monedero Virtual. Se cargaron clientes de ejemplo (1001 y 1002) listos para probar.");
    }

    private VBox encabezado() {
        Label titulo = new Label("💰 MONEDERO VIRTUAL");
        titulo.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: white;");
        Label subtitulo = new Label("Gestión inteligente de cuentas, transferencias, puntos y análisis de gastos");
        subtitulo.setStyle("-fx-font-size: 13px; -fx-text-fill: #B7E4C7;");
        VBox textos = new VBox(4, titulo, subtitulo);

        lblClientesCount = crearBadge("Clientes: 0", "#2D6A4F");
        lblMonederosCount = crearBadge("Monederos: 0", "#2D6A4F");
        lblAlertasCount = crearBadge("Alertas: 0", DORADO);

        HBox badges = new HBox(10, lblClientesCount, lblMonederosCount, lblAlertasCount);
        badges.setAlignment(Pos.CENTER_RIGHT);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox filaSuperior = new HBox(15, textos, spacer, badges);
        filaSuperior.setAlignment(Pos.CENTER_LEFT);

        VBox banner = new VBox(filaSuperior);
        banner.setPadding(new Insets(18, 22, 18, 22));
        banner.setStyle(
                "-fx-background-color: linear-gradient(to right, " + PRIMARIO + ", " + SECUNDARIO + ");" +
                "-fx-background-radius: 12;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.18), 10, 0, 0, 3);"
        );
        return banner;
    }

    private Label crearBadge(String texto, String colorFondo) {
        Label badge = new Label(texto);
        badge.setStyle(
                "-fx-background-color: " + colorFondo + ";" +
                "-fx-text-fill: white;" +
                "-fx-font-weight: bold;" +
                "-fx-font-size: 12px;" +
                "-fx-padding: 6 12 6 12;" +
                "-fx-background-radius: 20;"
        );
        return badge;
    }

    private void actualizarResumen() {
        if (lblClientesCount != null) lblClientesCount.setText("👤 Clientes: " + sistema.getClientes().size());
        if (lblMonederosCount != null) lblMonederosCount.setText("💳 Monederos: " + sistema.getMonederos().size());
        if (lblAlertasCount != null) lblAlertasCount.setText("🔔 Alertas: " + sistema.getAlertas().size());
    }

    private Tab crearTab(String nombre, Node contenido) {
        Tab tab = new Tab(nombre, contenido);
        tab.setClosable(false);
        return tab;
    }

    private VBox tarjetaFormulario(String titulo, String descripcion, Node... nodos) {
        VBox caja = new VBox(12);
        caja.setPadding(new Insets(20));
        caja.setStyle(
                "-fx-background-color: " + TARJETA + ";" +
                "-fx-background-radius: 12;" +
                "-fx-border-color: " + BORDE + ";" +
                "-fx-border-radius: 12;" +
                "-fx-border-width: 1;" +
                "-fx-effect: dropshadow(gaussian, rgba(27,67,50,0.08), 8, 0, 0, 2);"
        );

        Label lblTitulo = new Label(titulo);
        lblTitulo.setStyle("-fx-font-size: 17px; -fx-font-weight: bold; -fx-text-fill: " + PRIMARIO + ";");

        Label lblDesc = new Label(descripcion);
        lblDesc.setStyle("-fx-font-size: 12.5px; -fx-text-fill: #5C7065;");

        VBox cabecera = new VBox(3, lblTitulo, lblDesc, new Separator());
        caja.getChildren().add(cabecera);
        caja.getChildren().addAll(nodos);

        VBox contenedorExterno = new VBox(caja);
        contenedorExterno.setPadding(new Insets(12, 2, 4, 2));
        return contenedorExterno;
    }

    private HBox fila(Node... nodos) {
        HBox h = new HBox(10, nodos);
        h.setAlignment(Pos.CENTER_LEFT);
        h.setPadding(new Insets(3, 0, 3, 0));
        return h;
    }

    private TextField campoTexto(String prompt, double ancho) {
        TextField tf = new TextField();
        tf.setPromptText(prompt);
        tf.setPrefWidth(ancho);
        tf.setStyle(
                "-fx-background-color: #F8FAF9;" +
                "-fx-border-color: #C4D4CC;" +
                "-fx-border-radius: 7;" +
                "-fx-background-radius: 7;" +
                "-fx-padding: 8 10 8 10;" +
                "-fx-font-size: 13px;"
        );
        return tf;
    }

    private <T> ComboBox<T> comboEstilizado(String prompt, double ancho) {
        ComboBox<T> cb = new ComboBox<>();
        cb.setPromptText(prompt);
        cb.setPrefWidth(ancho);
        cb.setStyle(
                "-fx-background-color: #F8FAF9;" +
                "-fx-border-color: #C4D4CC;" +
                "-fx-border-radius: 7;" +
                "-fx-background-radius: 7;" +
                "-fx-padding: 4 6 4 6;" +
                "-fx-font-size: 13px;"
        );
        return cb;
    }

    private Button botonPrimario(String texto, Runnable accion) {
        return crearBotonConColor(texto, SECUNDARIO, accion);
    }

    private Button botonSecundario(String texto, Runnable accion) {
        return crearBotonConColor(texto, "#4A6357", accion);
    }

    private Button botonAcento(String texto, Runnable accion) {
        return crearBotonConColor(texto, DORADO, accion);
    }

    private Button botonPeligro(String texto, Runnable accion) {
        return crearBotonConColor(texto, "#B91C1C", accion);
    }

    private Button crearBotonConColor(String texto, String colorHex, Runnable accion) {
        Button b = new Button(texto);
        String estiloBase =
                "-fx-background-color: " + colorHex + ";" +
                "-fx-text-fill: white;" +
                "-fx-font-weight: bold;" +
                "-fx-font-size: 13px;" +
                "-fx-padding: 8 16 8 16;" +
                "-fx-background-radius: 7;" +
                "-fx-cursor: hand;";
        b.setStyle(estiloBase);
        b.setOnMouseEntered(e -> b.setOpacity(0.88));
        b.setOnMouseExited(e -> b.setOpacity(1.0));
        b.setOnAction(e -> ejecutar(accion));
        return b;
    }

    private void ejecutar(Runnable accion) {
        try {
            accion.run();
            actualizarResumen();
        } catch (NumberFormatException ex) {
            mensaje("✖ Error: Ingrese un valor numérico válido para el monto.");
        } catch (Exception ex) {
            mensaje("✖ Error: " + ex.getMessage());
        }
    }

    private void mensaje(String texto) {
        salida.appendText(texto + System.lineSeparator());
    }

    private BigDecimal dinero(TextField campo) {
        return new BigDecimal(campo.getText().trim());
    }

    private Node panelClientes() {
        TextField doc = campoTexto("Documento de identidad", 190);
        TextField nombre = campoTexto("Nombre completo", 260);
        TextField tel = campoTexto("Teléfono", 190);
        TextField correo = campoTexto("Correo electrónico", 260);

        Button registrar = botonPrimario("➕ Registrar cliente", () -> {
            sistema.registrarCliente(new Cliente(doc.getText(), nombre.getText(), tel.getText(), correo.getText()));
            mensaje("✔ Cliente registrado: " + nombre.getText() + " (" + doc.getText() + ")");
            doc.clear(); nombre.clear(); tel.clear(); correo.clear();
        });

        Button listar = botonSecundario("📋 Consultar todos los clientes", () -> {
            mensaje("--- LISTADO DE CLIENTES ---");
            sistema.getClientes().forEach(c ->
                    mensaje("• " + c + " | Puntos: " + c.getPuntos() + " | Rango: " + c.getRango()));
        });

        return tarjetaFormulario(
                "Registro y Consulta de Clientes",
                "Crea nuevos usuarios en el sistema o consulta sus puntos y categoría actual.",
                fila(doc, nombre),
                fila(tel, correo),
                fila(registrar, listar)
        );
    }

    private Node panelMonederos() {
        TextField doc = campoTexto("Documento del cliente", 200);
        TextField codigo = campoTexto("Código (ej: AH-003)", 200);
        ComboBox<TipoMonedero> tipo = comboEstilizado("Tipo de monedero", 210);
        tipo.setItems(FXCollections.observableArrayList(TipoMonedero.values()));

        Button crear = botonPrimario("➕ Crear monedero", () -> {
            sistema.crearMonedero(doc.getText(), codigo.getText(), tipo.getValue());
            mensaje("✔ Monedero creado: " + codigo.getText() + " (" + tipo.getValue() + ")");
            codigo.clear();
        });

        Button consultar = botonSecundario("💰 Consultar saldos", () -> {
            mensaje("--- SALDOS DE MONEDEROS ---");
            sistema.getMonederos().forEach(m -> mensaje("• " + m));
        });

        return tarjetaFormulario(
                "Administración de Monederos",
                "Asocia bolsillos de Ahorros o Gastos Diarios a un cliente existente.",
                fila(doc, codigo, tipo),
                fila(crear, consultar)
        );
    }

    private Node panelOperaciones() {
        TextField origen = campoTexto("Monedero origen (Retiro/Transf.)", 230);
        TextField destino = campoTexto("Monedero destino (Depósito/Transf.)", 240);
        TextField monto = campoTexto("Monto ($)", 180);

        Button deposito = botonPrimario("⬇ Depositar", () -> {
            var t = sistema.depositar(destino.getText(), dinero(monto));
            mensaje("✔ Depósito exitoso [" + t.getId() + "] por $" + t.getMonto() + " (+" + t.getPuntosOtorgados() + " pts)");
        });

        Button retiro = botonAcento("⬆ Retirar", () -> {
            var t = sistema.retirar(origen.getText(), dinero(monto));
            mensaje("✔ Retiro exitoso [" + t.getId() + "] por $" + t.getMonto() + " (+" + t.getPuntosOtorgados() + " pts)");
        });

        Button transferencia = botonSecundario("⇄ Transferir", () -> {
            var t = sistema.transferir(origen.getText(), destino.getText(), dinero(monto));
            mensaje("✔ Transferencia exitosa [" + t.getId() + "] por $" + t.getMonto() + " (+" + t.getPuntosOtorgados() + " pts)");
        });

        TextField id = campoTexto("ID transacción (ej: TR-1)", 230);
        Button reversar = botonPeligro("↩ Reversar operación", () -> {
            sistema.revertir(id.getText().trim());
            mensaje("✔ Transacción " + id.getText().trim() + " reversada. Saldos y puntos restaurados.");
            id.clear();
        });

        return tarjetaFormulario(
                "Operaciones Financieras",
                "Realiza depósitos, retiros, transferencias entre clientes o reversa movimientos por ID.",
                fila(origen, destino, monto),
                fila(deposito, retiro, transferencia),
                new Separator(),
                fila(id, reversar)
        );
    }

    private Node panelPuntos() {
        TextField doc = campoTexto("Documento del cliente", 200);
        TextField codigo = campoTexto("Código de monedero destino", 220);
        ComboBox<Beneficio> beneficio = comboEstilizado("Selecciona un beneficio", 320);
        beneficio.setItems(FXCollections.observableArrayList(sistema.getBeneficios()));

        Button consultar = botonSecundario("⭐ Consultar puntos y rango", () -> {
            Cliente c = sistema.buscarCliente(doc.getText());
            mensaje("★ " + c.getNombre() + " | Puntos acumulados: " + c.getPuntos() + " | Rango: " + c.getRango());
        });

        Button canjear = botonAcento("🎁 Canjear beneficio", () -> {
            if (beneficio.getValue() == null) throw new IllegalArgumentException("Seleccione un beneficio.");
            sistema.canjearBeneficio(doc.getText(), beneficio.getValue().getNombre(), codigo.getText());
            mensaje("✔ Beneficio '" + beneficio.getValue().getNombre() + "' canjeado con éxito.");
        });

        Label nota = new Label("ℹ Regla de puntos: Depósitos = 1 pt | Retiros = 2 pts | Transferencias = 3 pts por cada $100.");
        nota.setStyle("-fx-background-color: #E6F4EA; -fx-text-fill: #1B4332; -fx-padding: 8 12 8 12; -fx-background-radius: 6; -fx-font-weight: bold;");

        return tarjetaFormulario(
                "Programa de Puntos, Rangos y Beneficios",
                "Consulta tu nivel (Bronce, Plata, Oro, Platino) y redime tus puntos acumulados.",
                fila(doc, consultar),
                fila(codigo, beneficio, canjear),
                nota
        );
    }

    private Node panelAnalisis() {
        TextField doc = campoTexto("Documento del cliente", 220);
        Button analizar = botonPrimario("📊 Analizar patrones de gasto", () -> {
            mensaje("--- ANÁLISIS ESTADÍSTICO DE GASTOS (" + doc.getText() + ") ---");
            mensaje("• Promedio de gastos: $" + String.format("%.2f", sistema.promedioGastos(doc.getText())));
            mensaje("• Desviación estándar: $" + String.format("%.2f", sistema.desviacionGastos(doc.getText())));
            var atipicas = sistema.detectarAtipicas(doc.getText());
            if (atipicas.isEmpty()) mensaje("✔ No se detectaron movimientos atípicos.");
            else atipicas.forEach(t -> mensaje("⚠ MOVIMIENTO ATÍPICO DETECTADO: " + t));
        });

        Label info = new Label("ℹ Se evalúan retiros y transferencias exitosas. Un movimiento es atípico si supera: Promedio + 2 × Desviación Estándar.");
        info.setStyle("-fx-background-color: #FEF3C7; -fx-text-fill: #92400E; -fx-padding: 8 12 8 12; -fx-background-radius: 6;");

        return tarjetaFormulario(
                "Análisis de Patrones y Detección de Movimientos Atípicos",
                "Identifica gastos inusuales comparando el historial del cliente mediante desviación estándar.",
                fila(doc, analizar),
                info
        );
    }

    private Node panelProgramadas() {
        ComboBox<String> tipo = comboEstilizado("Tipo de operación", 180);
        tipo.setItems(FXCollections.observableArrayList("DEPÓSITO", "RETIRO", "TRANSFERENCIA"));
        TextField monto = campoTexto("Monto ($)", 150);
        ComboBox<Periodicidad> periodicidad = comboEstilizado("Periodicidad", 160);
        periodicidad.setItems(FXCollections.observableArrayList(Periodicidad.values()));
        DatePicker fecha = new DatePicker(LocalDate.now());
        fecha.setPrefWidth(160);

        TextField origen = campoTexto("Monedero origen (si aplica)", 220);
        TextField destino = campoTexto("Monedero destino (si aplica)", 220);

        Button programar = botonPrimario("🗓 Programar operación", () -> {
            sistema.programar(tipo.getValue(), dinero(monto), origen.getText(), destino.getText(), periodicidad.getValue(), fecha.getValue());
            mensaje("✔ Transacción programada para el " + fecha.getValue() + " (" + periodicidad.getValue() + ")");
        });

        Button ejecutar = botonAcento("▶ Ejecutar pendientes hasta hoy", () -> {
            mensaje("--- EJECUTANDO PROGRAMADAS ---");
            var res = sistema.ejecutarProgramadas(LocalDate.now());
            if (res.isEmpty()) mensaje("No hay transacciones programadas pendientes para hoy.");
            else res.forEach(r -> mensaje("• " + r));
        });

        Button listar = botonSecundario("📋 Listar programadas", () -> {
            mensaje("--- TRANSACCIONES PROGRAMADAS ---");
            sistema.getProgramadas().forEach(p -> mensaje("• " + p));
        });

        return tarjetaFormulario(
                "Programación de Transacciones Recurrentes",
                "Agenda depósitos, retiros o transferencias automáticas ordenadas por fecha de ejecución.",
                fila(tipo, monto, periodicidad, fecha),
                fila(origen, destino),
                fila(programar, ejecutar, listar)
        );
    }

    private Node panelHistorial() {
        Button historial = botonPrimario("📜 Mostrar historial de transacciones", () -> {
            mensaje("--- HISTORIAL DE TRANSACCIONES ---");
            sistema.getTransacciones().forEach(t -> mensaje("• " + t));
        });

        Button alertas = botonAcento("🔔 Mostrar alertas del sistema", () -> {
            mensaje("--- ALERTAS Y NOTIFICACIONES ---");
            if (sistema.getAlertas().isEmpty()) mensaje("No hay alertas registradas.");
            else sistema.getAlertas().forEach(a -> mensaje("⚠ " + a));
        });

        return tarjetaFormulario(
                "Historial de Movimientos y Centro de Alertas",
                "Consulta todas las operaciones ordenadas por fecha y las notificaciones de saldo bajo o actividad inusual.",
                fila(historial, alertas)
        );
    }

    private void cargarDatosEjemplo() {
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

    public static void main(String[] args) { launch(args); }
}
