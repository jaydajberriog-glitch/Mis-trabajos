package app;

import model.*;
import javax.swing.JOptionPane;
import java.util.List;

public class Main {

    // Objeto principal de la empresa
    private static TechRepair empresa = new TechRepair("TechRepair");

    public static void main(String[] args) {
        // El sistema inicia totalmente desde cero
        mostrarMenu();
    }

    public static void mostrarMenu() {
        int opcion = 0;

        do {
            String menu = "===== MENÚ TECHREPAIR =====\n\n" +
                    "1. Registrar cliente\n" +
                    "2. Registrar equipo\n" +
                    "3. Registrar técnico\n" +
                    "4. Registrar repuesto\n" +
                    "5. Crear orden de servicio\n" +
                    "6. Agregar detalle a una orden\n" +
                    "7. Asignar técnico\n" +
                    "8. Registrar repuestos utilizados\n" +
                    "9. Calcular valor de un detalle\n" +
                    "10. Calcular valor total de una orden\n" +
                    "11. Actualizar estado de un detalle\n" +
                    "12. Finalizar una orden\n" +
                    "13. Consultar órdenes de un cliente\n" +
                    "14. Consultar trabajos de un técnico\n" +
                    "15. Consultar equipos pendientes\n" +
                    "16. Reporte de servicios\n" +
                    "17. Reporte de técnicos\n" +
                    "18. Consultar inventario crítico\n" +
                    "19. Salir\n\n" +
                    "Ingrese una opción (1-19):";

            String entrada = JOptionPane.showInputDialog(null, menu, "TechRepair", JOptionPane.QUESTION_MESSAGE);

            // Si le da al botón Cancelar o a la X, salimos del bucle
            if (entrada == null) {
                break;
            }

            try {
                opcion = Integer.parseInt(entrada.trim());

                switch (opcion) {
                    case 1:
                        registrarCliente();
                        break;
                    case 2:
                        registrarEquipo();
                        break;
                    case 3:
                        registrarTecnico();
                        break;
                    case 4:
                        registrarRepuesto();
                        break;
                    case 5:
                        crearOrden();
                        break;
                    case 6:
                        agregarDetalle();
                        break;
                    case 7:
                        asignarTecnico();
                        break;
                    case 8:
                        registrarRepuestoUtilizado();
                        break;
                    case 9:
                        consultarValorDetalle();
                        break;
                    case 10:
                        consultarValorOrden();
                        break;
                    case 11:
                        actualizarEstadoDetalle();
                        break;
                    case 12:
                        finalizarOrden();
                        break;
                    case 13:
                        consultarOrdenesCliente();
                        break;
                    case 14:
                        consultarTrabajosTecnico();
                        break;
                    case 15:
                        consultarEquiposPendientes();
                        break;
                    case 16:
                        mostrarMensaje(empresa.reporteServicios());
                        break;
                    case 17:
                        mostrarMensaje(empresa.reporteTecnicos());
                        break;
                    case 18:
                        consultarInventarioCritico();
                        break;
                    case 19:
                        mostrarMensaje("¡Hasta luego! Gracias por usar el sistema.");
                        break;
                    default:
                        mostrarMensaje("Opción no válida. Digite un número entre 1 y 19.");
                        break;
                }
            } catch (Exception e) {
                mostrarMensaje("Error: " + e.getMessage());
            }

        } while (opcion != 19);
    }

    // =========================================================
    // Métodos sencillos para pedir datos por ventana (JOptionPane)
    // =========================================================

    public static String pedirTexto(String mensaje) {
        String texto = JOptionPane.showInputDialog(null, mensaje, "TechRepair", JOptionPane.QUESTION_MESSAGE);
        if (texto == null || texto.trim().isEmpty()) {
            throw new IllegalArgumentException("El campo no puede estar vacío o se canceló.");
        }
        return texto.trim();
    }

    public static int pedirEntero(String mensaje) {
        String texto = pedirTexto(mensaje);
        return Integer.parseInt(texto);
    }

    public static double pedirDouble(String mensaje) {
        String texto = pedirTexto(mensaje);
        return Double.parseDouble(texto.replace(',', '.'));
    }

