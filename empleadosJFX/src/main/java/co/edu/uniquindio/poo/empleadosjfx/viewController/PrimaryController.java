package co.edu.uniquindio.poo.empleadosjfx.viewController;

import co.edu.uniquindio.poo.empleadosjfx.app.App;
import javafx.fxml.FXML;

public class PrimaryController {
    private App app;

    public void setApp(App app) {
        this.app = app;
    }

    @FXML
    void onOpenCrudEmpleado() {
        app.openCrudEmpleado();
    }

    @FXML
    void onCerrarSesion() {
        app.openInicioSesion();
    }
}
