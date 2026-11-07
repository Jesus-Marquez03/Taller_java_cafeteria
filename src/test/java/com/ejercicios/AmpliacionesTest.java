package com.ejercicios;

import com.ejercicios.descuento.DescuentoFijo;
import com.ejercicios.modelo.Cliente;
import com.ejercicios.modelo.EstadoPedido;
import com.ejercicios.modelo.LineaPedido;
import com.ejercicios.modelo.Pedido;
import com.ejercicios.modelo.Producto;
import com.ejercicios.notificacion.NotificadorSms;
import com.ejercicios.pago.PagoTarjeta;
import com.ejercicios.pago.PagoTransferencia;
import com.ejercicios.servicio.ResultadoConfirmacion;
import com.ejercicios.servicio.ServicioConfirmacionPedido;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AmpliacionesTest {
    @Test
    void transferenciaSimuladaPuedeAprobarseORechazarse() {
        assertTrue(new PagoTransferencia(true).procesar(20_000).isAprobado());
        assertFalse(new PagoTransferencia(false).procesar(20_000).isAprobado());
    }

    @Test
    void descuentoFijoAplicaDesdeCuarentaMil() {
        Pedido pedido = crearPedido(new Cliente("Sara", "sara@correo.com"), 40_000);

        assertEquals(5_000, pedido.getDescuento());
        assertEquals(35_000, pedido.getTotal());
    }

    @Test
    void descuentoFijoNoAplicaPorDebajoDeCuarentaMil() {
        Pedido pedido = crearPedido(new Cliente("Sara", "sara@correo.com"), 39_999);

        assertEquals(0, pedido.getDescuento());
        assertEquals(39_999, pedido.getTotal());
    }

    @Test
    void smsSimuladoNotificaPedidoConfirmado() {
        Cliente cliente = new Cliente("Sara", "sara@correo.com", "300 123 4567");
        Pedido pedido = crearPedido(cliente, 40_000);
        ServicioConfirmacionPedido servicio = new ServicioConfirmacionPedido(
                new NotificadorSms(false));

        ResultadoConfirmacion resultado = servicio.intentarConfirmar(
                pedido, new PagoTarjeta(true));

        assertTrue(resultado.isNotificacionEnviada());
        assertEquals(EstadoPedido.CONFIRMADO, pedido.getEstado());
    }

    @Test
    void faltaDeTelefonoHaceFallarSmsSinRevertirConfirmacion() {
        Pedido pedido = crearPedido(new Cliente("Sara", "sara@correo.com"), 40_000);
        ServicioConfirmacionPedido servicio = new ServicioConfirmacionPedido(
                new NotificadorSms(false));

        ResultadoConfirmacion resultado = servicio.intentarConfirmar(
                pedido, new PagoTarjeta(true));

        assertFalse(resultado.isNotificacionEnviada());
        assertEquals(EstadoPedido.CONFIRMADO, pedido.getEstado());
    }

    private Pedido crearPedido(Cliente cliente, long precio) {
        return new Pedido(1, cliente,
                List.of(new LineaPedido(new Producto("Producto", precio), 1)),
                new DescuentoFijo());
    }
}
