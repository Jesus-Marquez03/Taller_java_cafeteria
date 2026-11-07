package com.ejercicios.notificacion;

import com.ejercicios.modelo.Pedido;

public class NotificadorCorreo implements Notificador {
    private final boolean simularFallo;

    public NotificadorCorreo(boolean simularFallo) {
        this.simularFallo = simularFallo;
    }

    @Override
    public void enviarConfirmacion(Pedido pedido) throws NotificacionException {
        if (simularFallo) {
            throw new NotificacionException("No fue posible enviar el correo simulado.");
        }
        System.out.printf("Correo enviado a %s: su pedido #%d fue confirmado.%n",
                pedido.getCliente().getCorreo(), pedido.getId());
    }
}
