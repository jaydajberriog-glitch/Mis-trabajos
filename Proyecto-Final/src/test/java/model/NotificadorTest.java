package model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class NotificadorTest {
    private final Cliente cliente = new Cliente("1", "Ana", "3001112233", "ana@mail.com");
    private final Alerta alerta = new Alerta("Saldo bajo en M1", TipoAlerta.SALDO_BAJO, cliente);

    @Test
    void cadaCanalUsaSuPropioFormato() {
        Notificador correo = new NotificadorCorreo();
        Notificador whatsapp = new NotificadorWhatsApp();
        Notificador sms = new NotificadorSMS();
        assertTrue(correo.enviar(cliente, alerta).contains("ana@mail.com"));
        assertTrue(correo.enviar(cliente, alerta).contains("Asunto"));
        assertTrue(whatsapp.enviar(cliente, alerta).contains("3001112233"));
        assertTrue(sms.enviar(cliente, alerta).startsWith("[SMS"));
    }

    @Test
    void smsTrunca160Caracteres() {
        Alerta larga = new Alerta("x".repeat(300), TipoAlerta.SALDO_BAJO, cliente);
        String enviado = new NotificadorSMS().enviar(cliente, larga);
        assertTrue(enviado.endsWith("..."));
        assertEquals(160, enviado.substring(enviado.indexOf("] ") + 2).length());
    }

    @Test
    void sinDatoDeContactoNoSeEnvia() {
        Cliente sinDatos = new Cliente("2", "Luis", "", "");
        assertTrue(new NotificadorCorreo().enviar(sinDatos, alerta).contains("No enviado"));
        assertTrue(new NotificadorWhatsApp().enviar(sinDatos, alerta).contains("No enviado"));
        assertTrue(new NotificadorSMS().enviar(sinDatos, alerta).contains("No enviado"));
    }
}
