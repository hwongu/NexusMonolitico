package net.hwongu.nexus.be.exception;

/**
 * Excepción personalizada para representar violaciones de integridad de datos,
 * como intentos de eliminar un registro que está siendo referenciado por otros.
 * <p>
 * Esta clase hereda de {@link RuntimeException}, lo que la convierte en una
 * excepción no comprobada (unchecked). Esto simplifica el manejo de errores,
 * ya que no obliga a los métodos intermedios (como los de la capa de servicio)
 * a declarar o capturar la excepción si solo necesitan propagarla.
 *
 * @author Henry Wong (hwongu@gmail.com)
 */
public class DataIntegrityViolationException extends RuntimeException {

    /**
     * Constructor que acepta un mensaje de error detallado.
     *
     * @param message El mensaje que describe la causa de la excepción.
     */
    public DataIntegrityViolationException(String message) {
        super(message);
    }

    /**
     * Constructor que acepta un mensaje de error y la causa original.
     *
     * @param message El mensaje que describe la causa de la excepción.
     * @param cause   La excepción original (por ejemplo, una {@code SQLException}) que provocó este error.
     */
    public DataIntegrityViolationException(String message, Throwable cause) {
        super(message, cause);
    }
}
