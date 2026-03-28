package net.hwongu.nexus.be.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import net.hwongu.nexus.be.dto.DetalleIngresoDTO;
import net.hwongu.nexus.be.dto.IngresoDTO;
import net.hwongu.nexus.be.entity.DetalleIngreso;
import net.hwongu.nexus.be.entity.Ingreso;
import net.hwongu.nexus.be.entity.Producto;
import net.hwongu.nexus.be.entity.Usuario;
import net.hwongu.nexus.be.repository.IngresoRepository;
import net.hwongu.nexus.be.service.IngresoService;
import net.hwongu.nexus.be.util.LocalDateAdapter;
import net.hwongu.nexus.be.util.RutaHttpUtil;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Expone endpoints REST para ingresos.
 *
 * @author Henry Wong
 * GitHub @hwongu
 * https://github.com/hwongu
 */
public class IngresoController implements HttpHandler {

    private final IngresoService ingresoService;
    private final Gson gson;

    public IngresoController() {
        this.ingresoService = new IngresoService(new IngresoRepository());
        this.gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateAdapter())
                .create();
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
                    if (path.matches("/\\d+/detalles")) {
                        Integer id = RutaHttpUtil.obtenerPrimerSegmentoComoEntero(path);
                        List<DetalleIngreso> detalles = ingresoService.buscarDetallesPorIngreso(id);
                        List<DetalleIngresoDTO> dtos = detalles.stream().map(this::toDetalleDTO).toList();
                        response = gson.toJson(dtos);
                    } else {
                        List<Ingreso> ingresos = ingresoService.listarIngresos();
                        List<IngresoDTO> dtos = ingresos.stream().map(this::toIngresoDTO).toList();
                        response = gson.toJson(dtos);
                    }
                    break;

                case "POST":
                    try (InputStreamReader reader = new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8)) {
                        IngresoConDetallesRequest request = gson.fromJson(reader, IngresoConDetallesRequest.class);
                        Ingreso ingreso = toIngresoEntity(request.getIngreso());
                        List<DetalleIngreso> detalles = request.getDetalles().stream().map(this::toDetalleEntity).toList();

                        ingresoService.registrarIngresoCompleto(ingreso, detalles);
                        statusCode = 201;
                        response = gson.toJson(Map.of("message", "Ingreso registrado exitosamente"));
                    }
                    break;

                case "PUT":
                    if (path.matches("/\\d+/estado")) {
                        Integer id = RutaHttpUtil.obtenerPrimerSegmentoComoEntero(path);
                        try (InputStreamReader reader = new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8)) {
                            Map<String, String> requestBody = gson.fromJson(reader, Map.class);
                            String nuevoEstado = requestBody.get("estado");

                            if (nuevoEstado == null || nuevoEstado.trim().isEmpty()) {
                                statusCode = 400;
                                response = gson.toJson(Map.of("error", "El campo 'estado' es requerido"));
                            } else {
                                ingresoService.actualizarEstadoIngreso(id, nuevoEstado);
                                response = gson.toJson(Map.of("message", "Estado del ingreso actualizado correctamente"));
                            }
                        }
                    } else {
                        statusCode = 400;
                        response = gson.toJson(Map.of("error", "URL no valida para actualizar estado. Use /api/ingresos/{id}/estado"));
                    }
                    break;

                case "DELETE":
                    if (path.matches("/\\d+")) {
                        Integer id = RutaHttpUtil.obtenerUltimoSegmentoComoEntero(path);
                        ingresoService.anularIngreso(id);
                        statusCode = 200;
                        response = gson.toJson(Map.of("message", "Ingreso anulado exitosamente"));
                    } else {
                        statusCode = 400;
                        response = gson.toJson(Map.of("error", "Se requiere un ID de ingreso en la URL para anular"));
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

        } catch (Exception e) {
            String errorResponse = gson.toJson(Map.of("error", "Error interno del servidor: " + e.getMessage()));
            byte[] responseBytes = errorResponse.getBytes(StandardCharsets.UTF_8);
            exchange.sendResponseHeaders(500, responseBytes.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(responseBytes);
            }
        }
    }

    private static class IngresoConDetallesRequest {
        private IngresoDTO ingreso;
        private List<DetalleIngresoDTO> detalles;

        public IngresoDTO getIngreso() { return ingreso; }

        public List<DetalleIngresoDTO> getDetalles() { return detalles; }
    }

    private IngresoDTO toIngresoDTO(Ingreso ingreso) {
        return new IngresoDTO(
                ingreso.getIdIngreso(),
                ingreso.getUsuario().getIdUsuario(),
                ingreso.getUsuario().getUsername(),
                ingreso.getFechaIngreso(),
                ingreso.getEstado()
        );
    }

    private DetalleIngresoDTO toDetalleDTO(DetalleIngreso detalle) {
        return new DetalleIngresoDTO(
                detalle.getIdDetalle(),
                detalle.getIngreso().getIdIngreso(),
                detalle.getProducto().getIdProducto(),
                detalle.getProducto().getNombre(),
                detalle.getCantidad(),
                detalle.getPrecioCompra()
        );
    }

    private Ingreso toIngresoEntity(IngresoDTO dto) {
        Ingreso ingreso = new Ingreso();
        ingreso.setUsuario(new Usuario(dto.getIdUsuario()));
        ingreso.setFechaIngreso(dto.getFechaIngreso() != null ? dto.getFechaIngreso() : LocalDateTime.now());
        ingreso.setEstado(dto.getEstado());
        return ingreso;
    }

    private DetalleIngreso toDetalleEntity(DetalleIngresoDTO dto) {
        DetalleIngreso detalle = new DetalleIngreso();
        detalle.setProducto(new Producto(dto.getIdProducto()));
        detalle.setCantidad(dto.getCantidad());
        detalle.setPrecioCompra(dto.getPrecioCompra());
        return detalle;
    }
}
