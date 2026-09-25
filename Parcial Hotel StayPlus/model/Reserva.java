package model;

import java.util.ArrayList;

public class Reserva {

    private int codigo;
    private String fecha;          // DD/MM/AAAA
    private int numeroNoches;
    private int cantidadHuespedes;
    private String estado;         // Pendiente, Confirmada, Finalizada
    private String metodoPago;     // Efectivo, Tarjeta, Transferencia
    private double valorTotal;

    private Huesped huesped;
    private ArrayList<Habitacion> listaHabitaciones;

    public Reserva(int codigo, String fecha, int numeroNoches, int cantidadHuespedes,
                   String estado, String metodoPago, Huesped huesped) {
        this.codigo = codigo;
        this.fecha = fecha;
        this.numeroNoches = numeroNoches;
        this.cantidadHuespedes = cantidadHuespedes;
        this.estado = estado;
        this.metodoPago = metodoPago;
        this.huesped = huesped;
        this.listaHabitaciones = new ArrayList<>();
        this.valorTotal = 0;

        if (huesped != null) {
            huesped.agregarReserva(this);
        }
    }

    public void agregarHabitacion(Habitacion habitacion) {
        listaHabitaciones.add(habitacion);
        if (this.estado.equalsIgnoreCase("Confirmada")) {
            habitacion.setEstado("Ocupada");
        }
        calcularValorTotal();
    }

    public double calcularValorTotal() {
        double sumaPrecios = 0;
        for (Habitacion h : listaHabitaciones) {
            sumaPrecios += h.getPrecioPorNoche();
        }
        this.valorTotal = sumaPrecios * numeroNoches;
        return valorTotal;
    }

    // Requisito 4: Número capicúa
    public boolean esEspecial() {
        int original = codigo;
        int invertido = 0;
        int aux = codigo;

        while (aux > 0) {
            invertido = (invertido * 10) + (aux % 10);
            aux = aux / 10;
        }
        return original == invertido;
    }

    public int getCodigo() {
        return codigo;
    }

    public String getFecha() {
        return fecha;
    }

    public int getNumeroNoches() {
        return numeroNoches;
    }

    public int getCantidadHuespedes() {
        return cantidadHuespedes;
    }

    public String getEstado() {
        return estado;
    }

    public String getMetodoPago() {
        return metodoPago;
    }

    public double getValorTotal() {
        return valorTotal;
    }

    public Huesped getHuesped() {
        return huesped;
    }

    public ArrayList<Habitacion> getListaHabitaciones() {
        return listaHabitaciones;
    }

    @Override
    public String toString() {
        return "Reserva #" + codigo + " | Fecha: " + fecha + " | Noches: " + numeroNoches +
                " | Total: $" + valorTotal + " | Estado: " + estado;
    }
}