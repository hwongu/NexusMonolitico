package net.hwongu.nexus.be.repository;

import net.hwongu.nexus.be.config.ConexionDB;
import net.hwongu.nexus.be.entity.Usuario;
import net.hwongu.nexus.be.exception.DataIntegrityViolationException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Repositorio para la entidad {@link Usuario}.
 * Proporciona una implementación de {@link CrudRepository} para gestionar
 * la persistencia de los objetos Usuario en la base de datos.
 *
 * @author Henry Wong (hwongu@gmail.com)
 */
public class UsuarioRepository implements CrudRepository<Usuario> {

    @Override
    public Usuario insertar(Usuario usuario) throws SQLException {
        Connection cn = ConexionDB.getInstancia();
        String sql = "INSERT INTO usuario (username, password, estado) VALUES (?, ?, ?)";
        try {
            cn.setAutoCommit(false);
            try (PreparedStatement ps = cn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, usuario.getUsername());
                ps.setString(2, usuario.getPassword());
                ps.setBoolean(3, usuario.getEstado());
                ps.executeUpdate();

                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        usuario.setIdUsuario(generatedKeys.getInt(1));
                    } else {
                        throw new SQLException("Fallo al crear el usuario, no se obtuvo el ID.");
                    }
                }
                cn.commit();
                System.out.println("[INFO] Usuario insertado y transacción confirmada.");
            }
        } catch (SQLException e) {
            if (cn != null) {
                cn.rollback();
                System.err.println("[ERROR] Error al insertar usuario. Se realizó rollback.");
            }
            throw e;
        } finally {
            if (cn != null && !cn.isClosed()) {
                cn.close();
                System.out.println("[INFO] Conexión cerrada.");
            }
        }
        return usuario;
    }

    @Override
    public void actualizar(Usuario usuario) throws SQLException {
        Connection cn = ConexionDB.getInstancia();
        String sql = "UPDATE usuario SET username = ?, password = ?, estado = ? WHERE id_usuario = ?";
        try {
            cn.setAutoCommit(false);
            try (PreparedStatement ps = cn.prepareStatement(sql)) {
                ps.setString(1, usuario.getUsername());
                ps.setString(2, usuario.getPassword());
                ps.setBoolean(3, usuario.getEstado());
                ps.setInt(4, usuario.getIdUsuario());
                ps.executeUpdate();
                cn.commit();
                System.out.println("[INFO] Usuario actualizado exitosamente.");
            }
        } catch (SQLException e) {
            if (cn != null) cn.rollback();
            throw e;
        } finally {
            if (cn != null && !cn.isClosed()) cn.close();
        }
    }

    @Override
    public void eliminar(Integer id) throws SQLException {
        Connection cn = ConexionDB.getInstancia();
        String sql = "DELETE FROM usuario WHERE id_usuario = ?";
        try {
            cn.setAutoCommit(false);
            try (PreparedStatement ps = cn.prepareStatement(sql)) {
                ps.setInt(1, id);
                ps.executeUpdate();
                cn.commit();
                System.out.println("[INFO] Usuario eliminado de la base de datos.");
            }
        } catch (SQLException e) {
            if (cn != null) cn.rollback();
            // Verificamos el SQLState. '23503' es un código estándar para violación de clave foránea.
            if ("23503".equals(e.getSQLState())) {
                throw new DataIntegrityViolationException("No se puede eliminar el usuario porque tiene ingresos registrados a su nombre.", e);
            }
            throw e; // Volvemos a lanzar otros errores de SQL.
        } finally {
            if (cn != null && !cn.isClosed()) cn.close();
        }
    }

    @Override
    public List<Usuario> listar() throws SQLException {
        List<Usuario> usuarios = new ArrayList<>();
        String sql = "SELECT id_usuario, username, password, estado FROM usuario ORDER BY id_usuario";
        Connection cn = ConexionDB.getInstancia();
        try (Statement st = cn.createStatement();
            ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                usuarios.add(new Usuario(
                        rs.getInt("id_usuario"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getBoolean("estado")
                ));
            }
        } finally {
            if (cn != null && !cn.isClosed()) cn.close();
        }
        return usuarios;
    }

    @Override
    public Usuario buscarPorId(Integer id) throws SQLException {
        String sql = "SELECT id_usuario, username, password, estado FROM usuario WHERE id_usuario = ?";
        Connection cn = ConexionDB.getInstancia();
        Usuario usuario = null;
        try (PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    usuario = new Usuario(
                            rs.getInt("id_usuario"),
                            rs.getString("username"),
                            rs.getString("password"),
                            rs.getBoolean("estado")
                    );
                }
            }
        } finally {
            if (cn != null && !cn.isClosed()) cn.close();
        }
        return usuario;
    }

    /**
     * Busca un usuario por su nombre de usuario y contraseña.
     * Este método se utiliza para la autenticación de usuarios.
     *
     * @param username El nombre de usuario.
     * @param password La contraseña del usuario.
     * @return Un objeto {@link Usuario} si las credenciales son correctas, de lo contrario, {@code null}.
     * @throws SQLException Si ocurre un error de acceso a la base de datos.
     */
    public Usuario buscarPorCredenciales(String username, String password) throws SQLException {
        String sql = "SELECT id_usuario, username, password, estado FROM usuario WHERE username = ? AND password = ? and estado = true";
        Connection cn = ConexionDB.getInstancia();
        Usuario usuario = null;
        try (PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, password);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Boolean estado = rs.getObject("estado", Boolean.class);
                    usuario = new Usuario(
                            rs.getInt("id_usuario"),
                            rs.getString("username"),
                            rs.getString("password"),
                            Boolean.TRUE.equals(estado)
                    );
                }
            }
        } finally {
            if (cn != null && !cn.isClosed()) cn.close();
        }
        return usuario;
    }
}
