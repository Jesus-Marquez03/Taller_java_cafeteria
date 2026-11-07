package com.ejercicios.descuento;

public interface PoliticaDescuento {
    long calcularDescuento(long subtotal, int totalUnidades);

    String getNombre();
}
