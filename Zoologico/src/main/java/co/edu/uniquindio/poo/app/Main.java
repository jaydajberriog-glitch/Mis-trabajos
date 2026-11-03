package co.edu.uniquindio.poo.app;

import co.edu.uniquindio.poo.model.Animal;
import co.edu.uniquindio.poo.model.Gato;
import co.edu.uniquindio.poo.model.Perro;
import co.edu.uniquindio.poo.model.Zoologico;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    static void main() {

        Zoologico zoologico = new Zoologico("Ukumari","perira via cartago");

        Animal gato1 = new Gato("001",(byte)3, "Gris","Michi",false);
        zoologico.registrarAnimal(gato1);

        Animal animal1 = new Perro("002",(byte)3,"Gris","Michi","Pit");
        zoologico.registrarAnimal(animal1);

        Gato g1 = (Gato)gato1;


        zoologico.comerTodosAnimales();



        //equals() el equals es el metodo para comparar objetos

        if(gato1.equals(gato1)){
            System.out.println("Son iguales");
        }else{
            System.out.println("Son distintos");
        }

        gato1.comer();
        gato1.hacerSonido();
        gato1.dormir();




    }
}
