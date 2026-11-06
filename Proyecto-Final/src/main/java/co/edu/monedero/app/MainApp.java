package co.edu.monedero.app;

import co.edu.monedero.model.*;
import co.edu.monedero.service.SistemaMonedero;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.math.BigDecimal;
import java.time.LocalDate;

public class MainApp extends Application {
    private final SistemaMonedero sistema = new SistemaMonedero();
    private final TextArea salida = new TextArea();

    @Override public void start(Stage stage) {
        cargarDatosEjemplo();
        TabPane pestañas = new TabPane(
                crearTab("Clientes", panelClientes()),
                crearTab("Monederos", panelMonederos()),
                crearTab("Operaciones", panelOperaciones()),
                crearTab("Puntos", panelPuntos()),
                crearTab("Análisis", panelAnalisis()),
                crearTab("Programadas", panelProgramadas()),
                crearTab("Historial y alertas", panelHistorial())
        );
        salida.setEditable(false);
        salida.setPrefRowCount(7);
        VBox raiz = new VBox(10, encabezado(), pestañas, new Label("Mensajes del sistema"), salida);
        raiz.setPadding(new Insets(15));
        raiz.setStyle("-fx-background-color: #F4F6F5;");
        Scene escena = new Scene(raiz, 1000, 760);
        stage.setTitle("Monedero Virtual");
        stage.setScene(escena);
        stage.show();
        mensaje("Bienvenido. Se cargaron dos clientes de ejemplo para facilitar las pruebas.");
    }

    private
    encabezado() {
        Label titulo = new Label("MONEDERO VIRTUAL");
        titulo.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #183D2B;");
        Label subtitulo = new Label("Administración de dinero, puntos y beneficios");
        subtitulo.setStyle("-fx-text-fill: #52645A;");
        VBox textos = new VBox(3, titulo, subtitulo);
        return new HBox(textos);
    }
    private Tab crearTab(String nombre, javafx.scene.Node contenido) {
        Tab tab = new Tab(nombre, contenido); tab.setClosable(false); return tab;
    }
    private VBox formulario(String titulo, javafx.scene.Node... nodos) {
        VBox caja = new VBox(10);
        caja.setPadding(new Insets(15));
        caja.getChildren().add(new Label(titulo));
        caja.getChildren().addAll(nodos);
        return caja;
    }
    private HBox fila(javafx.scene.Node... nodos) { HBox h = new HBox(8, nodos); h.setPadding(new Insets(4)); return h; }
    private Button boton(String texto, Runnable accion) {
        Button b = new Button(texto);
        b.setOnAction(e -> ejecutar(accion));
        return b;
    }
    private void ejecutar(Runnable accion) {
        try { accion.run(); }
        catch (Exception ex) { mensaje("Error: " + ex.getMessage()); }
    }
    private void mensaje(String texto) { salida.appendText(texto + System.lineSeparator()); }
    private BigDecimal dinero(TextField campo) { return new BigDecimal(campo.getText().trim()); }

