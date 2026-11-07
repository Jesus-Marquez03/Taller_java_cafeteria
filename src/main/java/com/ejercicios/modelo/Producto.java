package com.ejercicios.modelo;

public class Producto {
    private final String nombre;
    private final long precioUnitario;

    public Producto(String nombre, long precioUnitario) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre del producto es obligatorio.");
        }
        if (precioUnitario < 0) {
            throw new IllegalArgumentException("El precio del producto no puede ser negativo.");
        }
        this.nombre = nombre.trim();
        this.precioUnitario = precioUnitario;
    }

    public String getNombre() {
        return nombre;
    }

    public long getPrecioUnitario() {
        return precioUnitario;
    }
}
