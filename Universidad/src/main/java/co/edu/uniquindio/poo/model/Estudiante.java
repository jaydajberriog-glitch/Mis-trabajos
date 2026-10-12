package co.edu.uniquindio.poo.model;

import java.util.Arrays;

/**
 * Esta clase representa un curso de una universidad
 * @version 1.0
 * @author Robinson Arias Muñoz
 * @fecha : 22/09/26
 */
public class Estudiante {

    private String nombres;
    private String apellidos;
    private String identificacion;
    private byte edad;
    private String correo;
    private String telefono;

    private Curso ownedByCurso;
    private Nota[] listaNotas;


    public Estudiante(String nombres,String apellidos,String identificacion,
                      byte edad,String correo,String telefono,Curso ownedByCurso){
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.edad = edad;
        this.identificacion = identificacion;
        this.correo = correo;
        this.telefono = telefono;
        this.ownedByCurso = ownedByCurso;
        this.listaNotas = new Nota[5];
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getIdentificacion() {
        return identificacion;
    }

    public void setIdentificacion(String identificacion) {
        this.identificacion = identificacion;
    }

    public byte getEdad() {
        return edad;
    }

    public void setEdad(byte edad) {
        this.edad = edad;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public Curso getOwnedByCurso() {
        return ownedByCurso;
    }

    public void setOwnedByCurso(Curso ownedByCurso) {
        this.ownedByCurso = ownedByCurso;
    }

    public Nota[] getListaNotas() {
        return listaNotas;
    }

    public void setListaNotas(Nota[] listaNotas) {
        this.listaNotas = listaNotas;
    }

    @Override
    public String toString() {
        return "Estudiante{" +
                "nombres='" + nombres + '\'' +
                ", apellidos='" + apellidos + '\'' +
                ", identificacion='" + identificacion + '\'' +
                ", edad=" + edad +
                ", correo='" + correo + '\'' +
                ", telefono='" + telefono + '\'' +
                ", listaNotas=" + Arrays.toString(listaNotas) +
                '}';
    }

    public String registrarNota(String nombreNota, float valorNota) {
        Nota notaEncontrada = buscarNota(nombreNota);
        if(notaEncontrada != null){
            return "NO se puede registra la nota, ya existe";
        }else{
            int posicionDisponible = buscarPosicionDisponible();// esto restoran -1 para el caso que no exista una posicion disponible
            if(posicionDisponible == 200){
                return "Lo siento no se puede agregar mas notas ya tiene las 5 notas";
            }else{
                Nota nuevaNota = new Nota(nombreNota,valorNota);
                listaNotas[posicionDisponible] = nuevaNota;// listaEstdinates.add
                return "Nota regitrada exitosamente";
            }
        }
    }

    private int buscarPosicionDisponible() {

        for (int i = 0; i < listaNotas.length; i++) {
            if(listaNotas[i] == null){
                return i;
            }
        }
        return 200;// significa que no hay espacio
    }

    public Nota buscarNota(String nombreNota){
        for (Nota notaAux : listaNotas){
            if(notaAux != null && notaAux.getNombre().equals(nombreNota)){
                return notaAux;
            }
        }
        return null;
    }






}
