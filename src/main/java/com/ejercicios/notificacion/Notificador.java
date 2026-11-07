package com.ejercicios.notificacion;

import com.ejercicios.modelo.Pedido;

public interface Notificador {
    void enviarConfirmacion(Pedido pedido) throws NotificacionException;
}
