package com.ejercicios.servicio;

import com.ejercicios.descuento.SinDescuento;
import com.ejercicios.modelo.Cliente;
import com.ejercicios.modelo.EstadoPedido;
import com.ejercicios.modelo.LineaPedido;
import com.ejercicios.modelo.Pedido;
import com.ejercicios.modelo.Producto;
import com.ejercicios.notificacion.NotificacionException;
import com.ejercicios.notificacion.Notificador;
import com.ejercicios.pago.MetodoPago;
import com.ejercicios.pago.PagoEfectivo;
import com.ejercicios.pago.PagoTarjeta;
import com.ejercicios.pago.ResultadoPago;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ServicioConfirmacionPedidoTest {
    @Test
    void pagoEfectivoSuficienteCalculaCambio() {
        ResultadoPago resultado = new PagoEfectivo(12_000).procesar(10_000);

        assertTrue(resultado.isAprobado());
        assertEquals(2_000, resultado.getCambio());
    }

    @Test
    void pagoEfectivoInsuficienteEsRechazado() {
        ResultadoPago resultado = new PagoEfectivo(9_999).procesar(10_000);

        assertFalse(resultado.isAprobado());
    }

    @Test
    void tarjetaPuedeAprobarseORechazarse() {
        assertTrue(new PagoTarjeta(true).procesar(10_000).isAprobado());
        assertFalse(new PagoTarjeta(false).procesar(10_000).isAprobado());
    }

    @Test
    void permiteReintentarTrasRechazoYNotificaSoloAlConfirmar() {
        Pedido pedido = crearPedido();
        NotificadorPrueba notificador = new NotificadorPrueba(false);
        ServicioConfirmacionPedido servicio = new ServicioConfirmacionPedido(notificador);

        ResultadoConfirmacion rechazo = servicio.intentarConfirmar(pedido, new PagoTarjeta(false));
        assertFalse(rechazo.getResultadoPago().isAprobado());
        assertEquals(EstadoPedido.PENDIENTE, pedido.getEstado());
        assertEquals(0, notificador.envios);

        ResultadoConfirmacion aprobacion = servicio.intentarConfirmar(pedido, new PagoTarjeta(true));
        assertTrue(aprobacion.getResultadoPago().isAprobado());
        assertEquals(EstadoPedido.CONFIRMADO, pedido.getEstado());
        assertEquals(1, notificador.envios);
    }

    @Test
    void noCobraNuevamenteUnPedidoConfirmado() {
        Pedido pedido = crearPedido();
        NotificadorPrueba notificador = new NotificadorPrueba(false);
        ServicioConfirmacionPedido servicio = new ServicioConfirmacionPedido(notificador);
        servicio.intentarConfirmar(pedido, new PagoTarjeta(true));
        AtomicInteger cobros = new AtomicInteger();
        MetodoPago pagoContado = total -> {
            cobros.incrementAndGet();
            return ResultadoPago.aprobado("Aprobado", 0);
        };

        ResultadoConfirmacion segundoIntento = servicio.intentarConfirmar(pedido, pagoContado);

        assertFalse(segundoIntento.getResultadoPago().isAprobado());
        assertEquals(0, cobros.get());
        assertEquals(1, notificador.envios);
    }

    @Test
    void falloDeNotificacionNoReviertePagoNiConfirmacion() {
        Pedido pedido = crearPedido();
        ServicioConfirmacionPedido servicio = new ServicioConfirmacionPedido(
                new NotificadorPrueba(true));

        ResultadoConfirmacion resultado = servicio.intentarConfirmar(
                pedido, new PagoTarjeta(true));

        assertTrue(resultado.getResultadoPago().isAprobado());
        assertFalse(resultado.isNotificacionEnviada());
        assertEquals(EstadoPedido.CONFIRMADO, pedido.getEstado());
        assertTrue(resultado.getMensajeNotificacion().contains("falló"));
    }

    private Pedido crearPedido() {
        return new Pedido(1, new Cliente("Ana", "ana@correo.com"),
                List.of(new LineaPedido(new Producto("Café", 10_000), 1)),
                new SinDescuento());
    }

    private static class NotificadorPrueba implements Notificador {
        private final boolean fallar;
        private int envios;

        private NotificadorPrueba(boolean fallar) {
            this.fallar = fallar;
        }

        @Override
        public void enviarConfirmacion(Pedido pedido) throws NotificacionException {
            if (fallar) {
                throw new NotificacionException("Fallo controlado");
            }
            envios++;
        }
    }
}
