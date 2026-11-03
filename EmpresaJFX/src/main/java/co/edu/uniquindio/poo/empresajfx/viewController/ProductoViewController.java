package co.edu.uniquindio.poo.empresajfx.viewController;

import co.edu.uniquindio.poo.empresajfx.app.App;
import co.edu.uniquindio.poo.empresajfx.controller.ProductoController;
import co.edu.uniquindio.poo.empresajfx.model.Producto;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class ProductoViewController {

    private ProductoController productoController;
    private ObservableList<Producto> listProductos = FXCollections.observableArrayList();
    private Producto selectedProducto;
    private App app;

    @FXML private TextField txtCodigo;
    @FXML private TextField txtNombre;
    @FXML private TextField txtPrecio;
    @FXML private TableView<Producto> tblListProducto;
    @FXML private TableColumn<Producto, String> tbcCodigo;
    @FXML private TableColumn<Producto, String> tbcNombre;
    @FXML private TableColumn<Producto, String> tbcPrecio;

    @FXML void onAgregarProducto() { agregarProducto(); }
    @FXML void onActualizarProducto() { actualizarProducto(); }
    @FXML void onLimpiar() { limpiarSeleccion(); }
    @FXML void onEliminar() { eliminarProducto(); }
    @FXML void onVolver() { app.openViewPrincipal(); }

    public void setApp(App app) {
        this.app = app;
        this.productoController = new ProductoController(App.empresa);
        initView();
    }

    private void initView() {
        tbcCodigo.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getCodigo()));
        tbcNombre.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getNombre()));
        tbcPrecio.setCellValueFactory(c -> new SimpleStringProperty(String.valueOf(c.getValue().getPrecio())));

        listProductos.clear();
        listProductos.addAll(productoController.obtenerListaProductos());
        tblListProducto.setItems(listProductos);

        tblListProducto.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            selectedProducto = newSel;
            mostrarInformacionProducto(selectedProducto);
        });
    }

    private void mostrarInformacionProducto(Producto producto) {
        if (producto != null) {
            txtCodigo.setText(producto.getCodigo());
            txtNombre.setText(producto.getNombre());
            txtPrecio.setText(String.valueOf(producto.getPrecio()));
        }
    }

    private Producto buildProducto() {
        if (txtCodigo.getText().isBlank() || txtNombre.getText().isBlank() || txtPrecio.getText().isBlank()) {
            mostrarAlerta("Todos los campos son obligatorios.");
            return null;
        }
        try {
            double precio = Double.parseDouble(txtPrecio.getText().trim());
            if (precio < 0) {
                mostrarAlerta("El precio no puede ser negativo.");
                return null;
            }
            return new Producto(txtCodigo.getText().trim(), txtNombre.getText().trim(), precio);
        } catch (NumberFormatException e) {
            mostrarAlerta("El precio debe ser un número válido.");
            return null;
        }
    }

    private void agregarProducto() {
        Producto producto = buildProducto();
        if (producto == null) return;
        if (productoController.crearProducto(producto)) {
            listProductos.add(producto);
            limpiarCamposProducto();
        } else {
            mostrarAlerta("Ya existe un producto con ese código.");
        }
    }

    private void eliminarProducto() {
        if (selectedProducto == null) {
            mostrarAlerta("Selecciona un producto de la tabla.");
            return;
        }
        if (productoController.eliminarProducto(selectedProducto.getCodigo())) {
            listProductos.remove(selectedProducto);
            limpiarSeleccion();
        }
    }

    private void actualizarProducto() {
        if (selectedProducto == null) {
            mostrarAlerta("Selecciona un producto de la tabla.");
            return;
        }
        Producto nuevo = buildProducto();
        if (nuevo == null) return;
        if (productoController.actualizarProducto(selectedProducto.getCodigo(), nuevo)) {
            tblListProducto.refresh();
            limpiarSeleccion();
        }
    }

    private void limpiarSeleccion() {
        tblListProducto.getSelectionModel().clearSelection();
        limpiarCamposProducto();
    }

    private void limpiarCamposProducto() {
        txtCodigo.clear();
        txtNombre.clear();
        txtPrecio.clear();
    }

    private void mostrarAlerta(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Aviso");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
