package com.ejercicios.pago;

public class PagoEfectivo implements MetodoPago {
    private final long dineroRecibido;

    public PagoEfectivo(long dineroRecibido) {
        if (dineroRecibido < 0) {
            throw new IllegalArgumentException("El dinero recibido no puede ser negativo.");
        }
        this.dineroRecibido = dineroRecibido;
    }

    @Override
    public ResultadoPago procesar(long total) {
        validarTotal(total);
        if (dineroRecibido < total) {
            return ResultadoPago.rechazado("Pago rechazado: el dinero recibido es insuficiente.");
        }
        return ResultadoPago.aprobado("Pago en efectivo aprobado.", dineroRecibido - total);
    }

    private void validarTotal(long total) {
        if (total < 0) {
            throw new IllegalArgumentException("El total a pagar no puede ser negativo.");
        }
    }
}
