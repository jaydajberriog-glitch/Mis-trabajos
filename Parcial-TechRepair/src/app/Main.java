package app;

import model.*;
import javax.swing.JOptionPane;
import java.util.List;

public class Main {

    private static final TechRepair empresa = new TechRepair("TechRepair");

    public static void main(String[] args) {
        mostrarMenu();
    }

    public static void mostrarMenu() {
        int opcion = 0;

        do {
            String menu = """
                ===== MENÚ TECHREPAIR =====
                
                Seleccione una de las siguientes opciones:
                
                1. Registrar cliente
                2. Registrar equipo
                3. Registrar técnico
                4. Registrar repuesto
                5. Crear orden de servicio
                6. Agregar detalle a una orden
                7. Asignar técnico
                8. Registrar repuestos utilizados
                9. Calcular valor de un detalle
                10. Calcular valor total de una orden
                11. Actualizar estado de un detalle
                12. Finalizar una orden
                13. Consultar órdenes de un cliente
                14. Consultar trabajos de un técnico
                15. Consultar equipos pendientes
                16. Reporte de servicios
                17. Reporte de técnicos
                18. Consultar inventario crítico
                19. Salir
                """;

            try {
                opcion = ingresarEntero(menu + "\nIngrese la opción: ");

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
                        mostrarMensaje("¡ADIÓS! Gracias por utilizar TechRepair.");
                        break;
                    default:
                        mostrarMensaje("Opción no válida");
                        break;
                }
            } catch (Exception e) {
                mostrarMensaje("Error: " + e.getMessage());
            }

        } while (opcion != 19);
    }

    public static String ingresarTexto(String mensaje) {
        String valor = JOptionPane.showInputDialog(null, mensaje, "TechRepair", JOptionPane.QUESTION_MESSAGE);
        if (valor == null || valor.trim().isEmpty()) {
            throw new IllegalArgumentException("Campo vacío o venta cancelada.");
        }
        return valor.trim();
    }

    public static int ingresarEntero(String mensaje) {
        String texto = ingresarTexto(mensaje);
        return Integer.parseInt(texto);
    }

    public static double ingresarDouble(String mensaje) {
        String texto = ingresarTexto(mensaje);
        return Double.parseDouble(texto.replace(',', '.'));
    }

    public static boolean ingresarBooleano(String mensaje) {
        int respuesta = JOptionPane.showConfirmDialog(null, mensaje, "TechRepair", JOptionPane.YES_NO_OPTION);
        return respuesta == JOptionPane.YES_OPTION;
    }

    public static void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(null, mensaje, "TechRepair", JOptionPane.INFORMATION_MESSAGE);
    }

    private static void registrarCliente() {
        String doc = ingresarTexto("Digite el número de documento:");
        String nombre = ingresarTexto("Digite el nombre completo:");
        String tel = ingresarTexto("Digite el teléfono:");
        String correo = ingresarTexto("Digite el correo electrónico:");
        String dir = ingresarTexto("Digite la dirección:");

        Cliente c = new Cliente(doc, nombre, tel, correo, dir);
        mostrarMensaje(empresa.registrarCliente(c));
    }

    private static void registrarEquipo() {
        String documento = ingresarTexto("Documento del cliente propietario:");
        Cliente cliente = empresa.buscarCliente(documento).orElseThrow(() -> new IllegalArgumentException("Primero registre ese cliente."));

        int tipo = ingresarEntero("Tipo de equipo:\n1. Portátil\n2. Escritorio\n3. Teléfono inteligente");
        String codigo = ingresarTexto("Código único del equipo:");
        String serie = ingresarTexto("Número de serie:");
        String marca = ingresarTexto("Marca:");
        String modelo = ingresarTexto("Modelo:");
        int anio = ingresarEntero("Año de fabricación:");
        String estado = ingresarTexto("Estado físico al ingresar:");

        Equipo equipo = switch (tipo) {
            case 1 -> new ComputadorPortatil(codigo, serie, marca, modelo, anio, estado, cliente, ingresarDouble("Pantalla en pulgadas:"), ingresarEntero("RAM en GB:"), ingresarEntero("Almacenamiento en GB:"), ingresarBooleano("¿Batería extraíble?"));
            case 2 -> new ComputadorEscritorio(codigo, serie, marca, modelo, anio, estado, cliente, ingresarTexto("Tipo de procesador:"), ingresarEntero("RAM en GB:"), ingresarEntero("Almacenamiento en GB:"), ingresarBooleano("¿Tarjeta gráfica dedicada?"));
            case 3 -> new TelefonoInteligente(codigo, serie, marca, modelo, anio, estado, cliente, ingresarTexto("Sistema operativo:"), ingresarEntero("Almacenamiento en GB:"), ingresarEntero("Batería en mAh:"), ingresarBooleano("¿Compatible con 5G?"));
            default -> throw new IllegalArgumentException("Tipo de equipo no válido.");
        };
        mostrarMensaje(empresa.registrarEquipo(equipo));
    }

    private static void registrarTecnico() {
        String codigo = ingresarTexto("Código del técnico:");
        String nombre = ingresarTexto("Nombre completo:");
        String documento = ingresarTexto("Documento de identidad:");

        int esp = ingresarEntero("Especialidad:\n1. Computadores\n2. Dispositivos móviles\n3. Soporte general");
        Tecnico.EspecialidadTecnico especialidad = Tecnico.EspecialidadTecnico.values()[esp - 1];

        int est = ingresarEntero("Estado:\n1. Activo\n2. Inactivo");
        Tecnico.EstadoTecnico estado = Tecnico.EstadoTecnico.values()[est - 1];

        mostrarMensaje(empresa.registrarTecnico(new Tecnico(codigo, nombre, documento, especialidad, estado)));
    }

    private static void registrarRepuesto() {
        String codigo = ingresarTexto("Código del repuesto:");
        String nombre = ingresarTexto("Nombre:");
        String descripcion = ingresarTexto("Descripción:");
        double precio = ingresarDouble("Precio unitario:");
        int cantidad = ingresarEntero("Cantidad inicial en inventario:");

        int cat = ingresarEntero("Categoría:\n1. Computadores\n2. Dispositivos móviles\n3. Universal");
        Repuesto.CategoriaRepuesto categoria = Repuesto.CategoriaRepuesto.values()[cat - 1];

        mostrarMensaje(empresa.registrarRepuesto(new Repuesto(codigo, nombre, descripcion, precio, cantidad, categoria)));
    }

    private static void crearOrden() {
        String codigo = ingresarTexto("Código de la orden:");
        String doc = ingresarTexto("Documento del cliente:");
        String obs = ingresarTexto("Observaciones generales:");
        mostrarMensaje(empresa.crearOrden(codigo, doc, obs));
    }

    private static void agregarDetalle() {
        OrdenServicio orden = empresa.buscarOrden(ingresarTexto("Código de la orden:")).orElseThrow(() -> new IllegalArgumentException("Orden no encontrada."));
        List<Equipo> equipos = empresa.getEquipos().stream().filter(e -> e.getCliente().getDocumento().equalsIgnoreCase(orden.getCliente().getDocumento())).toList();
        if (equipos.isEmpty()) throw new IllegalArgumentException("El cliente no tiene equipos registrados.");

        StringBuilder sb = new StringBuilder("Equipos del cliente:\n");
        for (Equipo e : equipos) sb.append(e).append("\n");
        mostrarMensaje(sb.toString());

        Equipo equipo = empresa.buscarEquipo(ingresarTexto("Código del equipo a intervenir:")).orElseThrow(() -> new IllegalArgumentException("Equipo no encontrado."));
        Servicio servicio = empresa.buscarServicio(ingresarTexto("Código del servicio (SER-DIAG, SER-MANT, SER-REP):")).orElseThrow(() -> new IllegalArgumentException("Servicio no encontrado."));
        DetalleServicio detalle = new DetalleServicio(ingresarTexto("Código del detalle:"), equipo, servicio, ingresarTexto("Falla reportada:"));

        if (servicio instanceof ReparacionCorrectiva) {
            detalle.registrarHoras(ingresarDouble("Horas de mano de obra:"));
        }
        orden.agregarDetalle(detalle);
        mostrarMensaje("Detalle agregado correctamente a la orden.");
    }

    private static DetalleServicio buscarDetalle(String codigoOrden, String codigoDetalle) {
        OrdenServicio o = empresa.buscarOrden(codigoOrden).orElseThrow(() -> new IllegalArgumentException("Orden no encontrada."));
        return o.getDetalles().stream().filter(d -> d.getCodigo().equalsIgnoreCase(codigoDetalle)).findFirst().orElseThrow(() -> new IllegalArgumentException("Detalle no encontrado."));
    }

    private static void asignarTecnico() {
        DetalleServicio d = buscarDetalle(ingresarTexto("Código de la orden:"), ingresarTexto("Código del detalle:"));
        Tecnico t = empresa.buscarTecnico(ingresarTexto("Código del técnico:")).orElseThrow(() -> new IllegalArgumentException("Técnico no encontrado."));
        d.asignarTecnico(t);
        mostrarMensaje("Técnico asignado correctamente.");
    }

    private static void registrarRepuestoUtilizado() {
        DetalleServicio d = buscarDetalle(ingresarTexto("Código de la orden:"), ingresarTexto("Código del detalle:"));
        Repuesto r = empresa.buscarRepuesto(ingresarTexto("Código del repuesto:")).orElseThrow(() -> new IllegalArgumentException("Repuesto no encontrado."));
        d.agregarRepuesto(r, ingresarEntero("Cantidad utilizada:"));
        mostrarMensaje("Repuesto agregado. Existencias restantes: " + r.getCantidadDisponible());
    }

    private static void consultarValorDetalle() {
        DetalleServicio d = buscarDetalle(ingresarTexto("Código de la orden:"), ingresarTexto("Código del detalle:"));
        mostrarMensaje(d.toString());
    }

    private static void consultarValorOrden() {
        OrdenServicio o = empresa.buscarOrden(ingresarTexto("Código de la orden:")).orElseThrow(() -> new IllegalArgumentException("Orden no encontrada."));
        StringBuilder sb = new StringBuilder(o.toString()).append("\n\nDetalles de la orden:\n");
        for (DetalleServicio d : o.getDetalles()) sb.append(" - ").append(d).append("\n");
        mostrarMensaje(sb.toString());
    }

    private static void actualizarEstadoDetalle() {
        DetalleServicio d = buscarDetalle(ingresarTexto("Código de la orden:"), ingresarTexto("Código del detalle:"));
        int est = ingresarEntero("Estado:\n1. Pendiente\n2. En proceso\n3. Finalizado\n4. Cancelado");
        DetalleServicio.EstadoDetalle estado = DetalleServicio.EstadoDetalle.values()[est - 1];
        d.actualizarEstado(estado);
        mostrarMensaje("Estado actualizado correctamente.");
    }

    private static void finalizarOrden() {
        OrdenServicio o = empresa.buscarOrden(ingresarTexto("Código de la orden:")).orElseThrow(() -> new IllegalArgumentException("Orden no encontrada."));
        o.finalizar();
        mostrarMensaje("Orden finalizada correctamente.");
    }

    private static void consultarOrdenesCliente() {
        String doc = ingresarTexto("Documento del cliente:");
        List<OrdenServicio> lista = empresa.consultarOrdenesCliente(doc);
        if (lista.isEmpty()) {
            mostrarMensaje("No hay órdenes registradas para ese cliente.");
            return;
        }
        StringBuilder sb = new StringBuilder("Órdenes del cliente:\n");
        for (OrdenServicio o : lista) {
            sb.append(o).append("\n");
            for (DetalleServicio d : o.getDetalles()) sb.append("    -> ").append(d).append("\n");
        }
        mostrarMensaje(sb.toString());
    }

    private static void consultarTrabajosTecnico() {
        String codigo = ingresarTexto("Código del técnico:");
        List<DetalleServicio> lista = empresa.consultarTrabajosTecnico(codigo);
        if (lista.isEmpty()) {
            mostrarMensaje("El técnico no tiene trabajos asignados.");
            return;
        }
        StringBuilder sb = new StringBuilder("Trabajos del técnico:\n");
        for (DetalleServicio.EstadoDetalle estado : DetalleServicio.EstadoDetalle.values()) {
            sb.append("\n-- ").append(estado).append(" --\n");
            for (DetalleServicio d : lista) {
                if (d.getEstado() == estado) sb.append("  ").append(d).append("\n");
            }
        }
        mostrarMensaje(sb.toString());
    }

    private static void consultarEquiposPendientes() {
        List<DetalleServicio> lista = empresa.consultarEquiposPendientes();
        if (lista.isEmpty()) {
            mostrarMensaje("No hay intervenciones pendientes.");
            return;
        }
        StringBuilder sb = new StringBuilder("Intervenciones pendientes:\n");
        for (DetalleServicio d : lista) sb.append(" - ").append(d).append("\n");
        mostrarMensaje(sb.toString());
    }

    private static void consultarInventarioCritico() {
        List<Repuesto> lista = empresa.consultarInventarioCritico();
        if (lista.isEmpty()) {
            mostrarMensaje("No hay repuestos con menos de 5 unidades.");
            return;
        }
        StringBuilder sb = new StringBuilder("Repuestos con inventario crítico (< 5 unidades):\n");
        for (Repuesto r : lista) sb.append(" - ").append(r).append("\n");
        mostrarMensaje(sb.toString());
    }
}
