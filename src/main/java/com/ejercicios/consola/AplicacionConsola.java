package com.ejercicios.consola;

import com.ejercicios.descuento.DescuentoClienteFrecuente;
import com.ejercicios.descuento.DescuentoFijo;
import com.ejercicios.descuento.DescuentoPorVolumen;
import com.ejercicios.descuento.PoliticaDescuento;
import com.ejercicios.descuento.SinDescuento;
import com.ejercicios.modelo.Cliente;
import com.ejercicios.modelo.LineaPedido;
import com.ejercicios.modelo.Pedido;
import com.ejercicios.modelo.Producto;
import com.ejercicios.notificacion.Notificador;
import com.ejercicios.notificacion.NotificadorCorreo;
import com.ejercicios.notificacion.NotificadorSms;
import com.ejercicios.pago.MetodoPago;
import com.ejercicios.pago.PagoEfectivo;
import com.ejercicios.pago.PagoTarjeta;
import com.ejercicios.pago.PagoTransferencia;
import com.ejercicios.servicio.GestorPedidos;
import com.ejercicios.servicio.ResultadoConfirmacion;
import com.ejercicios.servicio.ServicioConfirmacionPedido;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class AplicacionConsola {
    private final Scanner scanner;
    private final GestorPedidos gestorPedidos;

    public AplicacionConsola(Scanner scanner, GestorPedidos gestorPedidos) {
        this.scanner = scanner;
        this.gestorPedidos = gestorPedidos;
    }

    public void iniciar() {
        System.out.println("=== Café Break: sistema de pedidos ===");
        boolean continuar = true;
        while (continuar) {
            mostrarMenu();
            int opcion = leerEntero("Seleccione una opción: ");
            switch (opcion) {
                case 1 -> registrarPedido();
                case 2 -> intentarPago();
                case 3 -> consultarPedido();
                case 4 -> listarPedidos();
                case 5 -> continuar = false;
                default -> System.out.println("Opción inválida. Elija un número del 1 al 5.");
            }
        }
        System.out.println("Gracias por usar Café Break.");
    }

    private void mostrarMenu() {
        System.out.println("\n1. Registrar pedido");
        System.out.println("2. Intentar pagar pedido pendiente");
        System.out.println("3. Consultar pedido por ID");
        System.out.println("4. Listar pedidos");
        System.out.println("5. Salir");
    }

    private void registrarPedido() {
        try {
            String nombre = leerTextoObligatorio("Nombre del cliente: ");
            String correo = leerTextoObligatorio("Correo del cliente: ");
            String telefono = leerTexto("Teléfono opcional (Enter para omitir): ");
            Cliente cliente = new Cliente(nombre, correo, telefono);

            int cantidadProductos = leerEnteroPositivo("Cantidad de productos diferentes: ");
            List<LineaPedido> lineas = new ArrayList<>();
            for (int i = 1; i <= cantidadProductos; i++) {
                System.out.println("Producto " + i + ":");
                String nombreProducto = leerTextoObligatorio("  Nombre: ");
                long precio = leerLong("  Precio unitario en pesos: ");
                int cantidad = leerEntero("  Cantidad: ");
                lineas.add(new LineaPedido(new Producto(nombreProducto, precio), cantidad));
            }

            PoliticaDescuento politica = seleccionarPoliticaDescuento();
            Pedido pedido = gestorPedidos.registrar(cliente, lineas, politica);
            System.out.println("Pedido #" + pedido.getId() + " registrado correctamente.");
            mostrarDetalle(pedido);
        } catch (IllegalArgumentException excepcion) {
            System.out.println("No se pudo registrar el pedido: " + excepcion.getMessage());
        }
    }

    private PoliticaDescuento seleccionarPoliticaDescuento() {
        while (true) {
            System.out.println("Política de descuento:");
            System.out.println("1. Sin descuento");
            System.out.println("2. Cliente frecuente (10 %)");
            System.out.println("3. Volumen (15 % desde 10 unidades)");
            System.out.println("4. Descuento fijo de $5.000 desde $40.000");
            int opcion = leerEntero("Seleccione: ");
            switch (opcion) {
                case 1:
                    return new SinDescuento();
                case 2:
                    return new DescuentoClienteFrecuente();
                case 3:
                    return new DescuentoPorVolumen();
                case 4:
                    return new DescuentoFijo();
                default:
                    System.out.println("Política inválida.");
            }
        }
    }

    private void intentarPago() {
        long id = leerLong("ID del pedido: ");
        Optional<Pedido> pedidoEncontrado = gestorPedidos.buscarPorId(id);
        if (pedidoEncontrado.isEmpty()) {
            System.out.println("No existe un pedido con ese ID.");
            return;
        }

        MetodoPago metodoPago = seleccionarMetodoPago();
        if (metodoPago == null) {
            return;
        }

        Notificador notificador = seleccionarNotificador();
        ServicioConfirmacionPedido servicio = new ServicioConfirmacionPedido(notificador);
        ResultadoConfirmacion resultado = servicio.intentarConfirmar(
                pedidoEncontrado.get(), metodoPago);

        System.out.println(resultado.getResultadoPago().getMensaje());
        if (resultado.getResultadoPago().isAprobado()
                && resultado.getResultadoPago().getCambio() > 0) {
            System.out.println("Cambio: " + formatearDinero(
                    resultado.getResultadoPago().getCambio()));
        }
        System.out.println(resultado.getMensajeNotificacion());
    }

    private Notificador seleccionarNotificador() {
        while (true) {
            System.out.println("Canal de notificación:");
            System.out.println("1. Correo electrónico");
            System.out.println("2. SMS");
            int opcion = leerEntero("Seleccione: ");
            boolean simularFallo;
            switch (opcion) {
                case 1:
                    simularFallo = leerSiNo("¿Simular fallo del correo? (s/n): ");
                    return new NotificadorCorreo(simularFallo);
                case 2:
                    simularFallo = leerSiNo("¿Simular fallo del SMS? (s/n): ");
                    return new NotificadorSms(simularFallo);
                default:
                    System.out.println("Canal de notificación inválido.");
            }
        }
    }

    private MetodoPago seleccionarMetodoPago() {
        System.out.println("Método de pago:");
        System.out.println("1. Efectivo");
        System.out.println("2. Tarjeta simulada");
        System.out.println("3. Transferencia simulada");
        int opcion = leerEntero("Seleccione: ");
        return switch (opcion) {
            case 1 -> new PagoEfectivo(leerLong("Dinero recibido: "));
            case 2 -> new PagoTarjeta(leerSiNo("¿Simular aprobación? (s/n): "));
            case 3 -> new PagoTransferencia(
                    leerSiNo("¿Simular aprobación? (s/n): "));
            default -> {
                System.out.println("Método de pago inválido.");
                yield null;
            }
        };
    }

    private void consultarPedido() {
        long id = leerLong("ID del pedido: ");
        gestorPedidos.buscarPorId(id).ifPresentOrElse(
                this::mostrarDetalle,
                () -> System.out.println("No existe un pedido con ese ID."));
    }

    private void listarPedidos() {
        List<Pedido> pedidos = gestorPedidos.listar();
        if (pedidos.isEmpty()) {
            System.out.println("No hay pedidos registrados.");
            return;
        }
        for (Pedido pedido : pedidos) {
            mostrarDetalle(pedido);
        }
    }

    private void mostrarDetalle(Pedido pedido) {
        System.out.printf("%nPedido #%d | Cliente: %s | Estado: %s%n",
                pedido.getId(), pedido.getCliente().getNombre(), pedido.getEstado());
        for (LineaPedido linea : pedido.getLineas()) {
            System.out.printf("- %s: %d x %s = %s%n",
                    linea.getProducto().getNombre(),
                    linea.getCantidad(),
                    formatearDinero(linea.getProducto().getPrecioUnitario()),
                    formatearDinero(linea.calcularSubtotal()));
        }
        System.out.println("Política: " + pedido.getNombrePoliticaDescuento());
        System.out.println("Subtotal: " + formatearDinero(pedido.getSubtotal()));
        System.out.println("Descuento: " + formatearDinero(pedido.getDescuento()));
        System.out.println("Total: " + formatearDinero(pedido.getTotal()));
    }

    private String leerTexto(String mensaje) {
        System.out.print(mensaje);
        return scanner.nextLine().trim();
    }

    private String leerTextoObligatorio(String mensaje) {
        while (true) {
            String valor = leerTexto(mensaje);
            if (!valor.isBlank()) {
                return valor;
            }
            System.out.println("El valor no puede estar vacío.");
        }
    }

    private int leerEntero(String mensaje) {
        while (true) {
            String entrada = leerTexto(mensaje);
            try {
                return Integer.parseInt(entrada);
            } catch (NumberFormatException excepcion) {
                System.out.println("Entrada inválida. Escriba un número entero.");
            }
        }
    }

    private int leerEnteroPositivo(String mensaje) {
        while (true) {
            int numero = leerEntero(mensaje);
            if (numero > 0) {
                return numero;
            }
            System.out.println("El número debe ser mayor que cero.");
        }
    }

    private long leerLong(String mensaje) {
        while (true) {
            String entrada = leerTexto(mensaje);
            try {
                return Long.parseLong(entrada);
            } catch (NumberFormatException excepcion) {
                System.out.println("Entrada inválida. Escriba un número entero.");
            }
        }
    }

    private boolean leerSiNo(String mensaje) {
        while (true) {
            String respuesta = leerTexto(mensaje).toLowerCase();
            if (respuesta.equals("s")) {
                return true;
            }
            if (respuesta.equals("n")) {
                return false;
            }
            System.out.println("Respuesta inválida. Escriba s o n.");
        }
    }

    private String formatearDinero(long valor) {
        return String.format("$%,d", valor).replace(',', '.');
    }
}
