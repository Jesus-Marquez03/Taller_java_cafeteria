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
        Scanner scanner = new Scanner("texto\n0\n5\n");
        ByteArrayOutputStream salidaCapturada = new ByteArrayOutputStream();
        PrintStream salidaOriginal = System.out;
        try {
            System.setOut(new PrintStream(salidaCapturada, true, StandardCharsets.UTF_8));
            new AplicacionConsola(scanner, new GestorPedidos()).iniciar();
        } finally {
            System.setOut(salidaOriginal);
        }

        String salida = salidaCapturada.toString(StandardCharsets.UTF_8);
        assertTrue(salida.contains("Entrada inválida"));
        assertTrue(salida.contains("Opción inválida"));
        assertTrue(salida.contains("Gracias por usar Café Break"));
    }
}
