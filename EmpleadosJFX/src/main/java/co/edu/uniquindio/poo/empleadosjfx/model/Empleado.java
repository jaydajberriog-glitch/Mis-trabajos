package co.edu.uniquindio.poo.empleadosjfx.model;

public abstract class Empleado {
    protected String id;
    protected String nombre;
    protected CategoriaEmpleado categoria;

    public Empleado(String id, String nombre, CategoriaEmpleado categoria) {
        this.id = id;
        this.nombre = nombre;
        this.categoria = categoria;
    }

    public abstract double calcularSalarioTotal();
    public abstract String getTipoEmpleado();

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public CategoriaEmpleado getCategoria() { return categoria; }
    public void setCategoria(CategoriaEmpleado categoria) { this.categoria = categoria; }
}
