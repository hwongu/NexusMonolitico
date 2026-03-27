package net.hwongu.nexus.be.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.Properties;

/**
 * Clase de utilidad para la gestion centralizada de la configuracion de la aplicacion.
 * Esta clase es responsable de cargar, una unica vez, el archivo de propiedades
 * {@code config.properties} desde el classpath. Proporciona metodos estaticos
 * para acceder a los valores de configuracion de forma segura en toda la aplicacion.
 *
 * @author Henry Wong (hwongu@gmail.com)
 */
public class ConfiguracionApp {

    private static final Properties propiedades = new Properties();
    private static final String NOMBRE_ARCHIVO = "config.properties";


    static {
        try (InputStream input = ClassLoader.getSystemResourceAsStream(NOMBRE_ARCHIVO)) {
            if (input == null) {
                System.err.println("Error critico: No se pudo encontrar el archivo " + NOMBRE_ARCHIVO);
                throw new RuntimeException("No se pudo encontrar el archivo de configuracion '" + NOMBRE_ARCHIVO + "'.");
            }
            // Carga las propiedades desde el flujo de entrada.
            propiedades.load(input);
            System.out.println("Archivo de configuracion '" + NOMBRE_ARCHIVO + "' cargado exitosamente.");
        } catch (IOException ex) {
            System.err.println("Error critico al leer el archivo de configuracion: " + ex.getMessage());
            // Este error es fatal para el arranque de la aplicacion.
            throw new RuntimeException("Error de I/O al leer el archivo de configuracion.", ex);
        }
    }

    /**
     * Constructor privado para evitar la instanciacion de esta clase de utilidad.
     */
    private ConfiguracionApp() {}

    /**
     * Obtiene el valor de una propiedad de configuracion como una cadena de texto.
     * Primero intenta resolver la clave desde propiedades del sistema y variables
     * de entorno. Si no encuentra un valor alli, utiliza el valor definido en
     * el archivo {@code config.properties}.
     *
     * @param clave El nombre de la propiedad (key) a obtener del archivo .properties.
     * @return El valor (value) asociado a la clave como un {@link String}.
     *         Retorna {@code null} si la clave no es encontrada.
     */
    public static String obtener(String clave) {
        String valorSistema = System.getProperty(clave);
        if (valorSistema != null && !valorSistema.trim().isEmpty()) {
            return valorSistema.trim();
        }

        String nombreVariableEntorno = convertirClaveAVariableEntorno(clave);
        String valorEntorno = System.getenv(nombreVariableEntorno);
        if (valorEntorno != null && !valorEntorno.trim().isEmpty()) {
            return valorEntorno.trim();
        }

        return propiedades.getProperty(clave);
    }

    /**
     * Obtiene el valor de una propiedad de configuracion como un numero entero.
     *
     * @param clave El nombre de la propiedad (key) a obtener.
     * @return El valor de la propiedad convertido a un tipo {@code int}.
     * @throws RuntimeException Si la clave no es encontrada en el archivo de configuracion.
     * @throws NumberFormatException Si el valor asociado a la clave no puede ser
     *                               interpretado como un numero entero.
     */
    public static int obtenerEntero(String clave) {
        String valor = obtener(clave);
        if (valor == null) {
            throw new RuntimeException("La propiedad requerida '" + clave + "' no fue encontrada en el archivo de configuracion.");
        }
        return Integer.parseInt(valor);
    }

    /**
     * Obtiene y normaliza el context path global del servidor.
     *
     * @return El context path listo para ser usado en el registro de contextos.
     *         Retorna una cadena vacia si no se ha configurado un prefijo global.
     */
    public static String obtenerContextPath() {
        String contextPath = obtener("server.context-path");

        if (contextPath == null) {
            return "";
        }

        contextPath = contextPath.trim();
        if (contextPath.isEmpty() || contextPath.equals("/")) {
            return "";
        }

        if (!contextPath.startsWith("/")) {
            contextPath = "/" + contextPath;
        }

        if (contextPath.endsWith("/")) {
            contextPath = contextPath.substring(0, contextPath.length() - 1);
        }

        return contextPath;
    }

    /**
     * Convierte una clave de configuracion al formato convencional de variable de entorno.
     *
     * @param clave La clave original del archivo de propiedades.
     * @return El nombre equivalente de la variable de entorno.
     */
    private static String convertirClaveAVariableEntorno(String clave) {
        return clave.toUpperCase(Locale.ROOT)
                .replace('.', '_')
                .replace('-', '_');
    }
}
