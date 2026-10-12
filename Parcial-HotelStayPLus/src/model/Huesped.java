package Parcial - Hotel SatyPlus.model;

import java.util.ArrayList;

public class Huesped {

    private String documentoIdentidad;
    private String nombreCompleto;
    private int edad;
    private String telefono;
    private String ciudadProcedencia;
    private ArrayList<Reserva> listaReservas;

    public Huesped(String documentoIdentidad, String nombreCompleto, int edad, String telefono, String ciudadProcedencia) {
        this.documentoIdentidad = documentoIdentidad;
        this.nombreCompleto = nombreCompleto;
        this.edad = edad;
        this.telefono = telefono;
        this.ciudadProcedencia = ciudadProcedencia;
        this.listaReservas = new ArrayList<>();
    }

    public void agregarReserva(Reserva reserva) {
        listaReservas.add(reserva);
    }

    public String getDocumentoIdentidad() {
        return documentoIdentidad;
    }

    public void setDocumentoIdentidad(String documentoIdentidad) {
        this.documentoIdentidad = documentoIdentidad;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCiudadProcedencia() {
        return ciudadProcedencia;
    }

    public void setCiudadProcedencia(String ciudadProcedencia) {
        this.ciudadProcedencia = ciudadProcedencia;
    }

    public ArrayList<Reserva> getListaReservas() {
        return listaReservas;
    }

    public void setListaReservas(ArrayList<Reserva> listaReservas) {
        this.listaReservas = listaReservas;
    }

    @Override
    public String toString() {
        return "Nombre: " + nombreCompleto + "\n" +
                "Documento: " + documentoIdentidad + "\n" +
                "Ciudad: " + ciudadProcedencia + "\n" +
                "Cantidad de Reservas: " + listaReservas.size();
    }
}