package com.ejercicios.modelo;

import com.ejercicios.descuento.DescuentoClienteFrecuente;
import com.ejercicios.descuento.DescuentoPorVolumen;
import com.ejercicios.descuento.SinDescuento;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PedidoTest {
    private final Cliente cliente = new Cliente("Ana", "ana@correo.com");

    @Test
    void registraPedidoCorrectamenteSinDescuento() {
        Pedido pedido = new Pedido(1, cliente,
                List.of(new LineaPedido(new Producto("Café", 5_000), 2)),
                new SinDescuento());

        assertEquals(10_000, pedido.getSubtotal());
        assertEquals(0, pedido.getDescuento());
        assertEquals(10_000, pedido.getTotal());
        assertEquals(EstadoPedido.PENDIENTE, pedido.getEstado());
    }

    @Test
    void rechazaPedidoVacio() {
        assertThrows(IllegalArgumentException.class,
                () -> new Pedido(1, cliente, List.of(), new SinDescuento()));
    }

    @Test
    void rechazaPrecioNegativo() {
        assertThrows(IllegalArgumentException.class,
                () -> new Producto("Café", -1));
    }

    @Test
    void rechazaCantidadCero() {
        Producto producto = new Producto("Café", 5_000);
        assertThrows(IllegalArgumentException.class,
                () -> new LineaPedido(producto, 0));
    }

    @Test
    void calculaDescuentoClienteFrecuenteDelDiezPorCiento() {
        Pedido pedido = crearPedido(10_005, 1, new DescuentoClienteFrecuente());

        assertEquals(1_001, pedido.getDescuento());
        assertEquals(9_004, pedido.getTotal());
    }

    @Test
    void aplicaDescuentoVolumenConExactamenteDiezUnidades() {
        Pedido pedido = crearPedido(4_000, 10, new DescuentoPorVolumen());

        assertEquals(6_000, pedido.getDescuento());
        assertEquals(34_000, pedido.getTotal());
    }

    @Test
    void noAplicaDescuentoVolumenConNueveUnidades() {
        Pedido pedido = crearPedido(4_000, 9, new DescuentoPorVolumen());

        assertEquals(0, pedido.getDescuento());
        assertEquals(36_000, pedido.getTotal());
    }

    private Pedido crearPedido(long precio, int cantidad,
                               com.ejercicios.descuento.PoliticaDescuento politica) {
        return new Pedido(1, cliente,
                List.of(new LineaPedido(new Producto("Producto", precio), cantidad)),
                politica);
    }
}
