module co.edu.uniquindio.poo.empresajfx {
    requires javafx.controls;
    requires javafx.fxml;

    opens co.edu.uniquindio.poo.empresajfx.app to javafx.fxml;
    exports co.edu.uniquindio.poo.empresajfx.app;

    opens co.edu.uniquindio.poo.empresajfx.model to javafx.fxml, javafx.base;
    exports co.edu.uniquindio.poo.empresajfx.model;

    opens co.edu.uniquindio.poo.empresajfx.controller to javafx.fxml;
    exports co.edu.uniquindio.poo.empresajfx.controller;

    opens co.edu.uniquindio.poo.empresajfx.viewController to javafx.fxml;
    exports co.edu.uniquindio.poo.empresajfx.viewController;
}
