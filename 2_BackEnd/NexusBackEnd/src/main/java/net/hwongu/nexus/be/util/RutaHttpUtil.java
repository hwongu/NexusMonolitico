package net.hwongu.nexus.be.util;

import com.sun.net.httpserver.HttpExchange;

/**
 * Procesa rutas HTTP relativas del microservicio.
 *
 * @author Henry Wong
 * GitHub @hwongu
 * https://github.com/hwongu
 */
public class RutaHttpUtil {

    private RutaHttpUtil() {}

    public static String obtenerRutaRelativa(HttpExchange exchange) {
        String requestPath = exchange.getRequestURI().getPath();
        String contextPath = exchange.getHttpContext().getPath();

        if (!requestPath.startsWith(contextPath)) {
            return requestPath;
        }

        String relativePath = requestPath.substring(contextPath.length());
        return relativePath.isEmpty() ? "/" : relativePath;
    }

    public static Integer obtenerPrimerSegmentoComoEntero(String path) {
        int inicio = path.startsWith("/") ? 1 : 0;
        int fin = path.indexOf('/', inicio);
        String valor = fin == -1 ? path.substring(inicio) : path.substring(inicio, fin);
        return Integer.parseInt(valor);
    }

    public static Integer obtenerUltimoSegmentoComoEntero(String path) {
        return Integer.parseInt(path.substring(path.lastIndexOf('/') + 1));
    }
}
