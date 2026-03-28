package net.hwongu.nexus.be.controller;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import net.hwongu.nexus.be.dto.UsuarioDTO;
import net.hwongu.nexus.be.entity.Usuario;
import net.hwongu.nexus.be.exception.DataIntegrityViolationException;
import net.hwongu.nexus.be.repository.UsuarioRepository;
import net.hwongu.nexus.be.service.UsuarioService;
import net.hwongu.nexus.be.util.RutaHttpUtil;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

/**
 * Expone endpoints REST para usuarios.
 *
 * @author Henry Wong
 * GitHub @hwongu
 * https://github.com/hwongu
 */
public class UsuarioController implements HttpHandler {

    private final UsuarioService usuarioService;
    private final Gson gson;

    public UsuarioController() {
        this.usuarioService = new UsuarioService(new UsuarioRepository());
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
                        Usuario usuario = usuarioService.buscarUsuarioPorId(id);
                        if (usuario != null) {
                            response = gson.toJson(toDTO(usuario));
                        } else {
                            statusCode = 404;
                            response = gson.toJson(Map.of("error", "Usuario no encontrado"));
                        }
                    } else {
                        List<Usuario> usuarios = usuarioService.listarUsuarios();
                        List<UsuarioDTO> dtos = usuarios.stream().map(this::toDTO).toList();
                        response = gson.toJson(dtos);
                    }
                    break;

                case "POST":
                    if ("/login".equals(path)) {
                        handleLogin(exchange);
                        return;
                    } else {
                        try (InputStreamReader reader = new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8)) {
                            Usuario usuario = gson.fromJson(reader, Usuario.class);
                            Usuario usuarioCreado = usuarioService.registrarUsuario(usuario);
                            statusCode = 201;
                            response = gson.toJson(toDTO(usuarioCreado));
                        }
                    }
                    break;

                case "PUT":
                    if (path.matches("/\\d+")) {
                        Integer id = RutaHttpUtil.obtenerUltimoSegmentoComoEntero(path);
                        try (InputStreamReader reader = new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8)) {
                            Usuario usuario = gson.fromJson(reader, Usuario.class);
                            usuario.setIdUsuario(id);
                            usuarioService.actualizarUsuario(usuario);
                            response = gson.toJson(Map.of("message", "Usuario actualizado exitosamente"));
                        }
                    } else {
                        statusCode = 400;
                        response = gson.toJson(Map.of("error", "Se requiere un ID de usuario en la URL para actualizar"));
                    }
                    break;

                case "DELETE":
                    if (path.matches("/\\d+")) {
                        Integer id = RutaHttpUtil.obtenerUltimoSegmentoComoEntero(path);
                        usuarioService.eliminarUsuario(id);
                        statusCode = 204;
                        response = "";
                    } else {
                        statusCode = 400;
                        response = gson.toJson(Map.of("error", "Se requiere un ID de usuario en la URL para eliminar"));
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

    private void handleLogin(HttpExchange exchange) throws IOException {
        String response;
        int statusCode;

        try (InputStreamReader reader = new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8)) {

            LoginRequest loginRequest = gson.fromJson(reader, LoginRequest.class);
            Usuario usuario = usuarioService.autenticarUsuario(loginRequest.getUsername(), loginRequest.getPassword());

            if (usuario != null) {
                statusCode = 200;
                response = gson.toJson(toDTO(usuario));
            } else {
                statusCode = 401;
                response = gson.toJson(Map.of("error", "Credenciales invalidas"));
            }
        }

        byte[] responseBytes = response.getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(statusCode, responseBytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(responseBytes);
        }
    }

    private UsuarioDTO toDTO(Usuario usuario) {
        return new UsuarioDTO(
                usuario.getIdUsuario(),
                usuario.getUsername(),
                usuario.getEstado()
        );
    }

    private static class LoginRequest {
        private String username;
        private String password;

        public String getUsername() {
            return username;
        }

        public String getPassword() {
            return password;
        }
    }
}
