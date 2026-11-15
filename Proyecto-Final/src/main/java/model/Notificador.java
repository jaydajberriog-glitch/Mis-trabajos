package model;

/** Canal de notificación. Cada implementación simula el envío con el formato propio de su canal. */
public interface Notificador {
    String getCanal();

    /** Simula el envío de la alerta al cliente y devuelve el texto de lo que se "envió". */
    String enviar(Cliente cliente, Alerta alerta);
}
