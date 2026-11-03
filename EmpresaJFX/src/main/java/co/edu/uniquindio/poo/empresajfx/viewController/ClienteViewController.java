package co.edu.uniquindio.poo.empresajfx.viewController;

import co.edu.uniquindio.poo.empresajfx.app.App;
import co.edu.uniquindio.poo.empresajfx.controller.ClienteController;
import co.edu.uniquindio.poo.empresajfx.model.Cliente;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class ClienteViewController {

    private ClienteController clienteController;
    private ObservableList<Cliente> listClientes = FXCollections.observableArrayList();
    private Cliente selectedCliente;
    private App app;

    @FXML private TextField txtCedula;
    @FXML private TextField txtNombre;
    @FXML private TextField txtApellido;

    @FXML private TableView<Cliente> tblListCliente;
    @FXML private TableColumn<Cliente, String> tbcCedula;
    @FXML private TableColumn<Cliente, String> tbcNombre;
    @FXML private TableColumn<Cliente, String> tbcApellido;

    @FXML
    void onAgregarCliente() {
        agregarCliente();
    }

    @FXML
    void onActualizarCliente() {
        actualizarCliente();
    }

    @FXML
    void onLimpiar() {
        limpiarSeleccion();
    }

    @FXML
    void onEliminar() {
        eliminarCliente();
    }

    public void setApp(App app) {
        this.app = app;
        this.clienteController = new ClienteController(App.empresa);
        initView();
    }

    private void initView() {
        tbcCedula.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCedula()));
        tbcNombre.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNombre()));
        tbcApellido.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getApellido()));

        listClientes.clear();
        listClientes.addAll(clienteController.obtenerListaClientes());
        tblListCliente.setItems(listClientes);

        tblListCliente.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            selectedCliente = newSelection;
            mostrarInformacionCliente(selectedCliente);
        });
    }

    private void mostrarInformacionCliente(Cliente cliente) {
        if (cliente != null) {
            txtCedula.setText(cliente.getCedula());
            txtNombre.setText(cliente.getNombre());
            txtApellido.setText(cliente.getApellido());
        }
    }

    private Cliente buildCliente() {
        return new Cliente(txtCedula.getText().trim(), txtNombre.getText().trim(), txtApellido.getText().trim());
    }

    private void agregarCliente() {
        if (txtCedula.getText().isBlank() || txtNombre.getText().isBlank() || txtApellido.getText().isBlank()) {
            mostrarMensaje("Atención", "Todos los campos son obligatorios", Alert.AlertType.WARNING);
            return;
        }

        Cliente cliente = buildCliente();
        if (clienteController.crearCliente(cliente)) {
            listClientes.add(cliente);
            limpiarCamposCliente();
            mostrarMensaje("Éxito", "Cliente agregado correctamente", Alert.AlertType.INFORMATION);
        } else {
            mostrarMensaje("Error", "Ya existe un cliente registrado con esa cédula", Alert.AlertType.ERROR);
        }
    }

    private void eliminarCliente() {
        if (selectedCliente == null) {
            mostrarMensaje("Atención", "Seleccione un cliente de la tabla", Alert.AlertType.WARNING);
            return;
        }

        if (clienteController.eliminarCliente(selectedCliente.getCedula())) {
            listClientes.remove(selectedCliente);
            limpiarCamposCliente();
            limpiarSeleccion();
            mostrarMensaje("Éxito", "Cliente eliminado correctamente", Alert.AlertType.INFORMATION);
        }
    }

    private void actualizarCliente() {
        if (selectedCliente == null) {
            mostrarMensaje("Atención", "Seleccione un cliente de la tabla", Alert.AlertType.WARNING);
            return;
        }

        Cliente nuevo = buildCliente();
        if (clienteController.actualizarCliente(selectedCliente.getCedula(), nuevo)) {
            int index = listClientes.indexOf(selectedCliente);
            if (index >= 0) {
                listClientes.set(index, nuevo);
            }
            tblListCliente.refresh();
            limpiarSeleccion();
            mostrarMensaje("Éxito", "Cliente actualizado correctamente", Alert.AlertType.INFORMATION);
        }
    }

    private void limpiarSeleccion() {
        tblListCliente.getSelectionModel().clearSelection();
        limpiarCamposCliente();
    }

    private void limpiarCamposCliente() {
        txtCedula.clear();
        txtNombre.clear();
        txtApellido.clear();
    }

    private void mostrarMensaje(String titulo, String mensaje, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
