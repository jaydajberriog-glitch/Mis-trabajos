package view;

import controllers.PanaderiaController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.Cajero;
import model.Panaderia;
import model.PanaderoArtesanal;
import model.PanaderoIndustrial;
import viewcontrollers.PanaderiaViewController;

import java.io.IOException;

/**
 * Punto de entrada de la aplicación JavaFX.
 */
public class Aplicacion extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        Panaderia panaderia = new Panaderia("Panadería El Trigal");
        cargarEmpleadosIniciales(panaderia);

        PanaderiaController controller = new PanaderiaController(panaderia);

        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/view/panaderia-view.fxml")
        );

        Scene scene = new Scene(loader.load());
        PanaderiaViewController viewController = loader.getController();
        viewController.setPanaderiaController(controller);

        stage.setTitle("Panadería El Trigal");
        stage.setMinWidth(900);
        stage.setMinHeight(620);
        stage.setScene(scene);
        stage.show();
    }

    private void cargarEmpleadosIniciales(Panaderia panaderia) {
        panaderia.agregarEmpleado(new PanaderoArtesanal("PA-01", "Carlos Pérez", 240));
        panaderia.agregarEmpleado(new PanaderoIndustrial("PI-01", "María Gómez", 300));
        panaderia.agregarEmpleado(new PanaderoArtesanal("PA-02", "Luis Ramírez", 180));
        panaderia.agregarEmpleado(new Cajero("CA-01", "Ana Torres"));
    }

    public static void main(String[] args) {
        launch(args);
    }
}
