package net.hwongu.nexus.be.repository;

import net.hwongu.nexus.be.config.ConexionDB;
import net.hwongu.nexus.be.entity.Categoria;
import net.hwongu.nexus.be.exception.DataIntegrityViolationException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Accede a datos de categorias.
 *
 * @author Henry Wong
 * GitHub @hwongu
 * https://github.com/hwongu
 */
public class CategoriaRepository implements CrudRepository<Categoria> {

    @Override
    public Categoria insertar(Categoria categoria) throws SQLException {
        Connection cn = ConexionDB.getInstancia();
        String sql = "INSERT INTO categoria (nombre, descripcion) VALUES (?, ?)";
        try {
            cn.setAutoCommit(false);
            try (PreparedStatement ps = cn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, categoria.getNombre());
                ps.setString(2, categoria.getDescripcion());
                ps.executeUpdate();

                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        categoria.setIdCategoria(generatedKeys.getInt(1));
                    } else {
                        throw new SQLException("Fallo al crear la categoría, no se obtuvo el ID.");
                    }
                }
                cn.commit();
                System.out.println("[INFO] Inserción exitosa y transacción confirmada.");
            }
        } catch (SQLException e) {
            if (cn != null) {
                cn.rollback();
                System.err.println("[ERROR] Error al insertar. Se realizó rollback de la transacción.");
            }
            throw e;
        } finally {
            if (cn != null && !cn.isClosed()) {
                cn.close();
                System.out.println("[INFO] Conexión cerrada.");
            }
        }
        return categoria;
    }

    @Override
    public void actualizar(Categoria categoria) throws SQLException {
        Connection cn = ConexionDB.getInstancia();
        String sql = "UPDATE categoria SET nombre = ?, descripcion = ? WHERE id_categoria = ?";
        try {
            cn.setAutoCommit(false);
            try (PreparedStatement ps = cn.prepareStatement(sql)) {
                ps.setString(1, categoria.getNombre());
                ps.setString(2, categoria.getDescripcion());
                ps.setInt(3, categoria.getIdCategoria());
                ps.executeUpdate();
                cn.commit();
                System.out.println("[INFO] Actualización exitosa y transacción confirmada.");
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
        String sql = "DELETE FROM categoria WHERE id_categoria = ?";
        try {
            cn.setAutoCommit(false);
            try (PreparedStatement ps = cn.prepareStatement(sql)) {
                ps.setInt(1, id);
                ps.executeUpdate();
                cn.commit();
                System.out.println("[INFO] Eliminación exitosa.");
            }
        } catch (SQLException e) {
            if (cn != null) cn.rollback();

            if ("23503".equals(e.getSQLState())) {
                throw new DataIntegrityViolationException("No se puede eliminar la categoría porque tiene productos asociados.", e);
            }
            throw e;
        } finally {
            if (cn != null && !cn.isClosed()) cn.close();
        }
    }

    @Override
    public List<Categoria> listar() throws SQLException {
        List<Categoria> lista = new ArrayList<>();
        String sql = "SELECT id_categoria, nombre, descripcion FROM categoria ORDER BY id_categoria";
        Connection cn = ConexionDB.getInstancia();
        try (Statement st = cn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(new Categoria(
                        rs.getInt("id_categoria"),
                        rs.getString("nombre"),
                        rs.getString("descripcion")
                ));
            }
        } finally {
            if (cn != null && !cn.isClosed()) cn.close();
        }
        return lista;
    }

    @Override
    public Categoria buscarPorId(Integer id) throws SQLException {
        String sql = "SELECT id_categoria, nombre, descripcion FROM categoria WHERE id_categoria = ?";
        Connection cn = ConexionDB.getInstancia();
        Categoria categoria = null;
        try (PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    categoria = new Categoria(
                            rs.getInt("id_categoria"),
                            rs.getString("nombre"),
                            rs.getString("descripcion")
                    );
                }
            }
        } finally {
            if (cn != null && !cn.isClosed()) cn.close();
        }
        return categoria;
    }
}
