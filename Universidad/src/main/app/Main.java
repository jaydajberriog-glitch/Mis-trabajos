package co.edu.uniquindio.poo.app;


import co.edu.uniquindio.poo.model.Curso;
import co.edu.uniquindio.poo.model.Estudiante;

import javax.swing.*;

public class Main {

    static void main() {

        JOptionPane.showMessageDialog(null,"Bienvenidos al sistema de gestion academica");
        String nombreCurso = JOptionPane.showInputDialog(null,"Por favor ingresar el nombre del curso");
        String codigoCurso = JOptionPane.showInputDialog(null,"Por favor ingresar el codigo del curso");

        Curso curso = new Curso(nombreCurso,codigoCurso);

        //CRUD create, read, update,delete
        int opcion;

        do{
            opcion = Integer.valueOf(JOptionPane.showInputDialog(null,
                    "Por favor selecciones una opcion :\n ---Menu--\n"+
                     "1. Agregar un estudiante\n" +
                            " 2. Buscar un estudiante\n"+
                            " 3. Eliminar un estudiante\n"+
                            " 4. Actualizar un estudiante\n"+
                            " 5. Registrar Nota un estudiante\n"+
                    ""));

            switch (opcion){
                case 1:
                    crearEstudiante(curso);
                    break;
                case 2:
                    buscarEstudiante(curso);
                    break;
                case 3:
                    eliminarEstudiante(curso);
                    break;
                case 4:
                    actualizarEstudiante(curso);
                    break;
                case 5:
                    registrarNotaEstudiante(curso);
                    break;
                case 0:
                    JOptionPane.showMessageDialog(null,"Muchas gracias por usar nuestro sistema");
                    break;
                default:JOptionPane.showMessageDialog(null,"Opcion Invalida");

            }

        }while(opcion != 0);


    }

    private static void registrarNotaEstudiante(Curso curso) {

        //pasos 1. pedir la identificacion del estudiante
        //pedir el nombre de la nota
        //pedir el valor de la nota
        String identificacion = JOptionPane.showInputDialog(null,
                "Por favor ingresar la indentificaion del estudiante al cual le quiere registar la nota");
        String nombreNota = JOptionPane.showInputDialog(null,
                "Por favor ingresar el nombre de la nota");
        float valorNota = Float.parseFloat(JOptionPane.showInputDialog(null,
                "Por favor ingresar el valor de la nota"));
        String resultado = curso.registrarNotaEstudiante(identificacion,nombreNota,valorNota);
        JOptionPane.showMessageDialog(null,resultado);
    }

    private static void actualizarEstudiante(Curso curso) {

        String identificacion = JOptionPane.showInputDialog(null,
                "Por favor ingresar la indentificaion del estudiante que desea actualizar");

        String nombresNuevos = JOptionPane.showInputDialog(null,"Por favor ingresar los nombres del estudiante nuevo");
        String apellidosNuevos = JOptionPane.showInputDialog(null,"Por favor ingresar los apellidos del estudiante nuevo");
        String identificacionNueva = JOptionPane.showInputDialog(null,"Por favor ingresar la indentificaion del estudiante nuevo");
        String edadNueva = JOptionPane.showInputDialog(null,"Por favor ingresar la edad del estudiante nuevo");
        byte edadEstudianteNueva = Byte.valueOf(edadNueva);
        String correoNuevo = JOptionPane.showInputDialog(null,"Por favor ingresar el correo del estudiante nuevo");
        String telefonoNuevo = JOptionPane.showInputDialog(null,"Por favor ingresar el telefono del estudiante nuevo");


        boolean actualizado = curso.actualizarEstudiante(identificacion,identificacionNueva,nombresNuevos,apellidosNuevos,edadEstudianteNueva,
                correoNuevo,telefonoNuevo);

        if(actualizado){
            JOptionPane.showMessageDialog(null,"El estudiante con la identificacion "+identificacion+" fue actualizado");
        }else JOptionPane.showMessageDialog(null,"El estudiante con la identificacion "+identificacion+" no existe");

    }

    private static void eliminarEstudiante(Curso curso) {
        String identificacion = JOptionPane.showInputDialog(null,
                "Por favor ingresar la indentificaion del estudiante que desea eliminar");
        boolean eliminado = curso.eliminarEstudiante(identificacion);

        if(eliminado){
            JOptionPane.showMessageDialog(null,"El estudiante con la identificacion "+identificacion+" fue eliminado");
        }else JOptionPane.showMessageDialog(null,"El estudiante con la identificacion "+identificacion+" no existe");
    }

    private static void buscarEstudiante(Curso curso) {
        String identificacion = JOptionPane.showInputDialog(null,
                "Por favor ingresar la indentificaion del estudiante que desea buscar");

        Estudiante estudianteEcontrado = curso.buscarEstudiante(identificacion);

        if(estudianteEcontrado != null){
            JOptionPane.showMessageDialog(null,estudianteEcontrado.toString());
        }else{
            JOptionPane.showMessageDialog(null,"El estudiante con la "+identificacion+" no existe");
        }

    }

    private static void crearEstudiante(Curso curso) {

        String nombres = JOptionPane.showInputDialog(null,"Por favor ingresar los nombres del estudiante nuevo");
        String apellidos = JOptionPane.showInputDialog(null,"Por favor ingresar los apellidos del estudiante nuevo");
        String identificacion = JOptionPane.showInputDialog(null,"Por favor ingresar la indentificaion del estudiante nuevo");
        String edad = JOptionPane.showInputDialog(null,"Por favor ingresar la edad del estudiante nuevo");
        byte edadEstudiante = Byte.valueOf(edad);
        String correo = JOptionPane.showInputDialog(null,"Por favor ingresar el correo del estudiante nuevo");
        String telefono = JOptionPane.showInputDialog(null,"Por favor ingresar el telefono del estudiante nuevo");

        String resultado = curso.registrarEstudiante(nombres,apellidos,identificacion,edadEstudiante,correo,telefono);

        JOptionPane.showMessageDialog(null,resultado);


    }
}
