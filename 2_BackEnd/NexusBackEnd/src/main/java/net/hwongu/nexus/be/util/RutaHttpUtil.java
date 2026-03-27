package net.hwongu.nexus.be.util;

import com.sun.net.httpserver.HttpExchange;

/**
 * Clase de utilidad para el trabajo con rutas HTTP dentro de los controladores.
 * Permite desacoplar la logica de los handlers del context path global con el
 * que se haya registrado cada {@code HttpContext}.
 *
 * @author Henry Wong (hwongu@gmail.com)
 */
public class RutaHttpUtil {

    /**
     * Constructor privado para evitar la instanciacion de esta clase de utilidad.
     */
    private RutaHttpUtil() {}

    /**
     * Obtiene la subruta solicitada relativa al contexto actual del handler.
     *
     * @param exchange El objeto que contiene la peticion HTTP actual.
     * @return La ruta relativa al contexto actual. Si la peticion apunta
     *         exactamente al contexto base, retorna {@code "/"}.
     */
    public static String obtenerRutaRelativa(HttpExchange exchange) {
        String requestPath = exchange.getRequestURI().getPath();
        String contextPath = exchange.getHttpContext().getPath();

        if (!requestPath.startsWith(contextPath)) {
            return requestPath;
        }

        String relativePath = requestPath.substring(contextPath.length());
        return relativePath.isEmpty() ? "/" : relativePath;
    }

    /**
     * Obtiene el primer segmento numerico de una subruta relativa.
     *
     * @param path La subruta relativa a procesar.
     * @return El valor entero del primer segmento.
     */
    public static Integer obtenerPrimerSegmentoComoEntero(String path) {
        int inicio = path.startsWith("/") ? 1 : 0;
        int fin = path.indexOf('/', inicio);
        String valor = fin == -1 ? path.substring(inicio) : path.substring(inicio, fin);
        return Integer.parseInt(valor);
    }

    /**
     * Obtiene el ultimo segmento numerico de una subruta relativa.
     *
     * @param path La subruta relativa a procesar.
     * @return El valor entero del ultimo segmento.
     */
    public static Integer obtenerUltimoSegmentoComoEntero(String path) {
        return Integer.parseInt(path.substring(path.lastIndexOf('/') + 1));
    }
}
