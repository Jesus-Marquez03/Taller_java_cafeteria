package com.ejercicios.pago;

public class PagoTransferencia implements MetodoPago {
    private final boolean simularAprobacion;

    public PagoTransferencia(boolean simularAprobacion) {
        this.simularAprobacion = simularAprobacion;
    }

    @Override
    public ResultadoPago procesar(long total) {
        if (total < 0) {
            throw new IllegalArgumentException("El total a pagar no puede ser negativo.");
        }
        if (simularAprobacion) {
            return ResultadoPago.aprobado(
                    "Transferencia aprobada por la simulación.", 0);
        }
        return ResultadoPago.rechazado(
                "Transferencia rechazada por la simulación.");
    }
}
