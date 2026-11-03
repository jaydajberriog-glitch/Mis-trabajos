package model;

public abstract class Empleado {
    protected String nombre;
    protected String documento;
    protected int edad;
    protected float salarioBase;
    protected CategoriaEmpleado categoria;
    protected float descuentoSalud;
    protected float descuentoPension;

    public Empleado(String nombre, String documento, int edad,
                    float salarioBase, CategoriaEmpleado categoria,
                    float descuentoSalud, float descuentoPension) {
        if (nombre == null || nombre.isBlank()) throw new IllegalArgumentException("El nombre no puede estar vacío.");
        if (documento == null || documento.isBlank()) throw new IllegalArgumentException("El documento no puede estar vacío.");
        if (edad < 0) throw new IllegalArgumentException("La edad no puede ser negativa.");
        if (salarioBase < 0) throw new IllegalArgumentException("El salario base no puede ser negativo.");
        if (categoria == null) throw new IllegalArgumentException("Debes elegir una categoría.");
        if (descuentoSalud < 0 || descuentoSalud > 100 || descuentoPension < 0 || descuentoPension > 100)
            throw new IllegalArgumentException("Los descuentos deben estar entre 0 y 100.");
        if (descuentoSalud + descuentoPension > 100)
            throw new IllegalArgumentException("La suma de descuentos no puede superar el 100%.");
        this.nombre = nombre.trim();
        this.documento = documento.trim();
        this.edad = edad;
        this.salarioBase = salarioBase;
        this.categoria = categoria;
        this.descuentoSalud = descuentoSalud;
        this.descuentoPension = descuentoPension;
    }

    public abstract float calcularSalarioBruto();
    public abstract String obtenerTipoEmpleado();

    protected float calcularBonificacion() {
        return switch (categoria) {
            case JUNIOR -> salarioBase * 0.05f;
            case SEMI_SENIOR -> salarioBase * 0.10f;
            case SENIOR -> salarioBase * 0.15f;
        };
    }

    public float calcularDescuentos() {
        return calcularSalarioBruto() * (descuentoSalud + descuentoPension) / 100f;
    }

    public float calcularSalarioNeto() {
        return calcularSalarioBruto() - calcularDescuentos();
    }

    public String getDocumento() { return documento; }
    public String getNombre() { return nombre; }

    public ResumenPago crearResumenPago() {
        return new ResumenPago(documento, nombre, obtenerTipoEmpleado(), calcularSalarioBruto(),
                calcularDescuentos(), calcularSalarioNeto());
    }

    public void mostrarInformacion() {
        System.out.printf("Documento: %s | Nombre: %s | Edad: %d | Tipo: %s | Categoría: %s%n",
                documento, nombre, edad, obtenerTipoEmpleado(), categoria);
        System.out.printf("Salario bruto: $%.2f | Descuentos: $%.2f | Salario neto: $%.2f%n",
                calcularSalarioBruto(), calcularDescuentos(), calcularSalarioNeto());
    }
}