    private javafx.scene.Node panelClientes() {
        TextField doc = new TextField(); doc.setPromptText("Documento");
        TextField nombre = new TextField(); nombre.setPromptText("Nombre completo");
        TextField tel = new TextField(); tel.setPromptText("Teléfono");
        TextField correo = new TextField(); correo.setPromptText("Correo");
        Button registrar = boton("Registrar cliente", () -> {
            sistema.registrarCliente(new Cliente(doc.getText(), nombre.getText(), tel.getText(), correo.getText()));
            mensaje("Cliente registrado: " + nombre.getText());
            doc.clear(); nombre.clear(); tel.clear(); correo.clear();
        });
        Button listar = boton("Consultar clientes", () -> sistema.getClientes().forEach(c ->
                mensaje(c + " | Puntos: " + c.getPuntos() + " | Rango: " + c.getRango())));
        return formulario("Registro y consulta de clientes", fila(doc, nombre), fila(tel, correo), registrar, listar);
    }
    private javafx.scene.Node panelMonederos() {
        TextField doc = new TextField(); doc.setPromptText("Documento del cliente");
        TextField codigo = new TextField(); codigo.setPromptText("Código del monedero");
        ComboBox<TipoMonedero> tipo = new ComboBox<>(FXCollections.observableArrayList(TipoMonedero.values()));
        tipo.setPromptText("Tipo");
        Button crear = boton("Crear monedero", () -> {
            sistema.crearMonedero(doc.getText(), codigo.getText(), tipo.getValue());
            mensaje("Monedero creado: " + codigo.getText());
        });
        Button consultar = boton("Consultar saldos", () -> sistema.getMonederos().forEach(m -> mensaje(m.toString())));
        return formulario("Administración de monederos", fila(doc, codigo, tipo), fila(crear, consultar));
    }
    private javafx.scene.Node panelOperaciones() {
        TextField origen = new TextField(); origen.setPromptText("Monedero origen");
        TextField destino = new TextField(); destino.setPromptText("Monedero destino");
        TextField monto = new TextField(); monto.setPromptText("Monto");
        Button deposito = boton("Depositar", () -> {
            sistema.depositar(destino.getText(), dinero(monto)); mensaje("Depósito realizado correctamente.");
        });
        Button retiro = boton("Retirar", () -> {
            sistema.retirar(origen.getText(), dinero(monto)); mensaje("Retiro realizado correctamente.");
        });
        Button transferencia = boton("Transferir", () -> {
            sistema.transferir(origen.getText(), destino.getText(), dinero(monto)); mensaje("Transferencia realizada correctamente.");
        });
        TextField id = new TextField(); id.setPromptText("ID de transacción para reversar");
        Button reversar = boton("Reversar", () -> { sistema.revertir(id.getText()); mensaje("Transacción reversada."); });
        return formulario("Operaciones financieras", fila(origen, destino), fila(monto),
                fila(deposito, retiro, transferencia), new Separator(), fila(id, reversar));
    }
    private javafx.scene.Node panelPuntos() {
        TextField doc = new TextField(); doc.setPromptText("Documento del cliente");
        TextField codigo = new TextField(); codigo.setPromptText("Código de monedero");
        ComboBox<Beneficio> beneficio = new ComboBox<>(FXCollections.observableArrayList(sistema.getBeneficios()));
        beneficio.setPromptText("Beneficio");
        Button consultar = boton("Consultar puntos y rango", () -> {
            Cliente c = sistema.buscarCliente(doc.getText());
            mensaje(c.getNombre() + " | Puntos: " + c.getPuntos() + " | Rango: " + c.getRango());
        });
        Button canjear = boton("Canjear beneficio", () -> {
            sistema.canjearBeneficio(doc.getText(), beneficio.getValue().getNombre(), codigo.getText());
            mensaje("Beneficio canjeado correctamente.");
        });
        return formulario("Puntos y beneficios", fila(doc, consultar), fila(codigo, beneficio, canjear),
                new Label("Los depósitos y las transferencias enviadas generan 1 punto por cada $10.000 completos."));
    }
    private javafx.scene.Node panelAnalisis() {
        TextField doc = new TextField(); doc.setPromptText("Documento del cliente");
        Button analizar = boton("Analizar gastos", () -> {
            mensaje("Promedio de gastos: $" + String.format("%.2f", sistema.promedioGastos(doc.getText())));
            mensaje("Desviación estándar: " + String.format("%.2f", sistema.desviacionGastos(doc.getText())));
            sistema.detectarAtipicas(doc.getText()).forEach(t -> mensaje("Movimiento atípico: " + t));
        });
        return formulario("Análisis de patrones y movimientos atípicos", fila(doc, analizar),
                new Label("Se analizan retiros y transferencias exitosas. Se requieren al menos dos movimientos."));
    }
    private javafx.scene.Node panelProgramadas() {
        ComboBox<String> tipo = new ComboBox<>(FXCollections.observableArrayList("DEPÓSITO", "RETIRO", "TRANSFERENCIA"));
        tipo.setPromptText("Tipo");
        TextField origen = new TextField(); origen.setPromptText("Origen (si aplica)");
        TextField destino = new TextField(); destino.setPromptText("Destino (si aplica)");
        TextField monto = new TextField(); monto.setPromptText("Monto");
        ComboBox<Periodicidad> periodicidad = new ComboBox<>(FXCollections.observableArrayList(Periodicidad.values()));
        periodicidad.setPromptText("Periodicidad");
        DatePicker fecha = new DatePicker(LocalDate.now());
        Button programar = boton("Programar operación", () -> {
            sistema.programar(tipo.getValue(), dinero(monto), origen.getText(), destino.getText(), periodicidad.getValue(), fecha.getValue());
            mensaje("Transacción programada.");
        });
        Button ejecutar = boton("Ejecutar pendientes hasta hoy", () ->
                sistema.ejecutarProgramadas(LocalDate.now()).forEach(this::mensaje));
        Button listar = boton("Listar programadas", () -> sistema.getProgramadas().forEach(p -> mensaje(p.toString())));
        return formulario("Programación de transacciones", fila(tipo, monto, periodicidad, fecha),
                fila(origen, destino), fila(programar, ejecutar, listar));
    }
    private javafx.scene.Node panelHistorial() {
        Button historial = boton("Mostrar historial", () -> sistema.getTransacciones().forEach(t -> mensaje(t.toString())));
        Button alertas = boton("Mostrar alertas", () -> sistema.getAlertas().forEach(a -> mensaje(a.toString())));
        return formulario("Historial y notificaciones", fila(historial, alertas));
    }
    private void cargarDatosEjemplo() {
        Cliente ana = new Cliente("1001", "Ana Pérez", "3001112233", "ana@email.com");
        Cliente luis = new Cliente("1002", "Luis Gómez", "3004445566", "luis@email.com");
        sistema.registrarCliente(ana); sistema.registrarCliente(luis);
        sistema.crearMonedero("1001", "AH-001", TipoMonedero.AHORROS);
        sistema.crearMonedero("1001", "GD-001", TipoMonedero.GASTOS_DIARIOS);
        sistema.crearMonedero("1002", "AH-002", TipoMonedero.AHORROS);
        sistema.depositar("AH-001", new BigDecimal("150000"));
        sistema.depositar("AH-002", new BigDecimal("120000"));
    }
    public static void main(String[] args) { launch(args); }
}
