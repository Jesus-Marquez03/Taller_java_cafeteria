package com.ejercicios.servicio;

import com.ejercicios.pago.ResultadoPago;

public class ResultadoConfirmacion {
    private final ResultadoPago resultadoPago;
    private final boolean notificacionEnviada;
    private final String mensajeNotificacion;

    public ResultadoConfirmacion(ResultadoPago resultadoPago, boolean notificacionEnviada,
                                 String mensajeNotificacion) {
        this.resultadoPago = resultadoPago;
        this.notificacionEnviada = notificacionEnviada;
        this.mensajeNotificacion = mensajeNotificacion;
    }

    public ResultadoPago getResultadoPago() {
        return resultadoPago;
    }

    public boolean isNotificacionEnviada() {
        return notificacionEnviada;
    }

    public String getMensajeNotificacion() {
        return mensajeNotificacion;
    }
}
