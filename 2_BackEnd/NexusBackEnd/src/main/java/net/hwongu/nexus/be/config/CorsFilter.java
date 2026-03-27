package net.hwongu.nexus.be.config;

import com.sun.net.httpserver.Filter;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;

/**
 * Filtro para gestionar el Intercambio de Recursos de Origen Cruzado (CORS).
 * Esta clase intercepta todas las peticiones HTTP para añadir las cabeceras
 * CORS necesarias en la respuesta, permitiendo que los clientes web (como una
 * aplicación en Angular) alojados en un origen diferente puedan consumir la API.
 * También maneja las solicitudes de "pre-vuelo" (preflight) del método OPTIONS.
 *
 * @author Henry Wong (hwongu@gmail.com)
 */
public class CorsFilter extends Filter {

    /**
     * Proporciona una descripción del propósito del filtro.
     * @return Una cadena de texto que describe la funcionalidad del filtro.
     */
    @Override
    public String description() {
        return "Filtro para habilitar el soporte de CORS en las respuestas HTTP.";
    }

    /**
     * Aplica la lógica del filtro a una petición HTTP entrante.
     *
     * @param exchange El objeto HttpExchange que encapsula la petición y la respuesta.
     * @param chain La cadena de filtros a la que pertenece este filtro.
     * @throws IOException Si ocurre un error de entrada/salida.
     */
    @Override
    public void doFilter(HttpExchange exchange, Chain chain) throws IOException {
        // Añade las cabeceras CORS a todas las respuestas.
        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type, Authorization");

        // Si la petición es de tipo OPTIONS (pre-vuelo), se responde inmediatamente
        // con éxito y no se continúa con el resto de la cadena de procesamiento.
        if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
            exchange.sendResponseHeaders(204, -1); // 204 No Content
            return;
        }

        // Para cualquier otro método, se continúa con el flujo normal de la petición.
        chain.doFilter(exchange);
    }
}
