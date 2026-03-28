package net.hwongu.nexus.be.exception;

/**
 * Representa errores base del microservicio.
 *
 * @author Henry Wong
 * GitHub @hwongu
 * https://github.com/hwongu
 */
public class NexusException extends RuntimeException {

    private final int codigoEstado;

    public NexusException(String mensaje, int codigoEstado) {
        super(mensaje);
        this.codigoEstado = codigoEstado;
    }

    public int getCodigoEstado() {
        return codigoEstado;
    }
}
