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
 * Expone endpoints REST para categorias.
 *
 * @author Henry Wong
 * GitHub @hwongu
 * https://github.com/hwongu
 */
public class CategoriaController implements HttpHandler {

    private final CategoriaService categoriaService;
    private final Gson gson;

    public CategoriaController() {
        this.categoriaService = new CategoriaService(new CategoriaRepository());
        this.gson = new Gson();
    }

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
                    if (path.matches("/\\d+")) {
                        Integer id = RutaHttpUtil.obtenerUltimoSegmentoComoEntero(path);
                        Categoria categoria = categoriaService.buscarCategoriaPorId(id);
                        if (categoria != null) {
                            response = gson.toJson(toDTO(categoria));
                        } else {
                            statusCode = 404;
                            response = gson.toJson(Map.of("error", "Categoria no encontrada"));
                        }
                    } else {
                        List<Categoria> categorias = categoriaService.listarCategorias();
                        List<CategoriaDTO> dtos = categorias.stream().map(this::toDTO).toList();
                        response = gson.toJson(dtos);
                    }
                    break;

                case "POST":
                    try (InputStreamReader reader = new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8)) {
                        CategoriaDTO dto = gson.fromJson(reader, CategoriaDTO.class);
                        Categoria categoria = toEntity(dto);
                        Categoria categoriaCreada = categoriaService.registrarCategoria(categoria);
                        statusCode = 201;
                        response = gson.toJson(toDTO(categoriaCreada));
                    }
                    break;

                case "PUT":
                    if (path.matches("/\\d+")) {
                        Integer id = RutaHttpUtil.obtenerUltimoSegmentoComoEntero(path);
                        try (InputStreamReader reader = new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8)) {
                            CategoriaDTO dto = gson.fromJson(reader, CategoriaDTO.class);
                            dto.setIdCategoria(id);
                            Categoria categoria = toEntity(dto);
                            categoriaService.actualizarCategoria(categoria);
                            response = gson.toJson(Map.of("message", "Categoria actualizada exitosamente"));
                        }
                    } else {
                        statusCode = 400;
                        response = gson.toJson(Map.of("error", "Se requiere un ID de categoria en la URL para actualizar"));
                    }
                    break;

                case "DELETE":
                    if (path.matches("/\\d+")) {
                        Integer id = RutaHttpUtil.obtenerUltimoSegmentoComoEntero(path);
                        categoriaService.eliminarCategoria(id);
                        statusCode = 204;
                        response = "";
                    } else {
                        statusCode = 400;
                        response = gson.toJson(Map.of("error", "Se requiere un ID de categoria en la URL para eliminar"));
                    }
                    break;

                default:
                    statusCode = 405;
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
            exchange.sendResponseHeaders(409, responseBytes.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(responseBytes);
            }
        } catch (RuntimeException e) {

            String errorResponse = gson.toJson(Map.of("error", "Error interno del servidor: " + e.getMessage()));
            byte[] responseBytes = errorResponse.getBytes(StandardCharsets.UTF_8);
            exchange.sendResponseHeaders(500, responseBytes.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(responseBytes);
            }
        }
    }

    private CategoriaDTO toDTO(Categoria categoria) {
        return new CategoriaDTO(
                categoria.getIdCategoria(),
                categoria.getNombre(),
                categoria.getDescripcion()
        );
    }

    private Categoria toEntity(CategoriaDTO dto) {
        return new Categoria(
                dto.getIdCategoria(),
                dto.getNombre(),
                dto.getDescripcion()
        );
    }
}
