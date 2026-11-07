package co.edu.unbosque.services;

import java.text.Normalizer;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Planes y estados de membresía definidos en la propuesta del proyecto
 * ("prémium, Alta, Media, Básica y Baja"; estados "Activa, Suspendida o Vencida").
 * Cada plan tiene una tarifa mensual fija que se asigna automáticamente.
 */
public final class PlanesMembresia {

    /** Tarifa mensual en pesos colombianos, de mayor a menor plan. */
    public static final Map<String, Double> TARIFAS = new LinkedHashMap<>();

    static {
        TARIFAS.put("Premium", 35000.0);
        TARIFAS.put("Alta", 25000.0);
        TARIFAS.put("Media", 18000.0);
        TARIFAS.put("Básica", 12000.0);
        TARIFAS.put("Baja", 8000.0);
    }

    public static final String ACTIVA = "Activa";
    public static final String SUSPENDIDA = "Suspendida";
    public static final String VENCIDA = "Vencida";

    private PlanesMembresia() {
    }

    private static String clave(String valor) {
        String sinTildes = Normalizer.normalize(valor.trim(), Normalizer.Form.NFD)
            .replaceAll("\\p{M}", "");
        return sinTildes.toLowerCase();
    }

    /** Devuelve el nombre oficial del plan o null si no es un plan válido. */
    public static String plan(String valor) {
        if (valor == null || valor.isBlank()) return null;
        String buscado = clave(valor);
        for (String plan : TARIFAS.keySet()) {
            if (clave(plan).equals(buscado)) return plan;
        }
        return null;
    }

    /** Devuelve el nombre oficial del estado o null si no es válido. */
    public static String estado(String valor) {
        if (valor == null || valor.isBlank()) return null;
        String buscado = clave(valor);
        for (String estado : new String[] {ACTIVA, SUSPENDIDA, VENCIDA}) {
            if (clave(estado).equals(buscado)) return estado;
        }
        return null;
    }

    public static double tarifa(String plan) {
        return TARIFAS.get(plan);
    }
}
