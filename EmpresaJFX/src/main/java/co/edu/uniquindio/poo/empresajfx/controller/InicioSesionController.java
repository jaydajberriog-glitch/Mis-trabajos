package co.edu.uniquindio.poo.empresajfx.controller;

import co.edu.uniquindio.poo.empresajfx.model.Empleado;
import co.edu.uniquindio.poo.empresajfx.model.Empresa;

public class InicioSesionController {
    private Empresa empresa;

    public InicioSesionController(Empresa empresa) {
        this.empresa = empresa;
    }

    public boolean verificarUsuario(Empleado empleado) {
        return empresa.verificarUsuario(empleado);
    }
}
