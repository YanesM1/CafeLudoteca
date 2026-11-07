package co.edu.unbosque.services;

import co.edu.unbosque.exceptions.BusinessException;

/**
 * Validaciones comunes de los servicios. Cada método lanza una
 * BusinessException (400) con un mensaje claro para el usuario.
 */
public final class Validaciones {

    private Validaciones() {
    }

    public static void requerido(Object valor, String mensaje) {
        if (valor == null) {
            throw BusinessException.badRequest(mensaje);
        }
    }

    public static void texto(String valor, String campo) {
        if (valor == null || valor.isBlank()) {
            throw BusinessException.badRequest("El campo " + campo + " es obligatorio.");
        }
    }

    public static void positivo(Integer valor, String campo) {
        if (valor == null || valor <= 0) {
            throw BusinessException.badRequest("El campo " + campo + " debe ser un número mayor que cero.");
        }
    }

    public static void numerico(String valor, String campo) {
        texto(valor, campo);
        if (!valor.trim().matches("\\d+")) {
            throw BusinessException.badRequest("El campo " + campo + " debe ser numérico.");
        }
    }
}
