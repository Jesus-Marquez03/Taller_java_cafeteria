package com.ejercicios.descuento;

public class DescuentoFijo implements PoliticaDescuento {
    private static final long COMPRA_MINIMA = 40_000;
    private static final long VALOR_DESCUENTO = 5_000;

    @Override
    public long calcularDescuento(long subtotal, int totalUnidades) {
        if (subtotal < COMPRA_MINIMA) {
            return 0;
        }
        return Math.min(VALOR_DESCUENTO, subtotal);
    }

    @Override
    public String getNombre() {
        return "Descuento fijo de $5.000 desde $40.000";
    }
}
