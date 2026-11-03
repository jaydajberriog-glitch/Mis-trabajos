package co.edu.uniquindio.poo.empresajfx.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class Empresa {
    private String nombre;
    private Collection<Cliente> clientes;
    private List<Empleado> listaEmpleados;
    private Collection<Producto> productos;

    public Empresa(String nombre) {
        this.nombre = nombre;
        this.clientes = new LinkedList<>();
        this.listaEmpleados = new ArrayList<>();
        this.productos = new LinkedList<>();
    }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public Collection<Cliente> getClientes() { return clientes; }

    public boolean agregarCliente(Cliente cliente) {
        if (!verificarCliente(cliente.getCedula())) {
            clientes.add(cliente);
            return true;
        }
        return false;
    }

    public boolean eliminarCliente(String cedula) {
        for (Cliente cliente : clientes) {
            if (cliente.getCedula().equals(cedula)) {
                clientes.remove(cliente);
                return true;
            }
        }
        return false;
    }

    public boolean actualizarCliente(String cedula, Cliente actualizado) {
        for (Cliente cliente : clientes) {
            if (cliente.getCedula().equals(cedula)) {
                cliente.setCedula(actualizado.getCedula());
                cliente.setNombre(actualizado.getNombre());
                cliente.setApellido(actualizado.getApellido());
                return true;
            }
        }
        return false;
    }

    public boolean verificarCliente(String cedula) {
        for (Cliente cliente : clientes) {
            if (cliente.getCedula().equals(cedula)) {
                return true;
            }
        }
        return false;
    }

    public void registraEmpleado(Empleado empleado) {
        listaEmpleados.add(empleado);
    }

    public boolean verificarUsuario(Empleado empleadoVerificar) {
        for (Empleado empleado : listaEmpleados) {
            if (empleado.getUser().equals(empleadoVerificar.getUser()) &&
                empleado.getPassword().equals(empleadoVerificar.getPassword())) {
                return true;
            }
        }
        return false;
    }

    public Collection<Producto> getProductos() { return productos; }

    public boolean agregarProducto(Producto producto) {
        for (Producto p : productos) {
            if (p.getCodigo().equals(producto.getCodigo())) return false;
        }
        productos.add(producto);
        return true;
    }

    public boolean eliminarProducto(String codigo) {
        for (Producto p : productos) {
            if (p.getCodigo().equals(codigo)) {
                productos.remove(p);
                return true;
            }
        }
        return false;
    }

    public boolean actualizarProducto(String codigo, Producto nuevo) {
        for (Producto p : productos) {
            if (p.getCodigo().equals(codigo)) {
                p.setNombre(nuevo.getNombre());
                p.setPrecio(nuevo.getPrecio());
                return true;
            }
        }
        return false;
    }
}