    public static void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(null, mensaje, "TechRepair", JOptionPane.INFORMATION_MESSAGE);
    }

    // =========================================================
    // Lógica sencilla de cada opción (1 a 18)
    // =========================================================

    // Opción 1
    private static void registrarCliente() {
        String doc = pedirTexto("Digite el documento del cliente:");
        String nom = pedirTexto("Digite el nombre completo:");
        String tel = pedirTexto("Digite el teléfono:");
        String cor = pedirTexto("Digite el correo electrónico:");
        String dir = pedirTexto("Digite la dirección:");

        Cliente c = new Cliente(doc, nom, tel, cor, dir);
        String respuesta = empresa.registrarCliente(c);
        mostrarMensaje(respuesta);
    }

    // Opción 2
    private static void registrarEquipo() {
        String doc = pedirTexto("Documento del cliente propietario del equipo:");
        
        // Buscamos si existe el cliente
        if (!empresa.buscarCliente(doc).isPresent()) {
            mostrarMensaje("El cliente con documento " + doc + " no está registrado. Registre primero al cliente.");
            return;
        }
        Cliente cliente = empresa.buscarCliente(doc).get();

        int tipo = pedirEntero("Seleccione el tipo de equipo:\n1. Portátil\n2. Escritorio\n3. Teléfono Inteligente");
        String cod = pedirTexto("Código del equipo:");
        String serie = pedirTexto("Número de serie:");
        String marca = pedirTexto("Marca:");
        String modelo = pedirTexto("Modelo:");
        int anio = pedirEntero("Año de fabricación:");
        String estado = pedirTexto("Estado físico del equipo:");

        Equipo equipo = null;

        if (tipo == 1) {
            double pantalla = pedirDouble("Tamaño de pantalla en pulgadas:");
            int ram = pedirEntero("RAM en GB:");
            int alm = pedirEntero("Almacenamiento en GB:");
            equipo = new ComputadorPortatil(cod, serie, marca, modelo, anio, estado, cliente, pantalla, ram, alm, true);
        } else if (tipo == 2) {
            String proc = pedirTexto("Tipo de procesador:");
            int ram = pedirEntero("RAM en GB:");
            int alm = pedirEntero("Almacenamiento en GB:");
            equipo = new ComputadorEscritorio(cod, serie, marca, modelo, anio, estado, cliente, proc, ram, alm, false);
        } else if (tipo == 3) {
            String so = pedirTexto("Sistema operativo:");
            int alm = pedirEntero("Almacenamiento en GB:");
            int bat = pedirEntero("Batería en mAh:");
            equipo = new TelefonoInteligente(cod, serie, marca, modelo, anio, estado, cliente, so, alm, bat, true);
        } else {
            mostrarMensaje("Tipo de equipo no válido.");
            return;
        }

        String respuesta = empresa.registrarEquipo(equipo);
        mostrarMensaje(respuesta);
    }

    // Opción 3
    private static void registrarTecnico() {
        String cod = pedirTexto("Código del técnico:");
        String nom = pedirTexto("Nombre completo:");
        String doc = pedirTexto("Documento de identidad:");

        int esp = pedirEntero("Especialidad:\n1. Computadores\n2. Dispositivos móviles\n3. Soporte general");
        Tecnico.EspecialidadTecnico especialidad = Tecnico.EspecialidadTecnico.COMPUTADORES;
        if (esp == 2) {
            especialidad = Tecnico.EspecialidadTecnico.DISPOSITIVOS_MOVILES;
        } else if (esp == 3) {
            especialidad = Tecnico.EspecialidadTecnico.SOPORTE_GENERAL;
        }

        Tecnico t = new Tecnico(cod, nom, doc, especialidad, Tecnico.EstadoTecnico.ACTIVO);
        String respuesta = empresa.registrarTecnico(t);
        mostrarMensaje(respuesta);
    }

    // Opción 4
    private static void registrarRepuesto() {
        String cod = pedirTexto("Código del repuesto:");
        String nom = pedirTexto("Nombre:");
        String desc = pedirTexto("Descripción:");
        double precio = pedirDouble("Precio unitario:");
        int cant = pedirEntero("Cantidad disponible:");

        int cat = pedirEntero("Categoría:\n1. Computadores\n2. Dispositivos móviles\n3. Universal");
        Repuesto.CategoriaRepuesto categoria = Repuesto.CategoriaRepuesto.COMPUTADORES;
        if (cat == 2) {
            categoria = Repuesto.CategoriaRepuesto.DISPOSITIVOS_MOVILES;
        } else if (cat == 3) {
            categoria = Repuesto.CategoriaRepuesto.UNIVERSAL;
        }

        Repuesto r = new Repuesto(cod, nom, desc, precio, cant, categoria);
        String respuesta = empresa.registrarRepuesto(r);
        mostrarMensaje(respuesta);
    }

    // Opción 5
    private static void crearOrden() {
        String cod = pedirTexto("Código de la nueva orden:");
        String doc = pedirTexto("Documento del cliente:");
        String obs = pedirTexto("Observaciones iniciales:");

        String respuesta = empresa.crearOrden(cod, doc, obs);
        mostrarMensaje(respuesta);
    }

    // Opción 6
    private static void agregarDetalle() {
        String codOrden = pedirTexto("Código de la orden:");
        if (!empresa.buscarOrden(codOrden).isPresent()) {
            mostrarMensaje("Orden no encontrada.");
            return;
        }
        OrdenServicio orden = empresa.buscarOrden(codOrden).get();

        String codEquipo = pedirTexto("Código del equipo a reparar:");
        if (!empresa.buscarEquipo(codEquipo).isPresent()) {
            mostrarMensaje("Equipo no encontrado.");
            return;
        }
        Equipo equipo = empresa.buscarEquipo(codEquipo).get();

        int serv = pedirEntero("Seleccione Servicio:\n1. Diagnóstico\n2. Mantenimiento Preventivo\n3. Reparación Correctiva");
        Servicio servicio = null;
        if (serv == 1) {
            servicio = empresa.buscarServicio("SER-DIAG").orElse(null);
        } else if (serv == 2) {
            servicio = empresa.buscarServicio("SER-MANT").orElse(null);
        } else if (serv == 3) {
            servicio = empresa.buscarServicio("SER-REP").orElse(null);
        }

        if (servicio == null) {
            mostrarMensaje("Servicio no válido.");
            return;
        }

        String codDetalle = pedirTexto("Código del detalle (ej: DET-01):");
        String falla = pedirTexto("Descripción de la falla reportada:");

        DetalleServicio detalle = new DetalleServicio(codDetalle, equipo, servicio, falla);

        if (servicio instanceof ReparacionCorrectiva) {
            double horas = pedirDouble("Horas de trabajo estimadas:");
            detalle.registrarHoras(horas);
        }

        orden.agregarDetalle(detalle);
        mostrarMensaje("Detalle agregado con éxito a la orden " + codOrden);
    }

    // Opción 7
    private static void asignarTecnico() {
        String codOrden = pedirTexto("Código de la orden:");
        String codDetalle = pedirTexto("Código del detalle:");
        String codTecnico = pedirTexto("Código del técnico:");

        DetalleServicio detalle = buscarDetalleEnOrden(codOrden, codDetalle);
        if (detalle == null) {
            mostrarMensaje("No se encontró el detalle o la orden.");
            return;
        }

        if (!empresa.buscarTecnico(codTecnico).isPresent()) {
            mostrarMensaje("Técnico no encontrado.");
            return;
        }
        Tecnico tecnico = empresa.buscarTecnico(codTecnico).get();

        detalle.asignarTecnico(tecnico);
        mostrarMensaje("Técnico " + tecnico.getNombre() + " asignado correctamente.");
    }

    // Opción 8
    private static void registrarRepuestoUtilizado() {
        String codOrden = pedirTexto("Código de la orden:");
        String codDetalle = pedirTexto("Código del detalle:");
        String codRepuesto = pedirTexto("Código del repuesto:");
        int cantidad = pedirEntero("Cantidad utilizada:");

        DetalleServicio detalle = buscarDetalleEnOrden(codOrden, codDetalle);
        if (detalle == null) {
            mostrarMensaje("No se encontró el detalle o la orden.");
            return;
        }

        if (!empresa.buscarRepuesto(codRepuesto).isPresent()) {
            mostrarMensaje("Repuesto no encontrado.");
            return;
        }
        Repuesto repuesto = empresa.buscarRepuesto(codRepuesto).get();

        detalle.agregarRepuesto(repuesto, cantidad);
        mostrarMensaje("Repuesto agregado con éxito.");
    }

    // Opción 9
    private static void consultarValorDetalle() {
        String codOrden = pedirTexto("Código de la orden:");
        String codDetalle = pedirTexto("Código del detalle:");

        DetalleServicio detalle = buscarDetalleEnOrden(codOrden, codDetalle);
        if (detalle == null) {
            mostrarMensaje("No se encontró el detalle.");
            return;
        }

        mostrarMensaje("Valor del detalle " + codDetalle + ": $" + detalle.calcularValor());
    }

    // Opción 10
    private static void consultarValorOrden() {
        String codOrden = pedirTexto("Código de la orden:");
        if (!empresa.buscarOrden(codOrden).isPresent()) {
            mostrarMensaje("Orden no encontrada.");
            return;
        }
        OrdenServicio orden = empresa.buscarOrden(codOrden).get();

        mostrarMensaje("El valor total de la orden " + codOrden + " es: $" + orden.calcularValorTotal());
    }

    // Opción 11
    private static void actualizarEstadoDetalle() {
        String codOrden = pedirTexto("Código de la orden:");
        String codDetalle = pedirTexto("Código del detalle:");

        DetalleServicio detalle = buscarDetalleEnOrden(codOrden, codDetalle);
        if (detalle == null) {
            mostrarMensaje("No se encontró el detalle.");
            return;
        }

        int est = pedirEntero("Seleccione nuevo estado:\n1. Pendiente\n2. En proceso\n3. Finalizado\n4. Cancelado");
        if (est == 1) detalle.actualizarEstado(DetalleServicio.EstadoDetalle.PENDIENTE);
        else if (est == 2) detalle.actualizarEstado(DetalleServicio.EstadoDetalle.EN_PROCESO);
        else if (est == 3) detalle.actualizarEstado(DetalleServicio.EstadoDetalle.FINALIZADO);
        else if (est == 4) detalle.actualizarEstado(DetalleServicio.EstadoDetalle.CANCELADO);

        mostrarMensaje("Estado actualizado con éxito.");
    }

    // Opción 12
    private static void finalizarOrden() {
        String codOrden = pedirTexto("Código de la orden:");
        if (!empresa.buscarOrden(codOrden).isPresent()) {
            mostrarMensaje("Orden no encontrada.");
            return;
        }
        OrdenServicio orden = empresa.buscarOrden(codOrden).get();

        orden.finalizar();
        mostrarMensaje("La orden " + codOrden + " ha sido finalizada.");
    }

    // Opción 13
    private static void consultarOrdenesCliente() {
        String doc = pedirTexto("Documento del cliente:");
        List<OrdenServicio> lista = empresa.consultarOrdenesCliente(doc);

        if (lista.isEmpty()) {
            mostrarMensaje("No se encontraron órdenes para el cliente con documento " + doc);
            return;
        }

        String resultado = "Órdenes del cliente:\n\n";
        for (OrdenServicio orden : lista) {
            resultado += orden.toString() + "\n";
        }
        mostrarMensaje(resultado);
    }

    // Opción 14
    private static void consultarTrabajosTecnico() {
        String codTecnico = pedirTexto("Código del técnico:");
        List<DetalleServicio> lista = empresa.consultarTrabajosTecnico(codTecnico);

        if (lista.isEmpty()) {
            mostrarMensaje("El técnico no tiene trabajos asignados.");
            return;
        }

        String resultado = "Trabajos del técnico " + codTecnico + ":\n\n";
        for (DetalleServicio d : lista) {
            resultado += d.toString() + "\n";
        }
        mostrarMensaje(resultado);
    }

    // Opción 15
    private static void consultarEquiposPendientes() {
        List<DetalleServicio> lista = empresa.consultarEquiposPendientes();

        if (lista.isEmpty()) {
            mostrarMensaje("No hay intervenciones ni equipos pendientes.");
            return;
        }

        String resultado = "Equipos e intervenciones pendientes:\n\n";
        for (DetalleServicio d : lista) {
            resultado += d.toString() + "\n";
        }
        mostrarMensaje(resultado);
    }

    // Opción 18
    private static void consultarInventarioCritico() {
        List<Repuesto> lista = empresa.consultarInventarioCritico();

        if (lista.isEmpty()) {
            mostrarMensaje("No hay repuestos en nivel crítico (menos de 5 unidades).");
            return;
        }

        String resultado = "Repuestos con inventario crítico (< 5 unidades):\n\n";
        for (Repuesto r : lista) {
            resultado += r.toString() + "\n";
        }
        mostrarMensaje(resultado);
    }

    // =========================================================
    // Búsqueda auxiliar simple con bucle 'for'
    // =========================================================

    private static DetalleServicio buscarDetalleEnOrden(String codOrden, String codDetalle) {
        if (!empresa.buscarOrden(codOrden).isPresent()) {
            return null;
        }
        OrdenServicio orden = empresa.buscarOrden(codOrden).get();
        for (DetalleServicio d : orden.getDetalles()) {
            if (d.getCodigo().equalsIgnoreCase(codDetalle)) {
                return d;
            }
        }
        return null;
    }
}
