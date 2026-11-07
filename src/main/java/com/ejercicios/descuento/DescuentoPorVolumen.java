package com.ejercicios.descuento;

public class DescuentoPorVolumen implements PoliticaDescuento {
    private static final int UNIDADES_MINIMAS = 10;
    private static final int PORCENTAJE = 15;

    @Override
    public long calcularDescuento(long subtotal, int totalUnidades) {
        if (totalUnidades < UNIDADES_MINIMAS) {
            return 0;
        }
        // Se redondea al peso más cercano; los valores de medio peso se redondean hacia arriba.
        return Math.addExact(Math.multiplyExact(subtotal, PORCENTAJE), 50) / 100;
    }

    @Override
    public String getNombre() {
        return "Compra por volumen (15 % desde 10 unidades)";
    }
}
