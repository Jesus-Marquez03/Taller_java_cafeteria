package com.ejercicios.pago;

public class PagoTarjeta implements MetodoPago {
    private final boolean simularAprobacion;

    public PagoTarjeta(boolean simularAprobacion) {
        this.simularAprobacion = simularAprobacion;
    }

    @Override
    public ResultadoPago procesar(long total) {
        if (total < 0) {
            throw new IllegalArgumentException("El total a pagar no puede ser negativo.");
        }
        if (simularAprobacion) {
            return ResultadoPago.aprobado("Pago con tarjeta aprobado por la simulación.", 0);
        }
        return ResultadoPago.rechazado("Pago con tarjeta rechazado por la simulación.");
    }
}
