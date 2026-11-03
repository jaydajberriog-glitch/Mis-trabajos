package app;

import model.*;
import javax.swing.JOptionPane;
import java.util.List;
import java.util.Optional;

public class Main {

    private static Tienda tienda = new Tienda("Tienda UQ", "123456789", "7430000");

    public static void main(String[] args) {
        cargarDatosPrueba();
        mostrarMenu();
    }

    public static void mostrarMenu() {
        int opcion = 0;

        do {
            String menu = """
                ===== MENÚ GESTIÓN TIENDA =====
                
                Seleccione una de las siguientes opciones:
                
                1. Registrar cliente
                2. Buscar cliente por documento
                3. Registrar producto
                4. Buscar producto por código
                5. Consultar productos con stock >= 10
                6. Consultar productos por categoría
                7. Consultar productos por rango de precio
                8. Consultar producto con precio más alto
                9. Consultar clientes por ciudad
                10. Consultar facturas con iPhone 16 Pro Max
                11. Salir
                """;

            try {
                opcion = ingresarEntero(menu + "\nIngrese la opción: ");

                switch (opcion) {
                    case 1:
                        registrarCliente();
                        break;
                    case 2:
                        buscarCliente();
                        break;
                    case 3:
                        registrarProducto();
                        break;
                    case 4:
                        buscarProducto();
                        break;
                    case 5:
                        consultarProductosStock();
                        break;
                    case 6:
                        consultarProductosCategoria();
                        break;
                    case 7:
                        consultarProductosRangoPrecio();
                        break;
                    case 8:
                        consultarProductoMayorPrecio();
                        break;
                    case 9:
                        consultarClientesCiudad();
                        break;
                    case 10:
                        consultarFacturasIphone();
                        break;
                    case 11:
                        mostrarMensaje("¡ADIÓS! Gracias por utilizar el sistema de la Tienda.");
                        break;
                    default:
                        mostrarMensaje("Opción no válida");
                        break;
                }
            } catch (Exception e) {
                mostrarMensaje("Error: " + e.getMessage());
            }

        } while (opcion != 11);
    }

    private static String ingresarTexto(String mensaje) {
        return JOptionPane.showInputDialog(null, mensaje);
    }

    private static int ingresarEntero(String mensaje) {
        String val = JOptionPane.showInputDialog(null, mensaje);
        if (val == null || val.trim().isEmpty()) return -1;
        return Integer.parseInt(val.trim());
    }

    private static double ingresarDouble(String mensaje) {
        String val = JOptionPane.showInputDialog(null, mensaje);
        if (val == null || val.trim().isEmpty()) return 0.0;
        return Double.parseDouble(val.trim());
    }

