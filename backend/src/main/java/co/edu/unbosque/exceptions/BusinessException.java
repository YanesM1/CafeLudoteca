package co.edu.unbosque.exceptions;

/**
 * Error de negocio o de validación que debe llegar al cliente con un
 * código HTTP concreto y un mensaje legible.
 */
public class BusinessException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final int status;

    public BusinessException(int status, String mensaje) {
        super(mensaje);
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public static BusinessException badRequest(String mensaje) {
        return new BusinessException(400, mensaje);
    }

    public static BusinessException notFound(String mensaje) {
        return new BusinessException(404, mensaje);
    }

    public static BusinessException conflict(String mensaje) {
        return new BusinessException(409, mensaje);
    }
}
