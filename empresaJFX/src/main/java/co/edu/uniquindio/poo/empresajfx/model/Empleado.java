package co.edu.uniquindio.poo.empresajfx.model;

public class Empleado {
    private String nombre;
    private String apellidos;
    private String identificacion;
    private String password;
    private String user;

    public Empleado(String nombre, String apellidos, String identificacion, String password, String user) {
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.identificacion = identificacion;
        this.password = password;
        this.user = user;
    }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellidos() { return apellidos; }
    public void setApellidos(String apellidos) { this.apellidos = apellidos; }

    public String getIdentificacion() { return identificacion; }
    public void setIdentificacion(String identificacion) { this.identificacion = identificacion; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getUser() { return user; }
    public void setUser(String user) { this.user = user; }
}
