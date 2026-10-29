package co.edu.uniquindio.poo.empleadosjfx.controller;

import co.edu.uniquindio.poo.empleadosjfx.model.Empleado;
import co.edu.uniquindio.poo.empleadosjfx.model.Empresa;
import java.util.List;

public class EmpleadoController {
    private Empresa empresa;

    public EmpleadoController(Empresa empresa) {
        this.empresa = empresa;
    }

    public boolean crearEmpleado(Empleado empleado) {
        return empresa.agregarEmpleado(empleado);
    }

    public List<Empleado> obtenerListaEmpleados() {
        return empresa.getEmpleados();
    }

    public boolean eliminarEmpleado(String id) {
        return empresa.eliminarEmpleado(id);
    }

    public boolean actualizarEmpleado(String id, Empleado empleado) {
        return empresa.actualizarEmpleado(id, empleado);
    }

    public double obtenerCostoTotalNomina() {
        return empresa.calcularCostoTotalNomina();
    }
}
