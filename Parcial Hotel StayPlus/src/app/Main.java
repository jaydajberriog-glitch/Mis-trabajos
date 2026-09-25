package app;

import model.Habitacion;
import model.Hotel;
import model.Huesped;
import model.Reserva;

import javax.swing.JOptionPane;


public class Main {

    public static void main(String[] args) {

        Hotel hotel = new Hotel("Hotel StayPlus", "900123456-7", "Calle 45 # 12-30", "3104567890");

        cargarDatosDemo(hotel);

        int opcion = -1;

        do {
            String menu = "=== BIENVENIDO AL HOTEL STAYPLUS ===\n" +
                    "1. Consultar huésped por teléfono\n" +
                    "2. Control de disponibilidad de habitaciones\n" +
                    "3. Matriz de ocupación semanal\n" +
                    "4. Reservas especiales (código capicúa)\n" +
                    "5. Ingresos del hotel por fecha\n" +
                    "0. Salir\n\n" +
                    "Seleccione una opción:";

            String entrada = JOptionPane.showInputDialog(null, menu);

            if (entrada == null) {
                break;
            }

            try {
                opcion = Integer.parseInt(entrada);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Por favor ingrese un número válido.");
                continue;
            }

            switch (opcion) {
                case 1:
                    String tel = JOptionPane.showInputDialog(null, "Ingrese el número de teléfono del huésped:");
                    if (tel != null && !tel.trim().isEmpty()) {
                        String resultado = hotel.consultarHuespedPorTelefono(tel.trim());
                        JOptionPane.showMessageDialog(null, resultado);
                    }
                    break;

                case 2:
                    JOptionPane.showMessageDialog(null, hotel.consultarControlDisponibilidad());
                    break;

                case 3:
                    JOptionPane.showMessageDialog(null, hotel.consultarOcupacionSemanal());
                    break;

                case 4:
                    JOptionPane.showMessageDialog(null, hotel.consultarReservasEspeciales());
                    break;

                case 5:
                    String fecha = JOptionPane.showInputDialog(null, "Ingrese la fecha a consultar (DD/MM/AAAA):");
                    if (fecha != null && !fecha.trim().isEmpty()) {
                        String resultado = hotel.consultarIngresosPorFecha(fecha.trim());
                        JOptionPane.showMessageDialog(null, resultado);
                    }
                    break;

                case 0:
                    JOptionPane.showMessageDialog(null, "Muchas gracias por usar el sistema del Hotel StayPlus.");
                    break;

                default:
                    JOptionPane.showMessageDialog(null, "Opción no válida.");
            }

        } while (opcion != 0);
    }

    private static void cargarDatosDemo(Hotel hotel) {
        // Registro de Huéspedes
        Huesped h1 = new Huesped("1092459671", "Jayda Jely Berrio García", 17, "3245272130", "Armenía");
        Huesped h2 = new Huesped("1126905438", "Samuel Jesus Ramirez Fajardo", 41, "3205531482", "Cali");
        hotel.registrarHuesped(h1);
        hotel.registrarHuesped(h2);

        // Registro de Habitaciones
        Habitacion hab101 = new Habitacion(101, "Individual", 1, 1, 120000, "Disponible");
        Habitacion hab102 = new Habitacion(102, "Doble", 1, 2, 180000, "Ocupada");
        Habitacion hab201 = new Habitacion(201, "Suite", 2, 4, 350000, "Disponible");
        Habitacion hab202 = new Habitacion(202, "Doble", 2, 2, 175000, "Mantenimiento");

        hotel.registrarHabitacion(hab101);
        hotel.registrarHabitacion(hab102);
        hotel.registrarHabitacion(hab201);
        hotel.registrarHabitacion(hab202);

        // Inicializar Matriz de Ocupación
        hotel.inicializarMatrizOcupacion();
        // Cargar ocupaciones de ejemplo en la matriz ('O' = Ocupada, 'D' = Disponible)
        hotel.cambiarEstadoMatriz(0, 0, "O"); // Hab 101 Lunes
        hotel.cambiarEstadoMatriz(0, 1, "O"); // Hab 101 Martes
        hotel.cambiarEstadoMatriz(1, 1, "O"); // Hab 102 Martes
        hotel.cambiarEstadoMatriz(1, 2, "O"); // Hab 102 Miércoles
        hotel.cambiarEstadoMatriz(2, 5, "O"); // Hab 201 Sábado
        hotel.cambiarEstadoMatriz(2, 6, "O"); // Hab 201 Domingo

        // Reservas (1221 y 909 son capicúa)
        Reserva r1 = new Reserva(1221, "22/09/2026", 3, 2, "Confirmada", "Tarjeta", h1);
        r1.agregarHabitacion(hab101);
        r1.agregarHabitacion(hab102);
        hotel.registrarReserva(r1);

        Reserva r2 = new Reserva(4567, "23/09/2026", 2, 4, "Pendiente", "Efectivo", h2);
        r2.agregarHabitacion(hab201);
        hotel.registrarReserva(r2);

        Reserva r3 = new Reserva(909, "24/09/2026", 1, 1, "Finalizada", "Transferencia", h1);
        hotel.registrarReserva(r3);
    }
}