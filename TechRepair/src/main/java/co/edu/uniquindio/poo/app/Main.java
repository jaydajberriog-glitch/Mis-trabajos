package co.edu.uniquindio.poo.app;

import co.edu.uniquindio.poo.model.*;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final TechRepair empresa = new TechRepair("TechRepair");

    public static void main(String[] args) {
        int opcion;
        do {
            mostrarMenu();
            opcion = leerEntero("Seleccione una opción: ");
            try {
                switch (opcion) {
                    case 1: registrarCliente(); break;
                    case 2: registrarEquipo(); break;
                    case 3: registrarTecnico(); break;
                    case 4: registrarRepuesto(); break;
                    case 5: crearOrden(); break;
                    case 6: agregarDetalle(); break;
                    case 7: asignarTecnico(); break;
                    case 8: registrarRepuestoUtilizado(); break;
                    case 9: consultarValorDetalle(); break;
                    case 10: consultarValorOrden(); break;
                    case 11: actualizarEstadoDetalle(); break;
                    case 12: finalizarOrden(); break;
                    case 13: consultarOrdenesCliente(); break;
                    case 14: consultarTrabajosTecnico(); break;
                    case 15: consultarEquiposPendientes(); break;
                    case 16: System.out.println(empresa.reporteServicios()); break;
                    case 17: System.out.println(empresa.reporteTecnicos()); break;
                    case 18: consultarInventarioCritico(); break;
                    case 0: System.out.println("¡Gracias por utilizar TechRepair!"); break;
                    default: System.out.println("Opción no válida.");
                }
            } catch (RuntimeException e) { System.out.println("No se pudo completar la operación: " + e.getMessage()); }
            if (opcion != 0) pausar();
        } while (opcion != 0);
    }

    private static void mostrarMenu() {
        System.out.println("\n========== TECHREPAIR ==========");
        System.out.println("1. Registrar cliente");
        System.out.println("2. Registrar equipo");
        System.out.println("3. Registrar técnico");
        System.out.println("4. Registrar repuesto");
        System.out.println("5. Crear orden de servicio");
        System.out.println("6. Agregar detalle a una orden");
        System.out.println("7. Asignar técnico");
        System.out.println("8. Registrar repuestos utilizados");
        System.out.println("9. Calcular valor de un detalle");
        System.out.println("10. Calcular valor total de una orden");
        System.out.println("11. Actualizar estado de un detalle");
        System.out.println("12. Finalizar una orden");
        System.out.println("13. Consultar órdenes de un cliente");
        System.out.println("14. Consultar trabajos de un técnico");
        System.out.println("15. Consultar equipos pendientes");
        System.out.println("16. Reporte de servicios");
        System.out.println("17. Reporte de técnicos");
        System.out.println("18. Consultar inventario crítico");
        System.out.println("0. Salir");
    }
    private static void registrarCliente() {
        Cliente c = new Cliente(leerTexto("Documento: "), leerTexto("Nombre completo: "), leerTexto("Teléfono: "), leerTexto("Correo: "), leerTexto("Dirección: "));
        System.out.println(empresa.registrarCliente(c));
    }
    private static void registrarEquipo() {
        String documento=leerTexto("Documento del cliente: "); Cliente cliente=empresa.buscarCliente(documento).orElseThrow(()->new IllegalArgumentException("Primero registre ese cliente."));
        System.out.println("1. Portátil  2. Escritorio  3. Teléfono inteligente"); int tipo=leerEntero("Tipo de equipo: ");
        String codigo=leerTexto("Código único: "), serie=leerTexto("Número de serie: "), marca=leerTexto("Marca: "), modelo=leerTexto("Modelo: "); int anio=leerEntero("Año de fabricación: "); String estado=leerTexto("Estado físico al ingresar: "); Equipo equipo;
        if(tipo==1) equipo=new ComputadorPortatil(codigo,serie,marca,modelo,anio,estado,cliente,leerDouble("Pantalla en pulgadas: "),leerEntero("RAM en GB: "),leerEntero("Almacenamiento en GB: "),leerBooleano("¿Batería extraíble? (s/n): "));
        else if(tipo==2) equipo=new ComputadorEscritorio(codigo,serie,marca,modelo,anio,estado,cliente,leerTexto("Tipo de procesador: "),leerEntero("RAM en GB: "),leerEntero("Almacenamiento en GB: "),leerBooleano("¿Tarjeta gráfica dedicada? (s/n): "));
        else if(tipo==3) equipo=new TelefonoInteligente(codigo,serie,marca,modelo,anio,estado,cliente,leerTexto("Sistema operativo: "),leerEntero("Almacenamiento en GB: "),leerEntero("Batería en mAh: "),leerBooleano("¿Compatible con 5G? (s/n): "));
        else throw new IllegalArgumentException("Tipo de equipo no válido.");
        System.out.println(empresa.registrarEquipo(equipo));
    }
    private static void registrarTecnico() {
        String codigo=leerTexto("Código: "), nombre=leerTexto("Nombre: "), documento=leerTexto("Documento: ");
        EspecialidadTecnico especialidad=leerEnum(EspecialidadTecnico.class,"Especialidad: 1 Computadores, 2 Dispositivos móviles, 3 Soporte general: ");
        EstadoTecnico estado=leerEnum(EstadoTecnico.class,"Estado: 1 Activo, 2 Inactivo: ");
        System.out.println(empresa.registrarTecnico(new Tecnico(codigo,nombre,documento,especialidad,estado)));
    }
    private static void registrarRepuesto() {
        String codigo=leerTexto("Código: "), nombre=leerTexto("Nombre: "), descripcion=leerTexto("Descripción: "); double precio=leerDouble("Precio unitario: "); int cantidad=leerEntero("Cantidad inicial: ");
        CategoriaRepuesto categoria=leerEnum(CategoriaRepuesto.class,"Categoría: 1 Computadores, 2 Dispositivos móviles, 3 Universal: ");
        System.out.println(empresa.registrarRepuesto(new Repuesto(codigo,nombre,descripcion,precio,cantidad,categoria)));
    }
    private static void crearOrden() {System.out.println(empresa.crearOrden(leerTexto("Código de orden: "),leerTexto("Documento del cliente: "),leerTexto("Observaciones generales: ")));}
    private static void agregarDetalle() {
        OrdenServicio orden=empresa.buscarOrden(leerTexto("Código de orden: ")).orElseThrow(()->new IllegalArgumentException("Orden no encontrada."));
        List<Equipo> equipos=empresa.getEquipos().stream().filter(e->e.getCliente().getDocumento().equalsIgnoreCase(orden.getCliente().getDocumento())).toList();
        if(equipos.isEmpty())throw new IllegalArgumentException("El cliente no tiene equipos registrados.");
        System.out.println("Equipos del cliente:");for(Equipo e:equipos)System.out.println(e);
        Equipo equipo=empresa.buscarEquipo(leerTexto("Código del equipo: ")).orElseThrow(()->new IllegalArgumentException("Equipo no encontrado."));
        Servicio servicio=empresa.buscarServicio(leerTexto("Código de servicio (SER-DIAG, SER-MANT, SER-REP): ")).orElseThrow(()->new IllegalArgumentException("Servicio no encontrado."));
        DetalleServicio detalle=new DetalleServicio(leerTexto("Código del detalle: "),equipo,servicio,leerTexto("Falla o necesidad reportada: "));
        if(servicio instanceof ReparacionCorrectiva)detalle.registrarHoras(leerDouble("Horas de mano de obra: "));
        orden.agregarDetalle(detalle); if(orden.getEstado()==EstadoOrden.ABIERTA)orden.actualizarEstado(EstadoOrden.EN_PROCESO);
        System.out.println("Detalle agregado correctamente.");
    }
    private static DetalleServicio buscarDetalle(String codigoOrden,String codigoDetalle){OrdenServicio o=empresa.buscarOrden(codigoOrden).orElseThrow(()->new IllegalArgumentException("Orden no encontrada."));return o.getDetalles().stream().filter(d->d.getCodigo().equalsIgnoreCase(codigoDetalle)).findFirst().orElseThrow(()->new IllegalArgumentException("Detalle no encontrado."));}
    private static void asignarTecnico(){DetalleServicio d=buscarDetalle(leerTexto("Código de orden: "),leerTexto("Código del detalle: "));Tecnico t=empresa.buscarTecnico(leerTexto("Código del técnico: ")).orElseThrow(()->new IllegalArgumentException("Técnico no encontrado."));d.asignarTecnico(t);System.out.println("Técnico asignado correctamente.");}
    private static void registrarRepuestoUtilizado(){DetalleServicio d=buscarDetalle(leerTexto("Código de orden: "),leerTexto("Código del detalle: "));Repuesto r=empresa.buscarRepuesto(leerTexto("Código del repuesto: ")).orElseThrow(()->new IllegalArgumentException("Repuesto no encontrado."));d.agregarRepuesto(r,leerEntero("Cantidad utilizada: "));System.out.println("Repuesto agregado. Existencias restantes: "+r.getCantidadDisponible());}
    private static void consultarValorDetalle(){DetalleServicio d=buscarDetalle(leerTexto("Código de orden: "),leerTexto("Código del detalle: "));System.out.println(d);}
    private static void consultarValorOrden(){OrdenServicio o=empresa.buscarOrden(leerTexto("Código de orden: ")).orElseThrow(()->new IllegalArgumentException("Orden no encontrada."));System.out.println(o);for(DetalleServicio d:o.getDetalles())System.out.println("  "+d);}
    private static void actualizarEstadoDetalle(){DetalleServicio d=buscarDetalle(leerTexto("Código de orden: "),leerTexto("Código del detalle: "));EstadoDetalle estado=leerEnum(EstadoDetalle.class,"Estado: 1 Pendiente, 2 En proceso, 3 Finalizado, 4 Cancelado: ");if(estado==EstadoDetalle.FINALIZADO&&d.getTecnico()==null)throw new IllegalArgumentException("Asigne un técnico antes de finalizar la intervención.");d.actualizarEstado(estado);System.out.println("Estado actualizado.");}
    private static void finalizarOrden(){OrdenServicio o=empresa.buscarOrden(leerTexto("Código de orden: ")).orElseThrow(()->new IllegalArgumentException("Orden no encontrada."));o.finalizar();System.out.println("Orden finalizada correctamente.");}
    private static void consultarOrdenesCliente(){String doc=leerTexto("Documento del cliente: ");List<OrdenServicio> lista=empresa.consultarOrdenesCliente(doc);if(lista.isEmpty())System.out.println("No hay órdenes para ese cliente.");for(OrdenServicio o:lista){System.out.println(o);for(DetalleServicio d:o.getDetalles())System.out.println("  "+d);}}
    private static void consultarTrabajosTecnico(){String codigo=leerTexto("Código del técnico: ");List<DetalleServicio> lista=empresa.consultarTrabajosTecnico(codigo);for(EstadoDetalle estado:EstadoDetalle.values()){System.out.println("-- "+estado+" --");for(DetalleServicio d:lista)if(d.getEstado()==estado)System.out.println(d);}if(lista.isEmpty())System.out.println("El técnico no tiene trabajos asignados.");}
    private static void consultarEquiposPendientes(){List<DetalleServicio> lista=empresa.consultarEquiposPendientes();if(lista.isEmpty())System.out.println("No hay intervenciones pendientes.");for(DetalleServicio d:lista)System.out.println(d);}
    private static void consultarInventarioCritico(){List<Repuesto> lista=empresa.consultarInventarioCritico();if(lista.isEmpty())System.out.println("No hay repuestos con menos de cinco unidades.");for(Repuesto r:lista)System.out.println(r);}
    private static String leerTexto(String mensaje){System.out.print(mensaje);String valor=scanner.nextLine().trim();if(valor.isEmpty())throw new IllegalArgumentException("Este campo no puede quedar vacío.");return valor;}
    private static int leerEntero(String mensaje){try{return Integer.parseInt(leerTexto(mensaje));}catch(NumberFormatException e){throw new IllegalArgumentException("Ingrese un número entero válido.");}}
    private static double leerDouble(String mensaje){try{return Double.parseDouble(leerTexto(mensaje).replace(',','.'));}catch(NumberFormatException e){throw new IllegalArgumentException("Ingrese un número válido.");}}
    private static boolean leerBooleano(String mensaje){String r=leerTexto(mensaje);if(r.equalsIgnoreCase("s"))return true;if(r.equalsIgnoreCase("n"))return false;throw new IllegalArgumentException("Responda s o n.");}
    private static <E extends Enum<E>> E leerEnum(Class<E> tipo,String mensaje){int opcion=leerEntero(mensaje);E[] valores=tipo.getEnumConstants();if(opcion<1||opcion>valores.length)throw new IllegalArgumentException("Opción no válida.");return valores[opcion-1];}
    private static void pausar(){System.out.println("\nPresione ENTER para volver al menú...");scanner.nextLine();}
}
