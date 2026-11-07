package com.ejercicios.pago;

public class ResultadoPago {
    private final boolean aprobado;
    private final String mensaje;
    private final long cambio;

    private ResultadoPago(boolean aprobado, String mensaje, long cambio) {
        this.aprobado = aprobado;
        this.mensaje = mensaje;
        this.cambio = cambio;
    }

    public static ResultadoPago aprobado(String mensaje, long cambio) {
        if (cambio < 0) {
            throw new IllegalArgumentException("El cambio no puede ser negativo.");
        }
        return new ResultadoPago(true, mensaje, cambio);
    }

    public static ResultadoPago rechazado(String mensaje) {
        return new ResultadoPago(false, mensaje, 0);
    }

    public boolean isAprobado() {
        return aprobado;
    }

    public String getMensaje() {
        return mensaje;
    }

    public long getCambio() {
        return cambio;
    }
}
