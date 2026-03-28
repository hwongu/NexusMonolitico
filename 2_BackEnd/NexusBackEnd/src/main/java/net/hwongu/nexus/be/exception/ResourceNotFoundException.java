package net.hwongu.nexus.be.exception;

/**
 * Representa errores por recursos no encontrados.
 *
 * @author Henry Wong
 * GitHub @hwongu
 * https://github.com/hwongu
 */
public class ResourceNotFoundException extends NexusException {

    public ResourceNotFoundException(String mensaje) {
        super(mensaje, 404);
    }
}
