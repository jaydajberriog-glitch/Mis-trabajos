package co.edu.uniquindio.poo.empresajfx.viewController;

import co.edu.uniquindio.poo.empresajfx.app.App;
import co.edu.uniquindio.poo.empresajfx.controller.InicioSesionController;
import co.edu.uniquindio.poo.empresajfx.model.Empleado;
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
            mostrarMensaje("Atención", "Por favor ingrese todos los campos.", Alert.AlertType.WARNING);
            return;
        }

        Empleado empleado = new Empleado("", "", "", password, usuario);
        if (inicioSesionController.verificarUsuario(empleado)) {
            mostrarMensaje("Éxito", "Usuario autorizado. ¡Bienvenido!", Alert.AlertType.INFORMATION);
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
