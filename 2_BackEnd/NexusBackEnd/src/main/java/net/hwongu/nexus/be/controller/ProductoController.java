package net.hwongu.nexus.be.controller;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import net.hwongu.nexus.be.dto.ProductoDTO;
import net.hwongu.nexus.be.entity.Categoria;
import net.hwongu.nexus.be.entity.Producto;
import net.hwongu.nexus.be.exception.DataIntegrityViolationException;
import net.hwongu.nexus.be.repository.ProductoRepository;
import net.hwongu.nexus.be.service.ProductoService;
import net.hwongu.nexus.be.util.RutaHttpUtil;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

/**
 * Expone endpoints REST para productos.
 *
 * @author Henry Wong
 * GitHub @hwongu
 * https://github.com/hwongu
 */
public class ProductoController implements HttpHandler {

    private final ProductoService productoService;
    private final Gson gson;

    public ProductoController() {
        this.productoService = new ProductoService(new ProductoRepository());
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
                        Producto producto = productoService.buscarProductoPorId(id);
                        if (producto != null) {
                            response = gson.toJson(toDTO(producto));
                        } else {
                            statusCode = 404;
                            response = gson.toJson(Map.of("error", "Producto no encontrado"));
                        }
                    } else {
                        List<Producto> productos = productoService.listarProductos();
                        List<ProductoDTO> dtos = productos.stream().map(this::toDTO).toList();
                        response = gson.toJson(dtos);
                    }
                    break;

                case "POST":
                    try (InputStreamReader reader = new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8)) {
                        ProductoDTO dto = gson.fromJson(reader, ProductoDTO.class);
                        Producto producto = toEntity(dto);
                        Producto productoCreado = productoService.registrarProducto(producto);
                        statusCode = 201;
                        response = gson.toJson(toDTO(productoCreado));
                    }
                    break;

                case "PUT":
                    if (path.matches("/\\d+")) {
                        Integer id = RutaHttpUtil.obtenerUltimoSegmentoComoEntero(path);
                        try (InputStreamReader reader = new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8)) {
                            ProductoDTO dto = gson.fromJson(reader, ProductoDTO.class);
                            dto.setIdProducto(id);
                            Producto producto = toEntity(dto);
                            productoService.actualizarProducto(producto);
                            response = gson.toJson(Map.of("message", "Producto actualizado exitosamente"));
                        }
                    } else {
                        statusCode = 400;
                        response = gson.toJson(Map.of("error", "Se requiere un ID de producto en la URL para actualizar"));
                    }
                    break;

                case "DELETE":
                    if (path.matches("/\\d+")) {
                        Integer id = RutaHttpUtil.obtenerUltimoSegmentoComoEntero(path);
                        productoService.eliminarProducto(id);
                        statusCode = 204;
                        response = "";
                    } else {
                        statusCode = 400;
                        response = gson.toJson(Map.of("error", "Se requiere un ID de producto en la URL para eliminar"));
                    }
                    break;

                default:
                    statusCode = 405;
                    response = gson.toJson(Map.of("error", "Metodo HTTP no permitido"));
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

    private ProductoDTO toDTO(Producto producto) {
        return new ProductoDTO(
                producto.getIdProducto(),
                producto.getCategoria().getIdCategoria(),
                producto.getCategoria().getNombre(),
                producto.getNombre(),
                producto.getPrecio(),
                producto.getStock()
        );
    }

    private Producto toEntity(ProductoDTO dto) {
        Categoria categoria = new Categoria(dto.getIdCategoria());
        return new Producto(
                dto.getIdProducto(),
                categoria,
                dto.getNombre(),
                dto.getPrecio(),
                dto.getStock()
        );
    }
}
