package co.edu.uniquindio.poo.model;

import java.util.Objects;

//superclase la clase padre
public abstract class Animal {

    private String nombre;
    private final String codigo;
    private byte edad;
    private String color;


    public Animal(String codigo, byte edad, String color, String nombre) {
        this.codigo = codigo;
        this.edad = edad;
        this.color = color;
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCodigo() {
        return codigo;
    }

    public byte getEdad() {
        return edad;
    }

    public void setEdad(byte edad) {
        this.edad = edad;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    /**
     *  @Override sobreescribir el metodo   Object
     *  La clase  Object es la clase padre e todas las clases en java
     * @param o   the reference object with which to compare.
     * @return
     */
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Animal animal = (Animal) o;
        return Objects.equals(codigo, animal.codigo);
    }

    //comer, dormir, morir,beber,reproducir

    public abstract void comer();


    public void dormir(){
        System.out.println("El animal esta durmiendo");
    }

    public abstract void hacerSonido();

}
