package com.ejercicios.descuento;

public class DescuentoClienteFrecuente implements PoliticaDescuento {
    private static final int PORCENTAJE = 10;

    @Override
    public long calcularDescuento(long subtotal, int totalUnidades) {
        return calcularPorcentajeRedondeado(subtotal, PORCENTAJE);
    }

    private long calcularPorcentajeRedondeado(long subtotal, int porcentaje) {
        // Se redondea al peso más cercano; los valores de medio peso se redondean hacia arriba.
        return Math.addExact(Math.multiplyExact(subtotal, porcentaje), 50) / 100;
    }

    @Override
    public String getNombre() {
        return "Cliente frecuente (10 %)";
    }
}
