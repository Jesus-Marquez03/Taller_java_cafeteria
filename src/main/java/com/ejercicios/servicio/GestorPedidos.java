package com.ejercicios.servicio;

import com.ejercicios.descuento.PoliticaDescuento;
import com.ejercicios.modelo.Cliente;
import com.ejercicios.modelo.LineaPedido;
import com.ejercicios.modelo.Pedido;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class GestorPedidos {
    private final List<Pedido> pedidos = new ArrayList<>();
    private long siguienteId = 1;

    public Pedido registrar(Cliente cliente, List<LineaPedido> lineas,
                            PoliticaDescuento politicaDescuento) {
        Pedido pedido = new Pedido(siguienteId, cliente, lineas, politicaDescuento);
        pedidos.add(pedido);
        siguienteId++;
        return pedido;
    }

    public Optional<Pedido> buscarPorId(long id) {
        return pedidos.stream()
                .filter(pedido -> pedido.getId() == id)
                .findFirst();
    }

    public List<Pedido> listar() {
        return Collections.unmodifiableList(new ArrayList<>(pedidos));
    }
}
