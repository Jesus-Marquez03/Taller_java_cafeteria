package com.ejercicios.modelo;

import com.ejercicios.descuento.PoliticaDescuento;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Pedido {
    private final long id;
    private final Cliente cliente;
    private final List<LineaPedido> lineas;
    private final String nombrePoliticaDescuento;
    private final long subtotal;
    private final long descuento;
    private final long total;
    private EstadoPedido estado;

    public Pedido(long id, Cliente cliente, List<LineaPedido> lineas,
                  PoliticaDescuento politicaDescuento) {
        if (id < 1) {
            throw new IllegalArgumentException("El identificador debe ser positivo.");
        }
        if (cliente == null) {
            throw new IllegalArgumentException("El cliente es obligatorio.");
        }
        if (lineas == null || lineas.isEmpty()) {
            throw new IllegalArgumentException("El pedido debe contener al menos un producto.");
        }
        if (lineas.stream().anyMatch(linea -> linea == null)) {
            throw new IllegalArgumentException("Las líneas del pedido no pueden ser nulas.");
        }
        if (politicaDescuento == null) {
            throw new IllegalArgumentException("La política de descuento es obligatoria.");
        }

        this.id = id;
        this.cliente = cliente;
        this.lineas = Collections.unmodifiableList(new ArrayList<>(lineas));
        this.subtotal = calcularSubtotal();
        int totalUnidades = calcularTotalUnidades();
        this.descuento = politicaDescuento.calcularDescuento(subtotal, totalUnidades);
        validarDescuento(descuento, subtotal);
        this.total = subtotal - descuento;
        this.nombrePoliticaDescuento = politicaDescuento.getNombre();
        this.estado = EstadoPedido.PENDIENTE;
    }

    private long calcularSubtotal() {
        long acumulado = 0;
        try {
            for (LineaPedido linea : lineas) {
                acumulado = Math.addExact(acumulado, linea.calcularSubtotal());
            }
            return acumulado;
        } catch (ArithmeticException excepcion) {
            throw new IllegalArgumentException("El subtotal del pedido es demasiado grande.");
        }
    }

    private int calcularTotalUnidades() {
        int acumulado = 0;
        try {
            for (LineaPedido linea : lineas) {
                acumulado = Math.addExact(acumulado, linea.getCantidad());
            }
            return acumulado;
        } catch (ArithmeticException excepcion) {
            throw new IllegalArgumentException("La cantidad total es demasiado grande.");
        }
    }

    private void validarDescuento(long valorDescuento, long valorSubtotal) {
        if (valorDescuento < 0 || valorDescuento > valorSubtotal) {
            throw new IllegalArgumentException("El descuento debe estar entre cero y el subtotal.");
        }
    }

    public void confirmar() {
        if (estado == EstadoPedido.CONFIRMADO) {
            throw new IllegalStateException("El pedido ya está confirmado.");
        }
        estado = EstadoPedido.CONFIRMADO;
    }

    public long getId() {
        return id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public List<LineaPedido> getLineas() {
        return lineas;
    }

    public String getNombrePoliticaDescuento() {
        return nombrePoliticaDescuento;
    }

    public long getSubtotal() {
        return subtotal;
    }

    public long getDescuento() {
        return descuento;
    }

    public long getTotal() {
        return total;
    }

    public EstadoPedido getEstado() {
        return estado;
    }

    public int getTotalUnidades() {
        return calcularTotalUnidades();
    }
}
