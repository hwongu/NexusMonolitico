package net.hwongu.nexus.be.exception;

/**
 * Excepción específica para indicar que un recurso solicitado no ha sido encontrado.
 * Esta clase hereda de {@link NexusException} y establece por defecto el código
 * de estado HTTP 404 (Not Found).
 *
 * @author Henry Wong (hwongu@gmail.com)
 */
public class ResourceNotFoundException extends NexusException {

    /**
     * Construye una nueva ResourceNotFoundException con un mensaje de detalle.
     * El código de estado se fija internamente en 404.
     *
     * @param mensaje El mensaje que describe el recurso no encontrado,
     *                por ejemplo, "Producto con ID 123 no encontrado".
     */
    public ResourceNotFoundException(String mensaje) {
        super(mensaje, 404);
    }
}
