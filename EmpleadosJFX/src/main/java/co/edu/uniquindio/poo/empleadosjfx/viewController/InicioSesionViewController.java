package co.edu.uniquindio.poo.empleadosjfx.viewController;

import co.edu.uniquindio.poo.empleadosjfx.app.App;
import co.edu.uniquindio.poo.empleadosjfx.controller.InicioSesionController;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class InicioSesionViewController {

    @FXML private TextField txtUsuario;
    @FXML private PasswordField txtPassword;

    private App app;
    private InicioSesionController inicioSesionController;

    public void setApp(App app) {
        this.app = app;
        this.inicioSesionController = new InicioSesionController(App.empresa);
    }

    @FXML
    void onLogin() {
        String usuario = txtUsuario.getText();
        String password = txtPassword.getText();

        if (usuario == null || usuario.isBlank() || password == null || password.isBlank()) {
            mostrarMensaje("Atención", "Ingrese usuario y contraseña", Alert.AlertType.WARNING);
            return;
        }

        if (inicioSesionController.verificarUsuario(usuario, password)) {
            mostrarMensaje("Éxito", "¡Bienvenido al Sistema de Nómina!", Alert.AlertType.INFORMATION);
            app.openViewPrincipal();
        } else {
            mostrarMensaje("Acceso Denegado", "Usuario o contraseña incorrectos", Alert.AlertType.ERROR);
            txtPassword.clear();
        }
    }

    private void mostrarMensaje(String titulo, String mensaje, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
