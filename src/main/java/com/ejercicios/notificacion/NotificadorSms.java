package com.ejercicios.notificacion;

import com.ejercicios.modelo.Pedido;

public class NotificadorSms implements Notificador {
    private final boolean simularFallo;

    public NotificadorSms(boolean simularFallo) {
        this.simularFallo = simularFallo;
    }

    @Override
    public void enviarConfirmacion(Pedido pedido) throws NotificacionException {
        String telefono = pedido.getCliente().getTelefono();
        if (telefono == null) {
            throw new NotificacionException(
                    "El cliente no registró un teléfono para recibir SMS.");
        }
        if (simularFallo) {
            throw new NotificacionException("No fue posible enviar el SMS simulado.");
        }
        System.out.printf("SMS enviado a %s: su pedido #%d fue confirmado.%n",
                telefono, pedido.getId());
    }
}
