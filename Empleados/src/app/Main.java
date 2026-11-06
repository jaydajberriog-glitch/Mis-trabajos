package app;

import model.*;
import java.util.Scanner;

public class Main {
    private static final Scanner sc = new Scanner(System.in);
    private static final Empresa empresa = new Empresa();

    public static void main(String[] args) {
        int opcion;
        do {
            System.out.println("\n===== SISTEMA DE NÓMINA =====");
            System.out.println("1. Registrar empleado de planta");
            System.out.println("2. Registrar empleado de ventas");
            System.out.println("3. Registrar empleado temporal");
            System.out.println("4. Listar empleados");
            System.out.println("5. Buscar empleado por documento");
            System.out.println("6. Empleado con mayor salario neto");
            System.out.println("7. Calcular nómina neta total");
            System.out.println("8. Mostrar resúmenes de pago");
            System.out.println("0. Salir");
            opcion = leerInt("Elige una opción: ");
            try {
                switch (opcion) {
                    case 1 -> registrarPlanta();
                    case 2 -> registrarVentas();
                    case 3 -> registrarTemporal();
                    case 4 -> listar();
                    case 5 -> buscar();
                    case 6 -> mayorSalario();
                    case 7 -> System.out.printf("Nómina neta total: $%.2f%n", empresa.calcularNominaNeta());
                    case 8 -> empresa.mostrarResumenes();
                    case 0 -> System.out.println("¡Hasta luego!");
                    default -> System.out.println("Opción no válida.");
                }
            } catch (IllegalArgumentException e) {
                System.out.println("No se pudo completar la operación: " + e.getMessage());
            }
        } while (opcion != 0);
    }

    private static EmpleadoDatos datosComunes() {
        String nombre = leerTexto("Nombre: ");
        String documento = leerTexto("Documento: ");
        int edad = leerInt("Edad: ");
        float base = leerFloat("Salario base: ");
        CategoriaEmpleado categoria = leerCategoria();
        float salud = leerFloat("Descuento de salud (%): ");
        float pension = leerFloat("Descuento de pensión (%): ");
        return new EmpleadoDatos(nombre, documento, edad, base, categoria, salud, pension);
    }

    private static void registrarPlanta() {
        EmpleadoDatos d = datosComunes();
        String cargo = leerTexto("Cargo: ");
        int horas = leerInt("Horas extra: ");
        float valorHora = leerFloat("Valor por hora extra: ");
        float auxilio = leerFloat("Auxilio de transporte: ");
        empresa.agregarEmpleado(new EmpleadoPlanta(d.nombre, d.documento, d.edad, d.base, d.categoria,
                d.salud, d.pension, cargo, horas, valorHora, auxilio));
        System.out.println("Empleado de planta registrado.");
    }

    private static void registrarVentas() {
        EmpleadoDatos d = datosComunes();
        float ventas = leerFloat("Total de ventas: ");
        float comision = leerFloat("Porcentaje de comisión (%): ");
        empresa.agregarEmpleado(new EmpleadoVentas(d.nombre, d.documento, d.edad, d.base, d.categoria,
                d.salud, d.pension, ventas, comision));
        System.out.println("Empleado de ventas registrado.");
    }

    private static void registrarTemporal() {
        EmpleadoDatos d = datosComunes();
        int dias = leerInt("Días trabajados: ");
        float valorDia = leerFloat("Valor por día: ");
        empresa.agregarEmpleado(new EmpleadoTemporal(d.nombre, d.documento, d.edad, d.base, d.categoria,
                d.salud, d.pension, dias, valorDia));
        System.out.println("Empleado temporal registrado.");
    }

    private static void listar() {
        if (empresa.listarEmpleados().isEmpty()) { System.out.println("No hay empleados registrados."); return; }
        for (Empleado e : empresa.listarEmpleados()) e.mostrarInformacion();
    }

    private static void buscar() {
        Empleado e = empresa.buscarPorDocumento(leerTexto("Documento a buscar: "));
        if (e == null) System.out.println("No se encontró un empleado con ese documento.");
        else e.mostrarInformacion();
    }

    private static void mayorSalario() {
        Empleado e = empresa.empleadoConMayorSalarioNeto();
        if (e == null) System.out.println("No hay empleados registrados.");
        else { System.out.println("Empleado con mayor salario neto:"); e.mostrarInformacion(); }
    }

    private static String leerTexto(String mensaje) {
        System.out.print(mensaje); return sc.nextLine().trim();
    }
    private static int leerInt(String mensaje) {
        while (true) try { return Integer.parseInt(leerTexto(mensaje)); }
        catch (NumberFormatException e) { System.out.println("Escribe un número entero válido."); }
    }
    private static float leerFloat(String mensaje) {
        while (true) try { return Float.parseFloat(leerTexto(mensaje).replace(',', '.')); }
        catch (NumberFormatException e) { System.out.println("Escribe un número válido."); }
    }
    private static CategoriaEmpleado leerCategoria() {
        while (true) {
            System.out.println("Categoría: 1. JUNIOR  2. SEMI_SENIOR  3. SENIOR");
            switch (leerInt("Elige categoría: ")) {
                case 1: return CategoriaEmpleado.JUNIOR;
                case 2: return CategoriaEmpleado.SEMI_SENIOR;
                case 3: return CategoriaEmpleado.SENIOR;
                default: System.out.println("Elige 1, 2 o 3.");
            }
        }
    }

    private record EmpleadoDatos(String nombre, String documento, int edad, float base,
                                 CategoriaEmpleado categoria, float salud, float pension) {}
}
