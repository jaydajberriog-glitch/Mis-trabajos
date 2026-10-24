package util;

/*
Consola: Esta es mi libreria propia creada en 2026-1 en lógica de programación
*/

import java.util.Scanner;

public class Consola {

    // Un solo Scanner compartido por todas las funciones
    private static Scanner scanner = new Scanner(System.in);

    //Función para ingresar una cadena de texto (no acepta texto vacío)
    public static String ingresarTexto(String mensaje){
        String texto = "";
        while (texto.isEmpty()) {
            System.out.print(mensaje);
            texto = scanner.nextLine().trim();
            if (texto.isEmpty()) {
                System.out.println("No puede estar vacío.");
            }
        }
        return texto;
    }

    //Función para ingresar un entero (repite hasta que escriba un entero válido)
    public static int ingresarEntero(String mensaje){
        while (true) {
            System.out.print(mensaje);
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Debe escribir un número entero.");
            }
        }
    }

    //Función para ingresar un decimal (acepta 2.5 o 2,5)
    public static double ingresarDecimal(String mensaje){
        while (true) {
            System.out.print(mensaje);
            try {
                return Double.parseDouble(scanner.nextLine().trim().replace(",", "."));
            } catch (NumberFormatException e) {
                System.out.println("Debe escribir un número (ejemplo: 2.5).");
            }
        }
    }

    //Función para ingresar un booleano (acepta s/si/true y n/no/false)
    public static boolean ingresarBooleano(String mensaje){
        while (true) {
            System.out.print(mensaje);
            String respuesta = scanner.nextLine().trim().toLowerCase();
            if (respuesta.equals("s") || respuesta.equals("si") || respuesta.equals("true")) {
                return true;
            }
            if (respuesta.equals("n") || respuesta.equals("no") || respuesta.equals("false")) {
                return false;
            }
            System.out.println("Responda con s o n.");
        }
    }

}
