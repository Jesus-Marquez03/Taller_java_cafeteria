package com.ejercicios.modelo;

public class Cliente {
    private final String nombre;
    private final String correo;
    private final String telefono;

    public Cliente(String nombre, String correo) {
        this(nombre, correo, null);
    }

    public Cliente(String nombre, String correo, String telefono) {
        this.nombre = validarTexto(nombre, "El nombre del cliente es obligatorio.");
        this.correo = validarCorreo(correo);
        this.telefono = validarTelefono(telefono);
    }

    private String validarTexto(String valor, String mensaje) {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException(mensaje);
        }
        return valor.trim();
    }

    private String validarCorreo(String correo) {
        String correoLimpio = validarTexto(correo, "El correo del cliente es obligatorio.");
        int posicionArroba = correoLimpio.indexOf('@');
        int posicionPunto = correoLimpio.lastIndexOf('.');
        if (posicionArroba <= 0 || posicionPunto <= posicionArroba + 1
                || posicionPunto == correoLimpio.length() - 1) {
            throw new IllegalArgumentException("El correo no tiene un formato válido.");
        }
        return correoLimpio;
    }

    private String validarTelefono(String telefono) {
        if (telefono == null || telefono.isBlank()) {
            return null;
        }
        String telefonoLimpio = telefono.trim();
        if (!telefonoLimpio.matches("[0-9+ -]{7,20}")) {
            throw new IllegalArgumentException("El teléfono no tiene un formato válido.");
        }
        return telefonoLimpio;
    }

    public String getNombre() {
        return nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public String getTelefono() {
        return telefono;
    }
}
