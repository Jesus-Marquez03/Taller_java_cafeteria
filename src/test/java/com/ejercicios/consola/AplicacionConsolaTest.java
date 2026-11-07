package com.ejercicios.consola;

import com.ejercicios.servicio.GestorPedidos;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertTrue;

class AplicacionConsolaTest {
    @Test
    void recuperaEntradaNoNumericaYContinuaHastaSalir() {
        String salida = ejecutarConEntrada("texto\n0\n5\n");

        assertTrue(salida.contains("Entrada inválida"));
        assertTrue(salida.contains("Opción inválida"));
        assertTrue(salida.contains("Gracias por usar Café Break"));
    }

    @Test
    void informaDineroNegativoSinCerrarLaAplicacion() {
        String entrada = "1\nAna\nana@correo.com\n\n1\nCafé\n10000\n1\n1\n"
                + "2\n1\n1\n-1\n5\n";

        String salida = ejecutarConEntrada(entrada);

        assertTrue(salida.contains("No se pudo procesar el pago"));
        assertTrue(salida.contains("Gracias por usar Café Break"));
    }

    private String ejecutarConEntrada(String entrada) {
        Scanner scanner = new Scanner(entrada);
        ByteArrayOutputStream salidaCapturada = new ByteArrayOutputStream();
        PrintStream salidaOriginal = System.out;
        try {
            System.setOut(new PrintStream(salidaCapturada, true, StandardCharsets.UTF_8));
            new AplicacionConsola(scanner, new GestorPedidos()).iniciar();
        } finally {
            System.setOut(salidaOriginal);
        }
        return salidaCapturada.toString(StandardCharsets.UTF_8);
    }
}