    private static void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(null, mensaje);
    }

    private static void registrarCliente() {
        String doc = ingresarTexto("Ingrese Documento / Cédula:");
        String nombre = ingresarTexto("Ingrese Nombre completo:");
        String tel = ingresarTexto("Ingrese Teléfono:");
        String correo = ingresarTexto("Ingrese Correo:");
        String ciudad = ingresarTexto("Ingrese Ciudad de residencia:");

        if (doc != null && nombre != null) {
            Cliente cliente = new Cliente(doc, nombre, tienda, tel, correo, ciudad);
            String mensaje = tienda.registrarCliente(cliente);
            mostrarMensaje(mensaje);
        }
    }

    private static void buscarCliente() {
        String doc = ingresarTexto("Ingrese el documento a buscar:");
        if (doc != null) {
            Cliente c = tienda.buscarCliente1(doc);
            if (c != null) {
                mostrarMensaje("Cliente Encontrado:\n" +
                        "Nombre: " + c.getNombreCompleto() + "\n" +
                        "Ciudad: " + c.getCiudadResidencia() + "\n" +
                        "Teléfono: " + c.getTelefono());
            } else {
                mostrarMensaje("No se encontró ningún cliente con ese documento.");
            }
        }
    }

    private static void registrarProducto() {
        String cod = ingresarTexto("Código del producto:");
        String nom = ingresarTexto("Nombre del producto:");
        String desc = ingresarTexto("Descripción:");
        int cant = ingresarEntero("Cantidad disponible:");
        double valor = ingresarDouble("Valor / Precio:");

        Producto p = new Producto(nom, cod, desc, cant, valor, Categoria.COMPUTADORES, tienda);
        String mensaje = tienda.registrarProducto(p);
        mostrarMensaje(mensaje);
    }

    private static void buscarProducto() {
        String cod = ingresarTexto("Ingrese el código del producto:");
        if (cod != null) {
            Optional<Producto> opt = tienda.buscarProducto(cod);
            if (opt.isPresent()) {
                Producto p = opt.get();
                mostrarMensaje("Producto Encontrado:\n" +
                        "Nombre: " + p.getNombre() + "\n" +
                        "Precio: $" + p.getValor() + "\n" +
                        "Stock: " + p.getCantidadDisponible() + "\n" +
                        "Categoría: " + p.getCategoria());
            } else {
                mostrarMensaje("No existe un producto registrado con ese código.");
            }
        }
    }

    private static void consultarProductosStock() {
        List<Producto> lista = tienda.obtenerMayoresDiez();
        if (lista.isEmpty()) {
            mostrarMensaje("No hay productos con stock >= 10.");
            return;
        }
        StringBuilder sb = new StringBuilder("--- Productos con stock >= 10 ---\n");
        for (Producto p : lista) {
            sb.append("• ").append(p.getNombre()).append(" | Stock: ").append(p.getCantidadDisponible()).append("\n");
        }
        mostrarMensaje(sb.toString());
    }

    private static void consultarProductosCategoria() {
        List<Producto> lista = tienda.obtenerProductosPorCategoria(Categoria.CELULARES);
        if (lista.isEmpty()) {
            mostrarMensaje("No hay productos en la categoría CELULARES.");
            return;
        }
        StringBuilder sb = new StringBuilder("--- Productos en la Categoría CELULARES ---\n");
        for (Producto p : lista) {
            sb.append("• ").append(p.getNombre()).append(" - $").append(p.getValor()).append("\n");
        }
        mostrarMensaje(sb.toString());
    }

    private static void consultarProductosRangoPrecio() {
        double min = ingresarDouble("Ingrese precio mínimo:");
        double max = ingresarDouble("Ingrese precio máximo:");
        List<Producto> lista = tienda.obtenerProductosPorRangoPrecio(min, max);

        if (lista.isEmpty()) {
            mostrarMensaje("No hay productos en ese rango de precios.");
            return;
        }
        StringBuilder sb = new StringBuilder("--- Productos entre $" + min + " y $" + max + " ---\n");
        for (Producto p : lista) {
            sb.append("• ").append(p.getNombre()).append(" - $").append(p.getValor()).append("\n");
        }
        mostrarMensaje(sb.toString());
    }

    private static void consultarProductoMayorPrecio() {
        Optional<Producto> mayor = tienda.obtenerProductoMayorPrecio();
        if (mayor.isPresent()) {
            Producto p = mayor.get();
            mostrarMensaje("El producto con precio más alto es:\n" + p.getNombre() + " | Precio: $" + p.getValor());
        } else {
            mostrarMensaje("No hay productos en la tienda.");
        }
    }

    private static void consultarClientesCiudad() {
        String ciudad = ingresarTexto("Ingrese la ciudad a consultar:");
        if (ciudad != null) {
            List<Cliente> clientes = tienda.obtenerClientesPorCiudad(ciudad);
            if (clientes.isEmpty()) {
                mostrarMensaje("No hay clientes registrados en " + ciudad);
            } else {
                StringBuilder sb = new StringBuilder("Clientes en " + ciudad + ":\n");
                for (Cliente c : clientes) {
                    sb.append("• ").append(c.getNombreCompleto()).append(" (ID: ").append(c.getDocumentoIdentidad()).append(")\n");
                }
                mostrarMensaje(sb.toString());
            }
        }
    }

    private static void consultarFacturasIphone() {
        List<Factura> facturas = tienda.obtenerFacturasIphone16ProMax();
        if (facturas.isEmpty()) {
            mostrarMensaje("No hay facturas con iPhone 16 Pro Max.");
        } else {
            StringBuilder sb = new StringBuilder("Facturas con iPhone 16 Pro Max:\n");
            for (Factura f : facturas) {
                sb.append("• Código: ").append(f.codigo()).append(" | Cliente: ").append(f.cliente().getNombreCompleto()).append("\n");
            }
            mostrarMensaje(sb.toString());
        }
    }

    private static void cargarDatosPrueba() {
        tienda.registrarCliente(new Cliente("1094", "Juan Perez", tienda, "3121234567", "juan@gmail.com", "Armenia"));
        tienda.registrarCliente(new Cliente("1095", "Rosa Gomez", tienda, "3159876543", "rosa@gmail.com", "Pereira"));

        tienda.registrarProducto(new Producto("Iphone 16 pro max", "P01", "Gama alta", 15, 5500000, Categoria.CELULARES, tienda));
        tienda.registrarProducto(new Producto("Laptop Asus", "P02", "16GB RAM SSD", 5, 3200000, Categoria.COMPUTADORES, tienda));
        tienda.registrarProducto(new Producto("Mouse Logitech", "P03", "Ergonómico", 25, 120000, Categoria.ACCESORIOS, tienda));
    }
}