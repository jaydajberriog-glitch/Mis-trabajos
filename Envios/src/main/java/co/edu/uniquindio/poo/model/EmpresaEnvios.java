package co.edu.uniquindio.poo.model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class EmpresaEnvios {

    private String nombre;
    private List<Envio> listaEnvios = new LinkedList<>();

    public EmpresaEnvios(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public List<Envio> getListaEnvios() {
        return listaEnvios;
    }

    public void setListaEnvios(List<Envio> listaEnvios) {
        this.listaEnvios = listaEnvios;
    }

    // logica de negocio
    public String registrarEnvio(Envio envio){
        Optional<Envio> encontrado = buscarEnvio(envio.getCodigo());
        if(encontrado.isPresent()){
            return "El envio ya existe";
        }else {
            listaEnvios.add(envio);
            return "Registrado exitosamente";
        }
    }

    public Optional<Envio> buscarEnvio(String codigo) {
        return listaEnvios.
                stream().
                filter(e -> e.getCodigo().equals(codigo)).findFirst();
    }


    @Override
    public String toString() {
        return "EmpresaEnvios{" +
                "nombre='" + nombre + '\'' +
                '}';
    }

    public Envio masCostoso(){
        Envio envioMayor = null;



        return envioMayor;
    }


    public List<Envio> obtenerEnviosMayores(int pesoLimite){
        List<Envio> enviosMayores = new ArrayList<>();
        for(Envio envioAux : listaEnvios){
            if(envioAux.getPeso() > pesoLimite){
                enviosMayores.add(envioAux);
            }
        }
        return enviosMayores;
    }

    public List<Envio> obtenerEnviosMayoresDeclarativo(int pesoLimite){
        return listaEnvios.stream().filter(envio -> envio.getPeso() >= pesoLimite).toList();
    }



}
