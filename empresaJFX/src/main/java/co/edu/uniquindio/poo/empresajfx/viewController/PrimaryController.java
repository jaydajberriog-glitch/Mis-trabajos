package co.edu.uniquindio.poo.empresajfx.viewController;

import co.edu.uniquindio.poo.empresajfx.app.App;
import javafx.fxml.FXML;

public class PrimaryController {
    private App app;

    public void setApp(App app) {
        this.app = app;
    }

    @FXML
    void onOpenCrudCliente() {
        app.openCrudCliente();
    }
}
