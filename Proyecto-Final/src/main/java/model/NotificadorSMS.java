package model;

public class NotificadorSMS implements Notificador {
    private static final int LIMITE = 160;

    @Override
    public String getCanal() { return "SMS"; }

    @Override
    public String enviar(Cliente cliente, Alerta alerta) {
        if (cliente.getTelefono().isBlank())
            return "[SMS] No enviado: " + cliente.getNombre() + " no tiene teléfono registrado.";
        String texto = alerta.getMensaje();
        if (texto.length() > LIMITE) texto = texto.substring(0, LIMITE - 3) + "...";
        return "[SMS -> " + cliente.getTelefono() + "] " + texto;
    }
}
