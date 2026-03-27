package net.hwongu.nexus.be.exception;

/**
 * Excepción base personalizada para la aplicación Nexus.
 * Esta excepción no verificada (unchecked) se utiliza para encapsular errores
 * específicos de la lógica de negocio, transportando un mensaje descriptivo y
 * un código de estado HTTP sugerido para la respuesta de la API.
 *
 * @author Henry Wong (hwongu@gmail.com)
 */
public class NexusException extends RuntimeException {

    private final int codigoEstado;

    /**
     * Construye una nueva NexusException con un mensaje y un código de estado.
     *
     * @param mensaje El mensaje de detalle que describe la excepción.
     * @param codigoEstado El código de estado HTTP que se debe asociar con esta excepción.
     */
    public NexusException(String mensaje, int codigoEstado) {
        super(mensaje);
        this.codigoEstado = codigoEstado;
    }

    /**
     * Obtiene el código de estado HTTP asociado a esta excepción.
     *
     * @return El código de estado HTTP.
     */
    public int getCodigoEstado() {
        return codigoEstado;
    }
}
