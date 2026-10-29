package co.edu.uniquindio.poo.empleadosjfx.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Empresa {
    private String nombre;
    private List<Empleado> empleados;
    private Map<String, String> usuarios;

    public Empresa(String nombre) {
        this.nombre = nombre;
        this.empleados = new ArrayList<>();
        this.usuarios = new HashMap<>();
    }

    public void registrarUsuario(String user, String pass) {
        usuarios.put(user, pass);
    }

    public boolean verificarUsuario(String user, String pass) {
        return usuarios.containsKey(user) && usuarios.get(user).equals(pass);
    }

    public String getNombre() { return nombre; }

    public List<Empleado> getEmpleados() { return empleados; }

    public boolean agregarEmpleado(Empleado empleado) {
        if (buscarEmpleado(empleado.getId()) == null) {
            empleados.add(empleado);
            return true;
        }
        return false;
    }

    public boolean eliminarEmpleado(String id) {
        Empleado emp = buscarEmpleado(id);
        if (emp != null) {
            empleados.remove(emp);
            return true;
        }
        return false;
    }

    public boolean actualizarEmpleado(String id, Empleado nuevo) {
        for (int i = 0; i < empleados.size(); i++) {
            if (empleados.get(i).getId().equals(id)) {
                empleados.set(i, nuevo);
                return true;
            }
        }
        return false;
    }

    public Empleado buscarEmpleado(String id) {
        for (Empleado e : empleados) {
            if (e.getId().equals(id)) return e;
        }
        return null;
    }

    public double calcularCostoTotalNomina() {
        double total = 0;
        for (Empleado e : empleados) {
            total += e.calcularSalarioTotal();
        }
        return total;
    }
}
