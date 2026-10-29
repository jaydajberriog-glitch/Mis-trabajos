package co.edu.uniquindio.poo.empleadosjfx.controller;

import co.edu.uniquindio.poo.empleadosjfx.model.Empresa;

public class InicioSesionController {
    private Empresa empresa;

    public InicioSesionController(Empresa empresa) {
        this.empresa = empresa;
    }

    public boolean verificarUsuario(String user, String pass) {
        return empresa.verificarUsuario(user, pass);
    }
}
