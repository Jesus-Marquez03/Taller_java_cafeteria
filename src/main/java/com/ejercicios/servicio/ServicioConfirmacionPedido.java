package com.ejercicios.servicio;

import com.ejercicios.modelo.EstadoPedido;
import com.ejercicios.modelo.Pedido;
import com.ejercicios.notificacion.NotificacionException;
import com.ejercicios.notificacion.Notificador;
import com.ejercicios.pago.MetodoPago;
import com.ejercicios.pago.ResultadoPago;

public class ServicioConfirmacionPedido {
    private final Notificador notificador;

    public ServicioConfirmacionPedido(Notificador notificador) {
        if (notificador == null) {
            throw new IllegalArgumentException("El notificador es obligatorio.");
        }
        this.notificador = notificador;
    }

    public ResultadoConfirmacion intentarConfirmar(Pedido pedido, MetodoPago metodoPago) {
        if (pedido == null) {
            throw new IllegalArgumentException("El pedido es obligatorio.");
        }
        if (metodoPago == null) {
            throw new IllegalArgumentException("El método de pago es obligatorio.");
        }
        if (pedido.getEstado() == EstadoPedido.CONFIRMADO) {
            ResultadoPago noProcesado = ResultadoPago.rechazado(
                    "El pedido ya fue confirmado; no se realizó un nuevo cobro.");
            return new ResultadoConfirmacion(noProcesado, false,
                    "No se envió otra notificación.");
        }

        ResultadoPago resultadoPago = metodoPago.procesar(pedido.getTotal());
        if (!resultadoPago.isAprobado()) {
            return new ResultadoConfirmacion(resultadoPago, false,
                    "No se envió notificación porque el pago fue rechazado.");
        }

        pedido.confirmar();
        try {
            notificador.enviarConfirmacion(pedido);
            return new ResultadoConfirmacion(resultadoPago, true,
                    "Notificación de confirmación enviada.");
        } catch (NotificacionException excepcion) {
            return new ResultadoConfirmacion(resultadoPago, false,
                    "El pedido quedó confirmado, pero falló la notificación: "
                            + excepcion.getMessage());
        }
    }
}
