package com.ejercicios.modelo;

public class LineaPedido {
    private final Producto producto;
    private final int cantidad;

    public LineaPedido(Producto producto, int cantidad) {
        if (producto == null) {
            throw new IllegalArgumentException("El producto es obligatorio.");
        }
        if (cantidad < 1) {
            throw new IllegalArgumentException("La cantidad debe ser al menos uno.");
        }
        this.producto = producto;
        this.cantidad = cantidad;
    }

    public long calcularSubtotal() {
        try {
            return Math.multiplyExact(producto.getPrecioUnitario(), cantidad);
        } catch (ArithmeticException excepcion) {
            throw new IllegalArgumentException("El subtotal del producto es demasiado grande.");
        }
    }

    public Producto getProducto() {
        return producto;
    }

    public int getCantidad() {
        return cantidad;
    }
}
