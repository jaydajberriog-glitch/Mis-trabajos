package co.edu.uniquindio.poo.empleadosjfx.app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.io.IOException;

import co.edu.uniquindio.poo.empleadosjfx.model.*;
import co.edu.uniquindio.poo.empleadosjfx.viewController.*;

public class App extends Application {

    private Stage primaryStage;
    public static Empresa empresa = new Empresa("Empresa de Gestión de Nómina");

    @Override
    public void start(Stage primaryStage) throws IOException {
        this.primaryStage = primaryStage;
        inicializarData();
        openInicioSesion();
    }

    public void openInicioSesion() {
        try {
            FXMLLoader loader = new FXMLLoader(App.class.getResource("/co/edu/uniquindio/poo/empleadosjfx/inicioSesion.fxml"));
            AnchorPane rootLayout = loader.load();

            InicioSesionViewController controller = loader.getController();
            controller.setApp(this);

            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Inicio de Sesión - Sistema de Empleados");
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void openViewPrincipal() {
        try {
            FXMLLoader loader = new FXMLLoader(App.class.getResource("/co/edu/uniquindio/poo/empleadosjfx/primary.fxml"));
            VBox rootLayout = loader.load();

            PrimaryController primaryController = loader.getController();
            primaryController.setApp(this);

            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Menú Principal - Gestión de Empleados");
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void openCrudEmpleado() {
        try {
            FXMLLoader loader = new FXMLLoader(App.class.getResource("/co/edu/uniquindio/poo/empleadosjfx/crudEmpleado.fxml"));
            AnchorPane rootLayout = loader.load();

            EmpleadoViewController controller = loader.getController();
            controller.setApp(this);

            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Gestión de Nómina y Empleados");
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void inicializarData() {
        // Usuarios para el Login
        empresa.registrarUsuario("admin", "admin");
        empresa.registrarUsuario("jelgar", "123456");

        // Datos iniciales de prueba
        empresa.agregarEmpleado(new EmpleadoPlanta("E001", "Ana Gómez", CategoriaEmpleado.SENIOR, 2500000, 300000));
        empresa.agregarEmpleado(new EmpleadoTemporal("E002", "Carlos Ruiz", CategoriaEmpleado.JUNIOR, 160, 15000));
        empresa.agregarEmpleado(new EmpleadoVentas("E003", "Beatriz López", CategoriaEmpleado.MID, 1300000, 15000000, 0.04));
    }

    public static void main(String[] args) {
        launch();
    }
}
