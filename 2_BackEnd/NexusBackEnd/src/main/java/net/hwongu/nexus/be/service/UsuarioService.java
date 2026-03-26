package net.hwongu.nexus.be.service;

import net.hwongu.nexus.be.entity.Usuario;
import net.hwongu.nexus.be.repository.UsuarioRepository;

import java.sql.SQLException;
import java.util.List;

/**
 * Capa de servicio para la gestión de la entidad {@link Usuario}.
 * Contiene la lógica de negocio para las operaciones CRUD de usuarios y
 * actúa como intermediario entre los controladores y el repositorio de usuarios.
 *
 * @author Henry Wong (hwongu@gmail.com)
 */
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    /**
     * Constructor que inyecta la dependencia del repositorio de usuarios.
     * @param usuarioRepository La instancia del repositorio a utilizar.
     */
    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    /**
     * Registra un nuevo usuario y lo devuelve con su ID.
     *
     * @param usuario El objeto {@link Usuario} a registrar.
     * @return La entidad {@link Usuario} persistida con su ID autogenerado.
     * @throws RuntimeException Si ocurre un error de base de datos durante la inserción.
     */
    public Usuario registrarUsuario(Usuario usuario) {
        try {
            return usuarioRepository.insertar(usuario);
        } catch (SQLException e) {
            throw new RuntimeException("Error al registrar el usuario: " + e.getMessage(), e);
        }
    }

    /**
     * Actualiza un usuario existente.
     *
     * @param usuario El objeto {@link Usuario} con los datos a actualizar.
     * @throws RuntimeException Si ocurre un error de base de datos durante la actualización.
     */
    public void actualizarUsuario(Usuario usuario) {
        try {
            usuarioRepository.actualizar(usuario);
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar el usuario: " + e.getMessage(), e);
        }
    }

    /**
     * Elimina un usuario por su ID.
     *
     * @param id El ID del usuario a eliminar.
     * @throws RuntimeException Si ocurre un error de base de datos durante la eliminación.
     */
    public void eliminarUsuario(Integer id) {
        try {
            usuarioRepository.eliminar(id);
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar el usuario: " + e.getMessage(), e);
        }
    }

    /**
     * Obtiene una lista de todos los usuarios.
     *
     * @return Una {@link List} de objetos {@link Usuario}.
     * @throws RuntimeException Si ocurre un error de base de datos durante la consulta.
     */
    public List<Usuario> listarUsuarios() {
        try {
            return usuarioRepository.listar();
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar los usuarios: " + e.getMessage(), e);
        }
    }

    /**
     * Busca un usuario por su ID.
     *
     * @param id El ID del usuario a buscar.
     * @return El objeto {@link Usuario} si se encuentra; de lo contrario, {@code null}.
     * @throws RuntimeException Si ocurre un error de base de datos durante la búsqueda.
     */
    public Usuario buscarUsuarioPorId(Integer id) {
        try {
            return usuarioRepository.buscarPorId(id);
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar el usuario: " + e.getMessage(), e);
        }
    }

    /**
     * Autentica a un usuario basado en su nombre de usuario y contraseña.
     *
     * @param username El nombre de usuario.
     * @param password La contraseña.
     * @return El objeto {@link Usuario} si la autenticación es exitosa; de lo contrario, {@code null}.
     * @throws RuntimeException Si ocurre un error de acceso a la base de datos.
     */
    public Usuario autenticarUsuario(String username, String password) {
        try {
            return usuarioRepository.buscarPorCredenciales(username, password);
        } catch (SQLException e) {
            throw new RuntimeException("Error durante la autenticación: " + e.getMessage(), e);
        }
    }
}
