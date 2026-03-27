package net.hwongu.nexus.be.controller;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import net.hwongu.nexus.be.dto.CategoriaDTO;
import net.hwongu.nexus.be.entity.Categoria;
import net.hwongu.nexus.be.exception.DataIntegrityViolationException;
import net.hwongu.nexus.be.repository.CategoriaRepository;
import net.hwongu.nexus.be.service.CategoriaService;
import net.hwongu.nexus.be.util.RutaHttpUtil;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

/**
 * Controlador HTTP para la gestion de {@link Categoria}.
 * Implementa la interfaz {@link HttpHandler} para procesar las peticiones
 * entrantes relacionadas con el recurso `/api/categorias`. Se encarga de
 * interpretar el metodo HTTP, invocar la logica de negocio a traves de
 * {@link CategoriaService} y construir respuestas HTTP en formato JSON.
 *
 * @author Henry Wong (hwongu@gmail.com)
 */
public class CategoriaController implements HttpHandler {

    private final CategoriaService categoriaService;
    private final Gson gson;

    /**
     * Constructor del controlador.
     * Inicializa el servicio de categorias y la instancia de Gson para
     * la serializacion/deserializacion JSON.
     */
    public CategoriaController() {
        this.categoriaService = new CategoriaService(new CategoriaRepository());
        this.gson = new Gson();
    }

    /**
     * Maneja las peticiones HTTP entrantes para el recurso de categorias.
     * Este metodo es el punto de entrada para todas las solicitudes dirigidas
     * a `/api/categorias` y sus subrutas.
     *
     * @param exchange El objeto {@link HttpExchange} que representa la peticion y permite construir la respuesta.
     * @throws IOException Si ocurre un error de entrada/salida al leer la peticion o escribir la respuesta.
     */
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            String path = RutaHttpUtil.obtenerRutaRelativa(exchange);
            String method = exchange.getRequestMethod();
            String response;
            int statusCode = 200;

            exchange.getResponseHeaders().set("Content-Type", "application/json");

            switch (method) {
                case "GET":
                    if (path.matches("/\\d+")) { // Expresion regular para /{id}
                        Integer id = RutaHttpUtil.obtenerUltimoSegmentoComoEntero(path);
                        Categoria categoria = categoriaService.buscarCategoriaPorId(id);
                        if (categoria != null) {
                            response = gson.toJson(toDTO(categoria));
                        } else {
                            statusCode = 404;
                            response = gson.toJson(Map.of("error", "Categoria no encontrada"));
                        }
                    } else { // GET /api/categorias
                        List<Categoria> categorias = categoriaService.listarCategorias();
                        List<CategoriaDTO> dtos = categorias.stream().map(this::toDTO).toList();
                        response = gson.toJson(dtos);
                    }
                    break;

                case "POST": // POST /api/categorias
                    try (InputStreamReader reader = new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8)) {
                        CategoriaDTO dto = gson.fromJson(reader, CategoriaDTO.class);
                        Categoria categoria = toEntity(dto);
                        Categoria categoriaCreada = categoriaService.registrarCategoria(categoria);
                        statusCode = 201; // Created
                        response = gson.toJson(toDTO(categoriaCreada));
                    }
                    break;

                case "PUT": // PUT /api/categorias/{id}
                    if (path.matches("/\\d+")) {
                        Integer id = RutaHttpUtil.obtenerUltimoSegmentoComoEntero(path);
                        try (InputStreamReader reader = new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8)) {
                            CategoriaDTO dto = gson.fromJson(reader, CategoriaDTO.class);
                            dto.setIdCategoria(id); // Aseguramos que el ID del DTO coincida con el de la URL
                            Categoria categoria = toEntity(dto);
                            categoriaService.actualizarCategoria(categoria);
                            response = gson.toJson(Map.of("message", "Categoria actualizada exitosamente"));
                        }
                    } else {
                        statusCode = 400;
                        response = gson.toJson(Map.of("error", "Se requiere un ID de categoria en la URL para actualizar"));
                    }
                    break;

                case "DELETE": // DELETE /api/categorias/{id}
                    if (path.matches("/\\d+")) {
                        Integer id = RutaHttpUtil.obtenerUltimoSegmentoComoEntero(path);
                        categoriaService.eliminarCategoria(id);
                        statusCode = 204; // No Content
                        response = ""; // No hay contenido en la respuesta para 204
                    } else {
                        statusCode = 400;
                        response = gson.toJson(Map.of("error", "Se requiere un ID de categoria en la URL para eliminar"));
                    }
                    break;

                default:
                    statusCode = 405; // Method Not Allowed
                    response = gson.toJson(Map.of("error", "Metodo HTTP no permitido para este recurso"));
                    break;
            }

            byte[] responseBytes = response.getBytes(StandardCharsets.UTF_8);
            exchange.sendResponseHeaders(statusCode, responseBytes.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(responseBytes);
            }

        } catch (DataIntegrityViolationException e) {
            String errorResponse = gson.toJson(Map.of("error", e.getMessage()));
            byte[] responseBytes = errorResponse.getBytes(StandardCharsets.UTF_8);
            exchange.getResponseHeaders().set("Content-Type", "application/json");
            exchange.sendResponseHeaders(409, responseBytes.length); // 409 Conflict
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(responseBytes);
            }
        } catch (RuntimeException e) {
            // Captura de excepciones generales de la capa de servicio
            String errorResponse = gson.toJson(Map.of("error", "Error interno del servidor: " + e.getMessage()));
            byte[] responseBytes = errorResponse.getBytes(StandardCharsets.UTF_8);
            exchange.sendResponseHeaders(500, responseBytes.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(responseBytes);
            }
        }
    }

    /**
     * Convierte un objeto {@link Categoria} de entidad a un {@link CategoriaDTO}.
     *
     * @param categoria La entidad Categoria a convertir.
     * @return Un objeto CategoriaDTO.
     */
    private CategoriaDTO toDTO(Categoria categoria) {
        return new CategoriaDTO(
                categoria.getIdCategoria(),
                categoria.getNombre(),
                categoria.getDescripcion()
        );
    }

    /**
     * Convierte un objeto {@link CategoriaDTO} a una entidad {@link Categoria}.
     *
     * @param dto El DTO de Categoria a convertir.
     * @return Un objeto Categoria de entidad.
     */
    private Categoria toEntity(CategoriaDTO dto) {
        return new Categoria(
                dto.getIdCategoria(),
                dto.getNombre(),
                dto.getDescripcion()
        );
    }
}
