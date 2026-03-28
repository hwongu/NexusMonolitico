package net.hwongu.nexus.be.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.Properties;

/**
 * Centraliza la configuracion del microservicio.
 *
 * @author Henry Wong
 * GitHub @hwongu
 * https://github.com/hwongu
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

            propiedades.load(input);
            System.out.println("Archivo de configuracion '" + NOMBRE_ARCHIVO + "' cargado exitosamente.");
        } catch (IOException ex) {
            System.err.println("Error critico al leer el archivo de configuracion: " + ex.getMessage());

            throw new RuntimeException("Error de I/O al leer el archivo de configuracion.", ex);
        }
    }

    private ConfiguracionApp() {}

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

    public static int obtenerEntero(String clave) {
        String valor = obtener(clave);
        if (valor == null) {
            throw new RuntimeException("La propiedad requerida '" + clave + "' no fue encontrada en el archivo de configuracion.");
        }
        return Integer.parseInt(valor);
    }

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

    private static String convertirClaveAVariableEntorno(String clave) {
        return clave.toUpperCase(Locale.ROOT)
                .replace('.', '_')
                .replace('-', '_');
    }
}
