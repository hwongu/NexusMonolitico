package net.hwongu.nexus.be.config;

import com.sun.net.httpserver.Filter;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;

/**
 * Centraliza la configuracion CORS del microservicio.
 *
 * @author Henry Wong
 * GitHub @hwongu
 * https://github.com/hwongu
 */
public class CorsFilter extends Filter {

    @Override
    public String description() {
        return "Filtro para habilitar el soporte de CORS en las respuestas HTTP.";
    }

    @Override
    public void doFilter(HttpExchange exchange, Chain chain) throws IOException {

        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type, Authorization");

        if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
            exchange.sendResponseHeaders(204, -1);
            return;
        }

        chain.doFilter(exchange);
    }
}
