package model;

import java.util.ArrayList;

public class Hotel {

    private String nombreComercial;
    private String nit;
    private String direccion;
    private String telefono;

    private ArrayList<Habitacion> listaHabitaciones;
    private ArrayList<Huesped> listaHuespedes;
    private ArrayList<Reserva> listaReservas;

    // Matriz de ocupación (Filas = Habitaciones, Columnas = 7 Días)
    private String[][] matrizOcupacion;
    private String[] diasSemana = {"Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo"};

    public Hotel(String nombreComercial, String nit, String direccion, String telefono) {
        this.nombreComercial = nombreComercial;
        this.nit = nit;
        this.direccion = direccion;
        this.telefono = telefono;
        this.listaHabitaciones = new ArrayList<>();
        this.listaHuespedes = new ArrayList<>();
        this.listaReservas = new ArrayList<>();
    }

    public void registrarHabitacion(Habitacion habitacion) {
        listaHabitaciones.add(habitacion);
    }

    public void registrarHuesped(Huesped huesped) {
        listaHuespedes.add(huesped);
    }

    public void registrarReserva(Reserva reserva) {
        listaReservas.add(reserva);
    }

    public void inicializarMatrizOcupacion() {
        int filas = listaHabitaciones.size();
        matrizOcupacion = new String[filas][7];
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < 7; j++) {
                matrizOcupacion[i][j] = "D";
            }
        }
    }

    public void cambiarEstadoMatriz(int posHabitacion, int posDia, String estado) {
        if (matrizOcupacion != null && posHabitacion < matrizOcupacion.length && posDia < 7) {
            matrizOcupacion[posHabitacion][posDia] = estado;
        }
    }

    // 1. Consultar huésped por teléfono
    public String consultarHuespedPorTelefono(String telefono) {
        for (Huesped h : listaHuespedes) {
            if (h.getTelefono().equals(telefono)) {
                String info = "--- DATOS DEL HUÉSPED ---\n" +
                        "Nombre: " + h.getNombreCompleto() + "\n" +
                        "Documento: " + h.getDocumentoIdentidad() + "\n" +
                        "Ciudad: " + h.getCiudadProcedencia() + "\n\n" +
                        "--- RESERVAS REALIZADAS ---\n";

                if (h.getListaReservas().isEmpty()) {
                    info += "No tiene reservas registradas.";
                } else {
                    for (Reserva r : h.getListaReservas()) {
                        info += r.toString() + "\n";
                    }
                }
                return info;
            }
        }
        return "No se encontró ningún huésped con el teléfono: " + telefono;
    }

    //2. Control de disponibilidad de habitaciones
    public String consultarControlDisponibilidad() {
        int disponibles = 0;
        int ocupadas = 0;
        int mantenimiento = 0;

        Habitacion masCara = null;
        Habitacion masEconomica = null;

        for (Habitacion h : listaHabitaciones) {
            if (h.getEstado().equalsIgnoreCase("Disponible")) {
                disponibles++;
            } else if (h.getEstado().equalsIgnoreCase("Ocupada")) {
                ocupadas++;
            } else if (h.getEstado().equalsIgnoreCase("Mantenimiento")) {
                mantenimiento++;
            }

            if (masCara == null || h.getPrecioPorNoche() > masCara.getPrecioPorNoche()) {
                masCara = h;
            }
            if (masEconomica == null || h.getPrecioPorNoche() < masEconomica.getPrecioPorNoche()) {
                masEconomica = h;
            }
        }

        String mensaje = "--- CONTROL DE DISPONIBILIDAD ---\n" +
                "Habitaciones Disponibles: " + disponibles + "\n" +
                "Habitaciones Ocupadas: " + ocupadas + "\n" +
                "Habitaciones en Mantenimiento: " + mantenimiento + "\n\n";

        if (masCara != null) {
            mensaje += "Habitación más cara: Hab " + masCara.getNumero() + " ($" + masCara.getPrecioPorNoche() + ")\n";
            mensaje += "Habitación más económica: Hab " + masEconomica.getNumero() + " ($" + masEconomica.getPrecioPorNoche() + ")";
        }

        return mensaje;
    }

    // 3. Matriz de ocupación semanal
    public String consultarOcupacionSemanal() {
        if (matrizOcupacion == null) {
            return "La matriz de ocupación no ha sido inicializada.";
        }

        int[] ocupadasPorDia = new int[7];
        int totalSemana = 0;

        String tabla = "--- MATRIZ DE OCUPACIÓN SEMANAL ---\n";
        tabla += "Hab | Lun | Mar | Mié | Jue | Vie | Sáb | Dom |\n";

        for (int i = 0; i < listaHabitaciones.size(); i++) {
            tabla += listaHabitaciones.get(i).getNumero() + " | ";
            for (int j = 0; j < 7; j++) {
                String estado = matrizOcupacion[i][j];
                tabla += " " + estado + "   | ";
                if (estado.equalsIgnoreCase("O")) {
                    ocupadasPorDia[j]++;
                    totalSemana++;
                }
            }
            tabla += "\n";
        }

        int max = -1;
        int min = Integer.MAX_VALUE;
        String diaMayor = "";
        String diaMenor = "";

        for (int j = 0; j < 7; j++) {
            if (ocupadasPorDia[j] > max) {
                max = ocupadasPorDia[j];
                diaMayor = diasSemana[j];
            }
            if (ocupadasPorDia[j] < min) {
                min = ocupadasPorDia[j];
                diaMenor = diasSemana[j];
            }
        }

        tabla += "\n--- ANÁLISIS DE OCUPACIÓN ---\n";
        tabla += "Día con mayor ocupación: " + diaMayor + " (" + max + " habitaciones)\n";
        tabla += "Día con menor ocupación: " + diaMenor + " (" + min + " habitaciones)\n";
        tabla += "Total de ocupaciones en la semana: " + totalSemana;

        return tabla;
    }

    // 4 Reservas especiales (código capicúa)
    public String consultarReservasEspeciales() {
        String mensaje = "--- RESERVAS ESPECIALES (CÓDIGO CAPICÚA) ---\n";
        boolean hayEspeciales = false;

        for (Reserva r : listaReservas) {
            if (r.esEspecial()) {
                mensaje += r.toString() + "\n";
                hayEspeciales = true;
            }
        }

        if (!hayEspeciales) {
            mensaje += "No se encontraron reservas con código capicúa.";
        }

        return mensaje;
    }

    // 5. Ingresos del hotel por fecha
    public String consultarIngresosPorFecha(String fecha) {
        double totalIngresos = 0;
        int cantidadReservas = 0;

        for (Reserva r : listaReservas) {
            if (r.getFecha().equalsIgnoreCase(fecha)) {
                totalIngresos += r.getValorTotal();
                cantidadReservas++;
            }
        }

        return "--- INGRESOS DEL DÍA (" + fecha + ") ---\n" +
                "Reservas encontradas: " + cantidadReservas + "\n" +
                "Ingreso total generado: $" + totalIngresos;
    }

    public String getNombreComercial() {
        return nombreComercial;
    }

    public String getNit() {
        return nit;
    }

    public String getDireccion() {
        return direccion;
    }

    public String getTelefono() {
        return telefono;
    }
}