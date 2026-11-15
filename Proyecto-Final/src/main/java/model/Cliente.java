package model;

import exceptions.OperacionInvalidaException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Cliente {
    private final String documento;
    private String nombre;
    private String telefono;
    private String correo;
    private final List<Monedero> monederos = new ArrayList<>();
    private final List<MovimientoPuntos> historialPuntos = new ArrayList<>();
    private int puntos;
    private BigDecimal descuentoBeneficio = BigDecimal.ZERO;
    private LocalDate sinCargosRetiroHasta;

    public Cliente(String documento, String nombre, String telefono, String correo) {
        if (documento == null || documento.isBlank() || nombre == null || nombre.isBlank())
            throw new OperacionInvalidaException("Documento y nombre son obligatorios.");
        this.documento = documento.trim();
        this.nombre = nombre.trim();
        this.telefono = telefono == null ? "" : telefono.trim();
        this.correo = correo == null ? "" : correo.trim();
    }

    public void agregarMonedero(Monedero monedero) { monederos.add(monedero); }

    public void sumarPuntos(int cantidad) { sumarPuntos(cantidad, "Ajuste manual", null); }

    public void sumarPuntos(int cantidad, String motivo, String idTransaccion) {
        if (cantidad < 0) throw new OperacionInvalidaException("Los puntos no pueden ser negativos.");
        puntos += cantidad;
        historialPuntos.add(new MovimientoPuntos(cantidad, motivo, idTransaccion));
    }

    public void restarPuntos(int cantidad) { restarPuntos(cantidad, "Ajuste manual", null); }

    public void restarPuntos(int cantidad, String motivo, String idTransaccion) {
        if (cantidad < 0 || puntos < cantidad) throw new OperacionInvalidaException("Puntos insuficientes.");
        puntos -= cantidad;
        historialPuntos.add(new MovimientoPuntos(-cantidad, motivo, idTransaccion));
    }

    /** Activa los efectos del beneficio canjeado (descuento de comisión y/o retiros sin cargo). */
    public void aplicarBeneficio(Beneficio beneficio, LocalDate hoy) {
        BigDecimal descuento = BigDecimal.valueOf(beneficio.getDescuentoComision());
        if (descuento.compareTo(descuentoBeneficio) > 0) descuentoBeneficio = descuento;
        if (beneficio.getDiasSinCargosRetiro() > 0) {
            LocalDate base = sinCargosRetiroHasta != null && sinCargosRetiroHasta.isAfter(hoy) ? sinCargosRetiroHasta : hoy;
            sinCargosRetiroHasta = base.plusDays(beneficio.getDiasSinCargosRetiro());
        }
    }

    /** Descuento total sobre la comisión de transferencias: rango + beneficio canjeado (máximo 100%). */
    public BigDecimal getDescuentoComisionTransferencia() {
        return getRango().getDescuentoTarifa().add(descuentoBeneficio).min(BigDecimal.ONE);
    }

    public boolean tieneRetirosSinCargo(LocalDate fecha) {
        return sinCargosRetiroHasta != null && !fecha.isAfter(sinCargosRetiroHasta);
    }

    public String getDocumento() { return documento; }
    public String getNombre() { return nombre; }
    public String getTelefono() { return telefono; }
    public String getCorreo() { return correo; }
    public List<Monedero> getMonederos() { return Collections.unmodifiableList(monederos); }
    public List<MovimientoPuntos> getHistorialPuntos() { return Collections.unmodifiableList(historialPuntos); }
    public int getPuntos() { return puntos; }
    public Rango getRango() { return Rango.desdePuntos(puntos); }
    public LocalDate getSinCargosRetiroHasta() { return sinCargosRetiroHasta; }

    public void setNombre(String nombre) {
        if (nombre == null || nombre.isBlank()) throw new OperacionInvalidaException("El nombre es obligatorio.");
        this.nombre = nombre.trim();
    }
    public void setTelefono(String telefono) { this.telefono = telefono == null ? "" : telefono.trim(); }
    public void setCorreo(String correo) { this.correo = correo == null ? "" : correo.trim(); }

    @Override
    public String toString() { return nombre + " (" + documento + ")"; }
}
