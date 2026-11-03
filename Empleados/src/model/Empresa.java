package model;

import java.util.ArrayList;
import java.util.List;

public class Empresa {
    private final List<Empleado> empleados = new ArrayList<>();

    public void agregarEmpleado(Empleado empleado) {
        if (empleado == null) throw new IllegalArgumentException("El empleado no puede ser nulo.");
        if (buscarPorDocumento(empleado.getDocumento()) != null)
            throw new IllegalArgumentException("Ya existe un empleado con ese documento.");
        empleados.add(empleado);
    }

    public List<Empleado> listarEmpleados() { return List.copyOf(empleados); }

    public Empleado buscarPorDocumento(String documento) {
        for (Empleado empleado : empleados)
            if (empleado.getDocumento().equalsIgnoreCase(documento.trim())) return empleado;
        return null;
    }

    public Empleado empleadoConMayorSalarioNeto() {
        Empleado mayor = null;
        for (Empleado empleado : empleados)
            if (mayor == null || empleado.calcularSalarioNeto() > mayor.calcularSalarioNeto()) mayor = empleado;
        return mayor;
    }

    public float calcularNominaNeta() {
        float total = 0;
        for (Empleado empleado : empleados) total += empleado.calcularSalarioNeto();
        return total;
    }

    public void mostrarResumenes() {
        if (empleados.isEmpty()) { System.out.println("No hay empleados registrados."); return; }
        for (Empleado empleado : empleados) {
            ResumenPago r = empleado.crearResumenPago();
            System.out.printf("%s | %s | %s | Bruto: $%.2f | Descuentos: $%.2f | Neto: $%.2f%n",
                    r.documento(), r.nombre(), r.tipoEmpleado(), r.salarioBruto(), r.descuentos(), r.salarioNeto());
        }
    }
}
