package model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Cliente {

    private final String documentoIdentidad;
    private String nombreCompleto;
    private final Tienda ownedByTienda;
    private String telefono;
    private String correo;
    private String ciudadResidencia;
    private final List<Factura> listaFacturas;

    public Cliente(String documentoIdentidad, String nombreCompleto,
                   Tienda ownedByTienda, String telefono,
                   String correo, String ciudadResidencia) {

        this.documentoIdentidad = documentoIdentidad;
        this.nombreCompleto = nombreCompleto;
        this.ownedByTienda = ownedByTienda;
        this.telefono = telefono;
        this.correo = correo;
        this.ciudadResidencia = ciudadResidencia;
        this.listaFacturas = new ArrayList<>();
    }

    public String getDocumentoIdentidad() {
        return documentoIdentidad;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getCorreo() {
        return correo;
    }

    public String getCiudadResidencia() {
        return ciudadResidencia;
    }

    public List<Factura> getListaFacturas() {
        return Collections.unmodifiableList(listaFacturas);
    }

    public Tienda getOwnedByTienda() {
        return ownedByTienda;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public void setCiudadResidencia(String ciudadResidencia) {
        this.ciudadResidencia = ciudadResidencia;
    }

    public void agregarFactura(Factura factura) {
        if (factura != null && !listaFacturas.contains(factura)) {
            listaFacturas.add(factura);
        }
    }

    public void eliminarFactura(Factura factura) {
        listaFacturas.remove(factura);
    }

    public boolean isCompraEnFecha(LocalDate fechaConsulta) {
        for (Factura factura : listaFacturas) {
            if (factura.fecha().isEqual(fechaConsulta)) {
                return true;
            }
        }
        return false;
    }

    public boolean verificarNombreConR() {
        return nombreCompleto != null && nombreCompleto.startsWith("R");
    }
}