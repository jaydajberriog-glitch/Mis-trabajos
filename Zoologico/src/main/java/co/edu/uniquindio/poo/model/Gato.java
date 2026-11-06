package co.edu.uniquindio.poo.model;


// subclase o la concreta o especializada
public class Gato extends  Animal{

    private boolean vacunado;

    public Gato(String codigo, byte edad, String color, String nombre,boolean vacunado){
        super(codigo,edad,color,nombre);// invocar el constructor de la clase padre
        this.vacunado = vacunado;
    }

    @Override
    public void comer() {
        System.out.println("El gato Toma leche");
    }

    @Override
    public void hacerSonido() {
        maullar();
    }

    @Override
    public void dormir() {
        System.out.println("El gato esta durmiendo ");
    }

    public void maullar(){
        System.out.println("El gato esta maullando");
    }
}
