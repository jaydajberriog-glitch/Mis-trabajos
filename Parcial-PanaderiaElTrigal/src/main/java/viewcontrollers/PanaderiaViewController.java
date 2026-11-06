package viewcontrollers;

import controllers.PanaderiaController;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import model.Cajero;
import model.Empleado;
import model.Panadero;
import model.Pedido;
import model.TipoPan;

public class PanaderiaViewController {

    private PanaderiaController controller;

    @FXML
    private TextField txtCodigo;

    @FXML
    private TextField txtCliente;

    @FXML
    private ComboBox<TipoPan> cmbTipoPan;

    @FXML
    private TextField txtCantidad;

    @FXML
    private TableView<Pedido> tblPedidos;

    @FXML
    private TableView<Empleado> tblEmpleados;

    @FXML
    public void initialize() {
        cmbTipoPan.setItems(FXCollections.observableArrayList(TipoPan.values()));
        cmbTipoPan.setValue(TipoPan.TRADICIONAL);

        configurarTablaPedidos();
        configurarTablaEmpleados();
    }

    public void setPanaderiaController(PanaderiaController controller) {
        this.controller = controller;
        actualizarTablas();
    }

    @FXML
    private void onRegistrarPedido() {
        try {
            asegurarController();

            int cantidad = Integer.parseInt(txtCantidad.getText().trim());
            controller.registrarPedido(
                    txtCodigo.getText(),
                    txtCliente.getText(),
                    cmbTipoPan.getValue(),
                    cantidad
            );

            mostrarAlerta("Pedido registrado", "El pedido quedó registrado en estado PENDIENTE.");
            limpiarFormulario();
            actualizarTablas();
        } catch (NumberFormatException e) {
            mostrarError("La cantidad debe ser un número entero.");
        } catch (IllegalArgumentException | IllegalStateException e) {
            mostrarError(e.getMessage());
        }
    }

    @FXML
    private void onAsignarPedido() {
        try {
            asegurarController();
            Pedido pedido = controller.asignarPedido(txtCodigo.getText());

            mostrarAlerta(
                    "Pedido asignado",
                    "Pedido asignado al panadero " + pedido.getCodigoPanaderoAsignado()
                            + ". Tiempo reservado: " + pedido.getMinutosReservados() + " minutos."
            );
            actualizarTablas();
        } catch (IllegalArgumentException | IllegalStateException e) {
            mostrarError(e.getMessage());
        }
    }

    @FXML
    private void onCancelarPedido() {
        try {
            asegurarController();
            controller.cancelarPedido(txtCodigo.getText());

            mostrarAlerta("Pedido cancelado", "El pedido fue cancelado correctamente.");
            actualizarTablas();
        } catch (IllegalArgumentException | IllegalStateException e) {
            mostrarError(e.getMessage());
        }
    }

    private void configurarTablaPedidos() {
        TableColumn<Pedido, String> codigo = new TableColumn<>("Código");
        codigo.setCellValueFactory(dato ->
                new SimpleStringProperty(dato.getValue().getCodigo()));

        TableColumn<Pedido, String> cliente = new TableColumn<>("Cliente");
        cliente.setCellValueFactory(dato ->
                new SimpleStringProperty(dato.getValue().getNombreCliente()));

        TableColumn<Pedido, String> tipo = new TableColumn<>("Tipo de pan");
        tipo.setCellValueFactory(dato ->
                new SimpleStringProperty(dato.getValue().getTipoPan().toString()));

        TableColumn<Pedido, String> cantidad = new TableColumn<>("Cantidad");
        cantidad.setCellValueFactory(dato ->
                new SimpleStringProperty(String.valueOf(dato.getValue().getCantidad())));

        TableColumn<Pedido, String> estado = new TableColumn<>("Estado");
        estado.setCellValueFactory(dato ->
                new SimpleStringProperty(dato.getValue().getEstado().toString()));

        TableColumn<Pedido, String> panadero = new TableColumn<>("Panadero");
        panadero.setCellValueFactory(dato ->
                new SimpleStringProperty(dato.getValue().getCodigoPanaderoAsignado() == null
                        ? "-" : dato.getValue().getCodigoPanaderoAsignado()));

        TableColumn<Pedido, String> minutos = new TableColumn<>("Minutos");
        minutos.setCellValueFactory(dato ->
                new SimpleStringProperty(String.valueOf(dato.getValue().getMinutosReservados())));

        tblPedidos.getColumns().setAll(codigo, cliente, tipo, cantidad, estado, panadero, minutos);
    }

    private void configurarTablaEmpleados() {
        TableColumn<Empleado, String> codigo = new TableColumn<>("Código");
        codigo.setCellValueFactory(dato ->
                new SimpleStringProperty(dato.getValue().getCodigo()));

        TableColumn<Empleado, String> nombre = new TableColumn<>("Nombre");
        nombre.setCellValueFactory(dato ->
                new SimpleStringProperty(dato.getValue().getNombre()));

        TableColumn<Empleado, String> tipo = new TableColumn<>("Cargo");
        tipo.setCellValueFactory(dato -> {
            Empleado empleado = dato.getValue();
            String cargo;

            if (empleado instanceof Panadero) {
                cargo = empleado instanceof model.PanaderoArtesanal
                        ? "Panadero artesanal" : "Panadero industrial";
            } else if (empleado instanceof Cajero) {
                cargo = "Cajero";
            } else {
                cargo = "Empleado";
            }

            return new SimpleStringProperty(cargo);
        });

        TableColumn<Empleado, String> minutos = new TableColumn<>("Minutos disponibles");
        minutos.setCellValueFactory(dato -> {
            if (dato.getValue() instanceof Panadero panadero) {
                return new SimpleStringProperty(String.valueOf(panadero.getMinutosDisponibles()));
            }
            return new SimpleStringProperty("No aplica");
        });

        tblEmpleados.getColumns().setAll(codigo, nombre, tipo, minutos);
    }

    private void actualizarTablas() {
        if (controller == null) {
            return;
        }

        tblPedidos.setItems(FXCollections.observableArrayList(controller.obtenerPedidos()));
        tblEmpleados.setItems(FXCollections.observableArrayList(controller.obtenerEmpleados()));
    }

    private void limpiarFormulario() {
        txtCodigo.clear();
        txtCliente.clear();
        txtCantidad.clear();
        cmbTipoPan.setValue(TipoPan.TRADICIONAL);
    }

    private void asegurarController() {
        if (controller == null) {
            throw new IllegalStateException("La pantalla todavía no está conectada con la panadería.");
        }
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }

    private void mostrarError(String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.ERROR);
        alerta.setTitle("No se pudo realizar la operación");
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}
