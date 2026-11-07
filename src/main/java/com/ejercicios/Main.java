package com.ejercicios;

import com.ejercicios.consola.AplicacionConsola;
import com.ejercicios.servicio.GestorPedidos;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        GestorPedidos gestorPedidos = new GestorPedidos();
        AplicacionConsola aplicacion = new AplicacionConsola(scanner, gestorPedidos);
        aplicacion.iniciar();
    }
}
