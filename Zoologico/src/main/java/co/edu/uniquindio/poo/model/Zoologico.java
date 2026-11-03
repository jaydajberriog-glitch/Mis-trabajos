package co.edu.uniquindio.poo.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class Zoologico {

    private String nombre;
    private final String direccion;
    private List<Animal> listaAnimales = new ArrayList<>();



    public Zoologico(String nombre,String direccion){
        this.nombre = nombre;
        this.direccion = direccion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public List<Animal> getListaAnimales() {
        return Collections.unmodifiableList(listaAnimales);
    }

    public void setListaAnimales(List<Animal> listaAnimales) {
        this.listaAnimales = listaAnimales;
    }

    public String registrarAnimal(Animal animal){
        Optional<Animal> animalOptional = buscarAnimal(animal.getCodigo());
        if(animalOptional.isPresent()){
            return "Lo siento ese animal ya existe";
        }else{
            listaAnimales.add(animal);
            return "Animal registrado exitosamente een el zoologico";
        }
    }
    public Optional<Animal> buscarAnimal(String codigo){
        return listaAnimales
                .stream()
                .filter(a -> a.getCodigo().equals(codigo))
                .findFirst();
    }


    public void comerTodosAnimales() {
        for(Animal animal: listaAnimales){
            animal.comer();
        }
    }

    public void comerTodosGatos() {
        for(Animal animal: listaAnimales){// todos los animales
            if(animal instanceof Gato ){
                animal.comer();
            }
        }
    }

    public void maullarTodosGatos() {
        for(Animal animal: listaAnimales){// todos los animales
            if(animal instanceof Gato ){
                ((Gato)animal).maullar();// cast
            }
        }
    }
}
