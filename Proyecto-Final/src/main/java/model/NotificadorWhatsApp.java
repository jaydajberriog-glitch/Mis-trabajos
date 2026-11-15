package model;

public class NotificadorWhatsApp implements Notificador {
    @Override
    public String getCanal() { return "WHATSAPP"; }

    @Override
    public String enviar(Cliente cliente, Alerta alerta) {
        if (cliente.getTelefono().isBlank())
            return "[WHATSAPP] No enviado: " + cliente.getNombre() + " no tiene teléfono registrado.";
        return "[WHATSAPP -> " + cliente.getTelefono() + "] Hola " + cliente.getNombre() + ", " + alerta.getMensaje();
    }
}
