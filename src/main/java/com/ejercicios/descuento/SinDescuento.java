package com.ejercicios.descuento;

public class SinDescuento implements PoliticaDescuento {
    @Override
    public long calcularDescuento(long subtotal, int totalUnidades) {
        return 0;
    }

    @Override
    public String getNombre() {
        return "Sin descuento";
    }
}
