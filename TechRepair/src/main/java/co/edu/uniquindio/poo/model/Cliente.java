package co.edu.uniquindio.poo.model;

public class Cliente {
    private String documento;
    private String nombreCompleto;
    private String telefono;
    private String correo;
    private String direccion;

    public Cliente(String documento, String nombreCompleto, String telefono, String correo, String direccion) {
        if (textoVacio(documento) || textoVacio(nombreCompleto) || textoVacio(telefono) || textoVacio(correo) || textoVacio(direccion))
            throw new IllegalArgumentException("Todos los datos del cliente son obligatorios.");
        this.documento = documento.trim(); this.nombreCompleto = nombreCompleto.trim();
        this.telefono = telefono.trim(); this.correo = correo.trim(); this.direccion = direccion.trim();
    }
    private boolean textoVacio(String valor) { return valor == null || valor.trim().isEmpty(); }
    public String getDocumento() { return documento; }
    public String getNombreCompleto() { return nombreCompleto; }
    public String getTelefono() { return telefono; }
    public String getCorreo() { return correo; }
    public String getDireccion() { return direccion; }
    public void setNombreCompleto(String nombreCompleto) { if (textoVacio(nombreCompleto)) throw new IllegalArgumentException("Nombre obligatorio."); this.nombreCompleto = nombreCompleto.trim(); }
    @Override public String toString() { return documento + " - " + nombreCompleto + " | Tel: " + telefono + " | Correo: " + correo; }
}
