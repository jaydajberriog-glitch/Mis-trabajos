package co.edu.uniquindio.poo.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

/**
 * Representa una tienda y gestiona sus clientes, facturas y productos.
 */
public class Tienda {

    private final String nombre;
    /** Número de Identificación Tributaria (NIT) de la tienda. */
    private final String nit;
    private String telefono;

    private final ArrayList<Cliente> listaClientes = new ArrayList<>();
    private final List<Factura> listaFacturas = new LinkedList<>();
    private Map<String, Producto> hashMaplistaProductos = new HashMap<>();


    public Tienda(String nombre, String nit, String telefono) {
        this.nombre = nombre;
        this.nit = nit;
        this.telefono = telefono;
    }

    // setters y getters

    public String getNombre() {
        return nombre;
    }

    public String getNit() {
        return nit;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    //-CRUD CLIENTE

    /**
     * Este metodo permite registrar un cliente
     * @param cliente
     * @return
     */
    public String registrarCliente(Cliente cliente) {
        Cliente clienteEncontrado = buscarCliente1(cliente.getDocumentoIdentidad());
        if (clienteEncontrado == null) {
            listaClientes.add(cliente);
            return "El cliente fue registrado exitosamente";
        } else {
            return "No se puede registrar, ya existe un cliente con esa informacion registrado anteriormente.";
        }
    }

    public String registrarCliente2(Cliente cliente) {
        Optional<Cliente> clienteEncontrado = buscarCliente2(cliente.getDocumentoIdentidad());
        if (clienteEncontrado.isEmpty()) {
            listaClientes.add(cliente);
            return "El cliente fue registrado exitosamente";
        } else {
            return "No se puede registrar, ya existe un cliente con esa informacion registrado anteriormente.";
        }
    }


    public Cliente buscarCliente1(String documentoIdentidad) {
        for (Cliente cliente : listaClientes) { // foreach
            if (documentoIdentidad.equals(cliente.getDocumentoIdentidad())) {
                return cliente;
            }
        }
        return null;
    }

    /**
     * Esta forma de hacer una busqueda se llama imperativo- significa que usted ve el còmo? hace las cosas
     * @param documentoIdentidad
     * @return
     */
    public Optional<Cliente> buscarCliente2(String documentoIdentidad) {
        for (Cliente cliente : listaClientes) {
            if (documentoIdentidad.equals(cliente.getDocumentoIdentidad())) {
                return Optional.of(cliente); // guardo en la caja
            }
        }
        return Optional.empty();
    }

    // imperativa y declarativa(estudiarlo)
    //Solución: Imperativa es mostrar los pasos paso a paso ¿Cómo se hace?. Declarativa se le dice lo que se debe hacer ¿Que se hace?


    public Optional<Cliente> buscarCliente3(String documentoIdentidad) {
        return listaClientes.stream().filter(cliente ->
                documentoIdentidad.equals(cliente.getDocumentoIdentidad())).findFirst();
    }


    // Cambiar if(clienteEncontrado == null){ por un Optional
    // hacer el metodo buscar cliente usando un optional
    public String registrarClienteConOptional(Cliente cliente) {
        if (cliente == null || cliente.getDocumentoIdentidad() == null) {
            return "Datos de cliente no válidos.";
        }
        Optional<Cliente> clienteEncontrado = buscarCliente2(cliente.getDocumentoIdentidad());
        if (clienteEncontrado.isPresent()) {
            return "No se puede registrar, ya existe un cliente con ese documento.";
        }
        listaClientes.add(cliente);
        return "El cliente fue registrado exitosamente";
    }

    public String actualizarCliente(String documentoIdentidad, String nombreCompleto,
                                    String telefono, String correo, String ciudadResidencia) {
        Optional<Cliente> clienteEncontrado = buscarCliente2(documentoIdentidad);
        if (clienteEncontrado.isEmpty()) {
            return "No existe un cliente con ese documento.";
        }
        Cliente cliente = clienteEncontrado.get();
        cliente.setNombreCompleto(nombreCompleto);
        cliente.setTelefono(telefono);
        cliente.setCorreo(correo);
        cliente.setCiudadResidencia(ciudadResidencia);
        return "El cliente fue actualizado exitosamente.";
    }

    public String eliminarCliente(String documentoIdentidad) {
        Optional<Cliente> clienteEncontrado = buscarCliente2(documentoIdentidad);
        if (clienteEncontrado.isEmpty()) {
            return "No existe un cliente con ese documento.";
        }
        Cliente cliente = clienteEncontrado.get();
        if (!cliente.getListaFacturas().isEmpty()) {
            return "No se puede eliminar el cliente porque tiene facturas asociadas.";
        }
        listaClientes.remove(cliente);
        return "El cliente fue eliminado exitosamente.";
    }


    //CRUD PRODUCTO

    public String registrarProducto(Producto producto) {
        if (producto == null || producto.getCodigo() == null) {
            return "No se puede registrar un producto nulo o sin código.";
        }
        if (hashMaplistaProductos.containsKey(producto.getCodigo())) {
            return "No se puede registrar, ya existe un producto con ese código.";
        }
        hashMaplistaProductos.put(producto.getCodigo(), producto);
        return "El producto fue registrado exitosamente.";
    }

    public Optional<Producto> buscarProducto(String codigo) {
        return Optional.ofNullable(hashMaplistaProductos.get(codigo));
    }

    public String actualizarProducto(String codigo, String nombre, String descripcion,
                                     int cantidadDisponible, double valor, Categoria categoria) {
        Producto producto = hashMaplistaProductos.get(codigo);
        if (producto == null) {
            return "No existe un producto con ese código.";
        }
        producto.setNombre(nombre);
        producto.setDescripcion(descripcion);
        producto.setCantidadDisponible(cantidadDisponible);
        producto.setValor(valor);
        producto.setCategoria(categoria);
        return "El producto fue actualizado exitosamente.";
    }

    public boolean eliminarProducto(String codigo) {
        Producto producto = hashMaplistaProductos.get(codigo);
        if (producto == null) return false;

        for (Factura factura : listaFacturas) {
            if (factura.listaDetallesFactura() != null) {
                for (DetalleFactura detalle : factura.listaDetallesFactura()) {
                    if (detalle != null && detalle.getProducto() == producto) {
                        return false; // No se elimina si está presente en alguna factura
                    }
                }
            }
        }
        hashMaplistaProductos.remove(codigo);
        return true;
    }


    //CRUD FACTURA

    public Factura buscarFactura(String codigo) {
        for (Factura factura : listaFacturas) {
            if (factura.codigo().equals(codigo)) {
                return factura;
            }
        }
        return null;
    }

    public Optional<Factura> obtenerFactura(String codigo) {
        return listaFacturas.stream().filter(f -> f.codigo().equals(codigo)).findFirst();
    }

    public String registrarFactura(Factura factura) {
        if (factura == null || factura.codigo() == null) {
            return "Factura no válida.";
        }
        if (obtenerFactura(factura.codigo()).isPresent()) {
            return "Ya existe una factura registrada con ese código.";
        }
        listaFacturas.add(factura);
        if (factura.cliente() != null) {
            factura.cliente().agregarFactura(factura);
        }
        return "La factura fue registrada exitosamente.";
    }

    public String eliminarFactura(String codigo) {
        Optional<Factura> facturaEncontrada = obtenerFactura(codigo);
        if (facturaEncontrada.isEmpty()) {
            return "No existe una factura con ese código.";
        }
        Factura factura = facturaEncontrada.get();
        listaFacturas.remove(factura);
        if (factura.cliente() != null) {
            factura.cliente().eliminarFactura(factura);
        }
        return "La factura fue eliminada exitosamente.";
    }

    // cacular el valor de la factura (sumar todos los detalles factura)
    public double calcularValorFactura(Factura factura) {
        if (factura == null || factura.listaDetallesFactura() == null) {
            return 0.0;
        }
        double total = 0.0;
        for (DetalleFactura detalle : factura.listaDetallesFactura()) {
            if (detalle != null) {
                total += detalle.calcularSubTotal();
            }
        }
        return total;
    }


    //EJERCICIOS PUNTOS 1 AL 11

    // 1. Obtener la lista de los productos con una cantidad disponible mayor igual a 10
    public List<Producto> obtenerMayoresDiez() {
        List<Producto> productosAdecuado = new ArrayList<>();

        for (Producto productosBuenos : hashMaplistaProductos.values()) {
            if (productosBuenos.getCantidadDisponible() >= 10) {
                productosAdecuado.add(productosBuenos);
            }
        }
        return productosAdecuado;
    }

    // 2. Obtener la lista de codigos de los productos con una cantidad disponible mayor igual a 10 y menor que 50
    public ArrayList<String> obtenerCodigosProductosAgotados(int limiteInferior, int limiteSuperior) {
        ArrayList<String> resultado = new ArrayList<>();
        for (String codigo : hashMaplistaProductos.keySet()) {
            Producto producto = hashMaplistaProductos.get(codigo);
            if (producto.getCantidadDisponible() >= limiteInferior && producto.getCantidadDisponible() < limiteSuperior) {
                resultado.add(codigo);
            }
        }
        return resultado;
    }

    public ArrayList<String> obtenerCodigosProductosAgotados2(int limiteInferior, int limiteSuperior) {
        ArrayList<String> resultado = new ArrayList<>();
        for (Producto productoAux : hashMaplistaProductos.values()) {
            if (productoAux.getCantidadDisponible() >= limiteInferior && productoAux.getCantidadDisponible() < limiteSuperior) {
                resultado.add(productoAux.getCodigo());
            }
        }
        return resultado;
    }

    // 3. Obtener la lista de clientes que hayan comprado el 07 de octubre de 2026
    // Enfoque desde la colección de facturas
    public ArrayList<Cliente> obtenerClientesCompras() {
        ArrayList<Cliente> listaClientesResultado = new ArrayList<>();
        LocalDate fechaConsulta = LocalDate.of(2026, 10, 7);
        for (Factura factura : listaFacturas) {
            if (factura.fecha().isEqual(fechaConsulta) && !listaClientesResultado.contains(factura.cliente())) {
                listaClientesResultado.add(factura.cliente());
            }
        }
        return listaClientesResultado;
    }

    //Enfoque desde las Facturas
    public ArrayList<Cliente> obtenerClientesCompras2(LocalDate fechaConsulta) {
        ArrayList<Cliente> listaClientesResultado = new ArrayList<>();
        for (Factura factura : listaFacturas) {
            if (factura.fecha().isEqual(fechaConsulta) && !listaClientesResultado.contains(factura.cliente())) {
                listaClientesResultado.add(factura.cliente());
            }
        }
        return listaClientesResultado;
    }

    //Enfoque desde los Clientes
    public ArrayList<Cliente> obtenerClientesCompras3(LocalDate fechaConsulta) {
        ArrayList<Cliente> listaClientesResultado = new ArrayList<>();
        for (Cliente clienteAux : this.listaClientes) {
            if (clienteAux.isCompraEnFecha(fechaConsulta)) {
                listaClientesResultado.add(clienteAux);
            }
        }
        return listaClientesResultado;
    }

    // punto 4: Obtener las facturas que tenga un cliente donde su nombre empiece por R
    public ArrayList<Factura> obtenerFacturasClienteConR() {
        ArrayList<Factura> resultado = new ArrayList<>();

        for (Factura factura : listaFacturas) {
            if (factura.tieneClienteConR()) {
                resultado.add(factura);
            }
        }
        return resultado;
    }

    // punto 5: Obtener las facturas donde se haya comprado un celular de marca Iphone 16 pro max
    public ArrayList<Factura> obtenerFacturasIphone16ProMax() {
        ArrayList<Factura> resultado = new ArrayList<>();
        for (Factura factura : listaFacturas) {
            if (factura.listaDetallesFactura() == null) continue;
            for (DetalleFactura detalle : factura.listaDetallesFactura()) {
                if (detalle != null && detalle.getProducto() != null) {
                    Producto producto = detalle.getProducto();
                    if (producto.getCategoria() == Categoria.CELULARES
                            && producto.getNombre() != null
                            && producto.getNombre().toLowerCase(Locale.ROOT).contains("iphone 16 pro max")) {
                        resultado.add(factura);
                        break;
                    }
                }
            }
        }
        return resultado;
    }

    // punto 6: Obtener las facturas que tenga un cliente donde su nombre sea juan y haya comprado un celular de marca Iphone 16 pro max
    public ArrayList<Factura> obtenerFacturasJuanConIphone16ProMax() {
        ArrayList<Factura> resultado = new ArrayList<>();
        for (Factura factura : obtenerFacturasIphone16ProMax()) {
            Cliente cliente = factura.cliente();
            if (cliente != null && cliente.getNombreCompleto() != null
                    && cliente.getNombreCompleto().strip().toLowerCase(Locale.ROOT).startsWith("juan")) {
                resultado.add(factura);
            }
        }
        return resultado;
    }

    // Punto 7: Implementar un método que reciba una categoría y retorne todos los productos registrados que pertenezcan a ella.
    public ArrayList<Producto> obtenerProductosPorCategoria(Categoria categoria) {
        ArrayList<Producto> resultado = new ArrayList<>();
        if (categoria == null) return resultado;
        for (Producto producto : hashMaplistaProductos.values()) {
            if (producto.getCategoria() == categoria) {
                resultado.add(producto);
            }
        }
        return resultado;
    }

    // punto 8: Implementar un método que reciba un precio mínimo y un precio máximo, y retorne los productos cuyo precio se encuentre dentro de ese rango, incluyendo ambos límites.
    public ArrayList<Producto> obtenerProductosPorRangoPrecio(double precioMinimo, double precioMaximo) {
        ArrayList<Producto> resultado = new ArrayList<>();
        if (precioMinimo > precioMaximo) return resultado;
        for (Producto producto : hashMaplistaProductos.values()) {
            if (producto.getValor() >= precioMinimo && producto.getValor() <= precioMaximo) {
                resultado.add(producto);
            }
        }
        return resultado;
    }

    // punto 9: Implementar un método que retorne todos los productos registrados en la tienda, ordenados de menor a mayor según su precio.
    public ArrayList<Producto> obtenerProductosOrdenadosPorPrecio() {
        ArrayList<Producto> resultado = new ArrayList<>(hashMaplistaProductos.values());
        resultado.sort(Comparator.comparingDouble(Producto::getValor));
        return resultado;
    }

    // punto 10: Implementar un método que identifique el producto con el precio más alto de la tienda. Si no existen productos registrados, el método debe retornar un Optional vacío.
    public Optional<Producto> obtenerProductoMayorPrecio() {
        return hashMaplistaProductos.values().stream()
                .max(Comparator.comparingDouble(Producto::getValor));
    }

    // Punto 11: Implementar un método que reciba el nombre de una ciudad y retorne todos los clientes que residan en ella. La búsqueda debe realizarse sin diferenciar entre mayúsculas y minúsculas.
    public ArrayList<Cliente> obtenerClientesPorCiudad(String ciudad) {
        ArrayList<Cliente> resultado = new ArrayList<>();
        if (ciudad == null) return resultado;
        for (Cliente cliente : listaClientes) {
            if (cliente.getCiudadResidencia() != null
                    && cliente.getCiudadResidencia().strip().equalsIgnoreCase(ciudad.strip())) {
                resultado.add(cliente);
            }
        }
        return resultado;
    }
}