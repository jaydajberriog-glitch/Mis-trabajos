package controllers;

import model.Empleado;
import model.Panaderia;
import model.Pedido;
import model.TipoPan;

import java.util.List;

/**
 * Recibe las solicitudes de la vista y las delega al modelo.
 */
public class PanaderiaController {

    private final Panaderia panaderia;

    public PanaderiaController(Panaderia panaderia) {
        if (panaderia == null) {
            throw new IllegalArgumentException("La panadería es obligatoria.");
        }
        this.panaderia = panaderia;
    }

    public Pedido registrarPedido(String codigo, String cliente, TipoPan tipoPan, int cantidad) {
        return panaderia.registrarPedido(codigo, cliente, tipoPan, cantidad);
    }

    public Pedido asignarPedido(String codigo) {
        return panaderia.asignarPedido(codigo);
    }

    public void cancelarPedido(String codigo) {
        panaderia.cancelarPedido(codigo);
    }

    public List<Pedido> obtenerPedidos() {
        return panaderia.getPedidos();
    }

    public List<Empleado> obtenerEmpleados() {
        return panaderia.getEmpleados();
    }

    public String obtenerNombrePanadero(String codigo) {
        return panaderia.buscarPanaderoPorCodigo(codigo).getNombre();
    }
}
