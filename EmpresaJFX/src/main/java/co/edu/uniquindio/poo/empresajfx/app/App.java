package co.edu.uniquindio.poo.empresajfx.app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import java.io.IOException;

import co.edu.uniquindio.poo.empresajfx.model.Cliente;
import co.edu.uniquindio.poo.empresajfx.model.Empleado;
import co.edu.uniquindio.poo.empresajfx.model.Empresa;
import co.edu.uniquindio.poo.empresajfx.model.Producto;
import co.edu.uniquindio.poo.empresajfx.viewController.ClienteViewController;
import co.edu.uniquindio.poo.empresajfx.viewController.InicioSesionViewController;
import co.edu.uniquindio.poo.empresajfx.viewController.PrimaryController;

public class App extends Application {

    private Stage primaryStage;
    public static Empresa empresa = new Empresa("UQ");

    @Override
    public void start(Stage primaryStage) throws IOException {
        this.primaryStage = primaryStage;
        inicializarData();
        openInicioSesion();
    }

    public void openInicioSesion() {
        try {
            FXMLLoader loader = new FXMLLoader(App.class.getResource("/co/edu/uniquindio/poo/empresajfx/inicioSesion.fxml"));
            AnchorPane rootLayout = loader.load();

            InicioSesionViewController controller = loader.getController();
            controller.setApp(this);

            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Inicio de Sesión - Empresa UQ");
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void openViewPrincipal() {
        try {
            FXMLLoader loader = new FXMLLoader(App.class.getResource("/co/edu/uniquindio/poo/empresajfx/primary.fxml"));
            javafx.scene.layout.VBox rootLayout = loader.load();

            PrimaryController primaryController = loader.getController();
            primaryController.setApp(this);

            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Menú Principal - Empresa UQ");
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void openCrudCliente() {
        try {
            FXMLLoader loader = new FXMLLoader(App.class.getResource("/co/edu/uniquindio/poo/empresajfx/crudCliente.fxml"));
            AnchorPane rootLayout = loader.load();

            ClienteViewController clienteViewController = loader.getController();
            clienteViewController.setApp(this);

            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Gestión de Clientes");
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void inicializarData() {
        Empleado emp1 = new Empleado("Jely", "Garcia", "98765", "123456", "jelgar");
        Empleado emp2 = new Empleado("Admin", "General", "1000", "admin", "admin");
        empresa.registraEmpleado(emp1);
        empresa.registraEmpleado(emp2);

        Cliente cliente1 = new Cliente("54321", "Jayda", "Berrío");
        empresa.agregarCliente(cliente1);

        Producto prod1 = new Producto("P001", "Laptop HP", 2500000.0);
        empresa.agregarProducto(prod1);
    }

    public static void main(String[] args) {
        launch();
    }
}
