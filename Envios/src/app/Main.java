package app;

import model.Envio;
import model.EnvioNacional;
import model.EnvioInternacional;
import model.EnvioExpres;
import util.Consola;

import java.util.ArrayList;

public class Main {

    static ArrayList<Envio> envios = new ArrayList<>();
    static int contadorEnvios = 0;

    public static void main(String[] args) {
        System.out.println("=== Bienvenido a la empresa de mensajeria ===");

        int opcion = 0;
        while (opcion != 4) {
            mostrarMenu();
            opcion = Consola.ingresarEntero("Seleccione una opcion: ");

            switch (opcion) {
                case 1:
                    registrarEnvio();
                    break;
                case 2:
                    verEnvios();
                    break;
                case 3:
                    cancelarEnvio();
                    break;
                case 4:
                    System.out.println("Gracias por usar el sistema. Hasta pronto!");
                    break;
                default:
                    System.out.println("Opcion no valida, intente de nuevo.");
            }
        }
    }

    static void mostrarMenu() {
        System.out.println();
        System.out.println("----- MENU PRINCIPAL -----");
        System.out.println("1. Registrar envio");
        System.out.println("2. Ver envios registrados");
        System.out.println("3. Cancelar envio");
        System.out.println("4. Salir");
    }

    static void registrarEnvio() {
        System.out.println();
        System.out.println("Tipo de envio:");
        System.out.println("1. Nacional");
        System.out.println("2. Internacional");
        System.out.println("3. Expres");
        int tipo = Consola.ingresarEntero("Seleccione el tipo: ");

        if (tipo < 1 || tipo > 3) {
            System.out.println("Tipo no valido. Envio cancelado.");
            return;
        }

        // Datos comunes a todos los envios
        String destinatario = Consola.ingresarTexto("Nombre del destinatario: ");
        double peso = leerPeso();
        contadorEnvios++;

        Envio nuevo = null;

        switch (tipo) {
            case 1: {
                String ciudad = Consola.ingresarTexto("Ciudad de destino: ");
                boolean domicilio = Consola.ingresarBooleano("Requiere entrega a domicilio? (s/n): ");
                nuevo = new EnvioNacional("N-" + contadorEnvios, destinatario, peso, ciudad, domicilio);
                break;
            }
            case 2: {
                String pais = Consola.ingresarTexto("Pais de destino: ");
                boolean seguro = Consola.ingresarBooleano("Necesita seguro? (s/n): ");
                nuevo = new EnvioInternacional("I-" + contadorEnvios, destinatario, peso, pais, seguro);
                break;
            }
            case 3: {
                String ciudad = Consola.ingresarTexto("Ciudad de destino: ");
                boolean domicilio = Consola.ingresarBooleano("Requiere entrega a domicilio? (s/n): ");
                int horas = Consola.ingresarEntero("Horas maximas para la entrega: ");
                nuevo = new EnvioExpres("E-" + contadorEnvios, destinatario, peso, ciudad, domicilio, horas);
                break;
            }
        }

        envios.add(nuevo);
        System.out.println();
        System.out.println("Envio registrado con exito!");
        System.out.println(nuevo);
        System.out.println("Costo: " + nuevo.calcularCosto());
    }

    static void verEnvios() {
        System.out.println();
        if (envios.isEmpty()) {
            System.out.println("Todavia no hay envios registrados.");
            return;
        }

        double total = 0;
        for (Envio e : envios) {
            System.out.println(e);
            System.out.println("Costo: " + e.calcularCosto());
            System.out.println();
            total = total + e.calcularCosto();
        }
        System.out.println("Total: " + total);
    }

    //Ejercicio añadido 1
    static void cancelarEnvio() {
        System.out.println();
        if (envios.isEmpty()) {
            System.out.println("No hay envios registrados.");
            return;
        }
        String codigo = Consola.ingresarTexto("Ingrese el codigo del envio: ");
        Envio encontrado = null;
        for (Envio e : envios) {
            if (e.getCodigo().equalsIgnoreCase(codigo)) {
                encontrado = e;
            }
        }
        if (encontrado != null) {
            System.out.println(encontrado);
            boolean confirmar = Consola.ingresarBooleano("Cancelar envio? (s/n): ");
            if (confirmar) {
                envios.remove(encontrado);
                System.out.println("Envio cancelado.");
            } else {
                System.out.println("Operacion cancelada.");
            }
        } else {
            System.out.println("No existe un envio con ese codigo.");
        }
    }

    //Ejercicio añadido 2
    static void buscarEnvio() {
        System.out.println();
        if (envios.isEmpty()) {
            System.out.println("No hay envios registrados.");
            return;
        }
        String codigo = Consola.ingresarTexto("Ingrese el código del envio: ");
        Envio encontrado = null;
        for (Envio e : envios) {
            if (e.getCodigo().equalsIgnoreCase(codigo)) {
                encontrado = e;
            }
        }
        if (encontrado != null) {
            System.out.println("Envio encontrado:");
            System.out.println(encontrado);
        } else {
            System.out.println("Envio no encontrado.");
        }
    }


        // El peso debe ser mayor que 0
    static double leerPeso() {
        double peso = Consola.ingresarDecimal("Peso en kg: ");
        while (peso <= 0) {
            System.out.println("El peso debe ser mayor que 0.");
            peso = Consola.ingresarDecimal("Peso en kg: ");
        }
        return peso;
    }
}
