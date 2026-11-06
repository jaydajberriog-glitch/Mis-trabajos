package co.edu.uniquindio.poo.empleadosjfx.viewController;

import co.edu.uniquindio.poo.empleadosjfx.app.App;
import co.edu.uniquindio.poo.empleadosjfx.controller.EmpleadoController;
import co.edu.uniquindio.poo.empleadosjfx.model.*;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class EmpleadoViewController {

    private EmpleadoController empleadoController;
    private ObservableList<Empleado> listEmpleados = FXCollections.observableArrayList();
    private Empleado selectedEmpleado;
    private App app;

    @FXML private TextField txtId;
    @FXML private TextField txtNombre;
    @FXML private ComboBox<String> cbTipoEmpleado;
    @FXML private ComboBox<CategoriaEmpleado> cbCategoria;
    
    @FXML private Label lblCampo1;
    @FXML private TextField txtCampo1;
    @FXML private Label lblCampo2;
    @FXML private TextField txtCampo2;

    @FXML private Label lblTotalNomina;

    @FXML private TableView<Empleado> tblListEmpleado;
    @FXML private TableColumn<Empleado, String> tbcId;
    @FXML private TableColumn<Empleado, String> tbcNombre;
    @FXML private TableColumn<Empleado, String> tbcTipo;
    @FXML private TableColumn<Empleado, String> tbcCategoria;
    @FXML private TableColumn<Empleado, Number> tbcSalarioTotal;

    public void setApp(App app) {
        this.app = app;
        this.empleadoController = new EmpleadoController(App.empresa);
        initView();
    }

    private void initView() {
        cbTipoEmpleado.setItems(FXCollections.observableArrayList("Planta", "Temporal", "Ventas"));
        cbTipoEmpleado.setValue("Planta");

        cbCategoria.setItems(FXCollections.observableArrayList(CategoriaEmpleado.values()));
        cbCategoria.setValue(CategoriaEmpleado.JUNIOR);

        cbTipoEmpleado.valueProperty().addListener((obs, oldVal, newVal) -> actualizarFormularioTipo(newVal));
        actualizarFormularioTipo("Planta");

        tbcId.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getId()));
        tbcNombre.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getNombre()));
        tbcTipo.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getTipoEmpleado()));
        tbcCategoria.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getCategoria().toString()));
        tbcSalarioTotal.setCellValueFactory(cell -> new SimpleDoubleProperty(cell.getValue().calcularSalarioTotal()));

        cargarTabla();

        tblListEmpleado.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            selectedEmpleado = newSel;
            mostrarInformacion(selectedEmpleado);
        });
    }

    private void actualizarFormularioTipo(String tipo) {
        if ("Planta".equals(tipo)) {
            lblCampo1.setText("Salario Base:");
            lblCampo2.setText("Bonificación Fija:");
            lblCampo2.setVisible(true);
            txtCampo2.setVisible(true);
        } else if ("Temporal".equals(tipo)) {
            lblCampo1.setText("Horas Trabajadas:");
            lblCampo2.setText("Valor Hora:");
            lblCampo2.setVisible(true);
            txtCampo2.setVisible(true);
        } else if ("Ventas".equals(tipo)) {
            lblCampo1.setText("Salario Base:");
            lblCampo2.setText("Total Ventas:");
            lblCampo2.setVisible(true);
            txtCampo2.setVisible(true);
        }
    }

    private void cargarTabla() {
        listEmpleados.clear();
        listEmpleados.addAll(empleadoController.obtenerListaEmpleados());
        tblListEmpleado.setItems(listEmpleados);
        actualizarTotalNomina();
    }

    private void actualizarTotalNomina() {
        double total = empleadoController.obtenerCostoTotalNomina();
        lblTotalNomina.setText(String.format("Costo Total Nómina: $%.2f", total));
    }

    private void mostrarInformacion(Empleado emp) {
        if (emp != null) {
            txtId.setText(emp.getId());
            txtNombre.setText(emp.getNombre());
            cbCategoria.setValue(emp.getCategoria());
            cbTipoEmpleado.setValue(emp.getTipoEmpleado());

            if (emp instanceof EmpleadoPlanta ep) {
                txtCampo1.setText(String.valueOf(ep.getSalarioBase()));
                txtCampo2.setText(String.valueOf(ep.getBonificacionFija()));
            } else if (emp instanceof EmpleadoTemporal et) {
                txtCampo1.setText(String.valueOf(et.getHorasTrabajadas()));
                txtCampo2.setText(String.valueOf(et.getValorHora()));
            } else if (emp instanceof EmpleadoVentas ev) {
                txtCampo1.setText(String.valueOf(ev.getSalarioBase()));
                txtCampo2.setText(String.valueOf(ev.getTotalVentas()));
            }
        }
    }

    @FXML
    void onAgregarEmpleado() {
        try {
            Empleado nuevo = buildEmpleado();
            if (nuevo == null) return;

            if (empleadoController.crearEmpleado(nuevo)) {
                cargarTabla();
                limpiar();
                mostrarMensaje("Éxito", "Empleado registrado correctamente", Alert.AlertType.INFORMATION);
            } else {
                mostrarMensaje("Error", "Ya existe un empleado con este ID", Alert.AlertType.ERROR);
            }
        } catch (NumberFormatException e) {
            mostrarMensaje("Error", "Ingrese valores numéricos válidos", Alert.AlertType.ERROR);
        }
    }

    @FXML
    void onActualizarEmpleado() {
        if (selectedEmpleado == null) {
            mostrarMensaje("Atención", "Seleccione un empleado de la tabla", Alert.AlertType.WARNING);
            return;
        }

        try {
            Empleado nuevo = buildEmpleado();
            if (nuevo == null) return;

            if (empleadoController.actualizarEmpleado(selectedEmpleado.getId(), nuevo)) {
                cargarTabla();
                limpiar();
                mostrarMensaje("Éxito", "Empleado actualizado correctamente", Alert.AlertType.INFORMATION);
            }
        } catch (NumberFormatException e) {
            mostrarMensaje("Error", "Ingrese valores numéricos válidos", Alert.AlertType.ERROR);
        }
    }

    @FXML
    void onEliminar() {
        if (selectedEmpleado == null) {
            mostrarMensaje("Atención", "Seleccione un empleado de la tabla", Alert.AlertType.WARNING);
            return;
        }

        if (empleadoController.eliminarEmpleado(selectedEmpleado.getId())) {
            cargarTabla();
            limpiar();
            mostrarMensaje("Éxito", "Empleado eliminado correctamente", Alert.AlertType.INFORMATION);
        }
    }

    @FXML
    void onLimpiar() {
        limpiar();
    }

    @FXML
    void onVolver() {
        app.openViewPrincipal();
    }

    private Empleado buildEmpleado() {
        String id = txtId.getText().trim();
        String nombre = txtNombre.getText().trim();
        String tipo = cbTipoEmpleado.getValue();
        CategoriaEmpleado cat = cbCategoria.getValue();

        if (id.isBlank() || nombre.isBlank() || txtCampo1.getText().isBlank()) {
            mostrarMensaje("Atención", "Complete todos los campos obligatorios", Alert.AlertType.WARNING);
            return null;
        }

        double v1 = Double.parseDouble(txtCampo1.getText().trim());
        double v2 = txtCampo2.getText().isBlank() ? 0 : Double.parseDouble(txtCampo2.getText().trim());

        return switch (tipo) {
            case "Planta" -> new EmpleadoPlanta(id, nombre, cat, v1, v2);
            case "Temporal" -> new EmpleadoTemporal(id, nombre, cat, (int) v1, v2);
            case "Ventas" -> new EmpleadoVentas(id, nombre, cat, v1, v2, 0.05); // 5% de comisión estándar
            default -> null;
        };
    }

    private void limpiar() {
        tblListEmpleado.getSelectionModel().clearSelection();
        txtId.clear();
        txtNombre.clear();
        txtCampo1.clear();
        txtCampo2.clear();
        selectedEmpleado = null;
    }

    private void mostrarMensaje(String titulo, String mensaje, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
