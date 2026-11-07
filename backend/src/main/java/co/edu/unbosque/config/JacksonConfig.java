package co.edu.unbosque.config;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import jakarta.ws.rs.ext.ContextResolver;
import jakarta.ws.rs.ext.Provider;

/**
 * Configuración de JSON para toda la API.
 * - Las fechas (LocalDate) se envían como "yyyy-MM-dd" y no como [2026, 11, 3].
 * - Los campos desconocidos en el JSON recibido se ignoran.
 *
 * El módulo de fechas de Java (jackson-datatype-jsr310) lo provee WildFly.
 * Si por alguna razón no está disponible, se devuelve null y RESTEasy usa
 * su configuración por defecto, de modo que la API sigue funcionando.
 */
@Provider
public class JacksonConfig implements ContextResolver<ObjectMapper> {

    private static final Logger LOGGER = Logger.getLogger(JacksonConfig.class.getName());

    private final ObjectMapper mapper = crearMapper();

    private static ObjectMapper crearMapper() {
        try {
            Class<?> modulo = Class.forName(
                    "com.fasterxml.jackson.datatype.jsr310.JavaTimeModule");
            return new ObjectMapper()
                    .registerModule((Module) modulo.getDeclaredConstructor().newInstance())
                    .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                    .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        } catch (ReflectiveOperationException | LinkageError | ClassCastException e) {
            LOGGER.log(Level.WARNING,
                    "No se encontró jackson-datatype-jsr310; se usa la configuración JSON por defecto.", e);
            return null;
        }
    }

    @Override
    public ObjectMapper getContext(Class<?> type) {
        return mapper;
    }
}
