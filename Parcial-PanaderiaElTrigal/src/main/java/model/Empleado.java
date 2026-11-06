package model;

/**
 * Representa los datos comunes de cualquier empleado.
 */
public abstract class Empleado {

    private final String codigo;
    private final String nombre;

    public Empleado(String codigo, String nombre) {
        if (codigo == null || codigo.isBlank()) {
            throw new IllegalArgumentException("El código del empleado es obligatorio.");
        }
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre del empleado es obligatorio.");
        }

        this.codigo = codigo.trim();
        this.nombre = nombre.trim();
    }

    public String getCodigo() {
        return codigo;
    }

    public String getNombre() {
        return nombre;
    }
}
