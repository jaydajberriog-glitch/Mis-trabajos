package model;

public class NotificadorCorreo implements Notificador {
    @Override
    public String getCanal() { return "CORREO"; }

    @Override
    public String enviar(Cliente cliente, Alerta alerta) {
        if (cliente.getCorreo().isBlank())
            return "[CORREO] No enviado: " + cliente.getNombre() + " no tiene correo registrado.";
        return "[CORREO -> " + cliente.getCorreo() + "] Asunto: " + alerta.getTipo().getTitulo()
                + " | " + alerta.getMensaje();
    }
}
