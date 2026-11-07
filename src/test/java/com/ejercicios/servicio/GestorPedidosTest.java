package com.ejercicios.servicio;

import com.ejercicios.descuento.SinDescuento;
import com.ejercicios.modelo.Cliente;
import com.ejercicios.modelo.LineaPedido;
import com.ejercicios.modelo.Pedido;
import com.ejercicios.modelo.Producto;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GestorPedidosTest {
    @Test
    void asignaIdentificadoresUnicosYPermiteConsultar() {
        GestorPedidos gestor = new GestorPedidos();
        Cliente cliente = new Cliente("Luis", "luis@correo.com");
        List<LineaPedido> lineas = List.of(
                new LineaPedido(new Producto("Té", 4_000), 1));

        Pedido primero = gestor.registrar(cliente, lineas, new SinDescuento());
        Pedido segundo = gestor.registrar(cliente, lineas, new SinDescuento());

        assertEquals(1, primero.getId());
        assertEquals(2, segundo.getId());
        assertTrue(gestor.buscarPorId(2).isPresent());
        assertEquals(2, gestor.listar().size());
    }
}
