package net.hwongu.nexus.be.service;

import net.hwongu.nexus.be.entity.Usuario;
import net.hwongu.nexus.be.repository.UsuarioRepository;

import java.sql.SQLException;
import java.util.List;

/**
 * Coordina la logica de negocio de usuarios.
 *
 * @author Henry Wong
 * GitHub @hwongu
 * https://github.com/hwongu
 */
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public Usuario registrarUsuario(Usuario usuario) {
        try {
            return usuarioRepository.insertar(usuario);
        } catch (SQLException e) {
            throw new RuntimeException("Error al registrar el usuario: " + e.getMessage(), e);
        }
    }

    public void actualizarUsuario(Usuario usuario) {
        try {
            usuarioRepository.actualizar(usuario);
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar el usuario: " + e.getMessage(), e);
        }
    }

    public void eliminarUsuario(Integer id) {
        try {
            usuarioRepository.eliminar(id);
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar el usuario: " + e.getMessage(), e);
        }
    }

    public List<Usuario> listarUsuarios() {
        try {
            return usuarioRepository.listar();
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar los usuarios: " + e.getMessage(), e);
        }
    }

    public Usuario buscarUsuarioPorId(Integer id) {
        try {
            return usuarioRepository.buscarPorId(id);
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar el usuario: " + e.getMessage(), e);
        }
    }

    public Usuario autenticarUsuario(String username, String password) {
        try {
            return usuarioRepository.buscarPorCredenciales(username, password);
        } catch (SQLException e) {
            throw new RuntimeException("Error durante la autenticación: " + e.getMessage(), e);
        }
    }
}
