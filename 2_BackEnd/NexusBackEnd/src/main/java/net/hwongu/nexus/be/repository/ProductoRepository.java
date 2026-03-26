package net.hwongu.nexus.be.repository;

import net.hwongu.nexus.be.config.ConexionDB;
import net.hwongu.nexus.be.entity.Categoria;
import net.hwongu.nexus.be.entity.Producto;
import net.hwongu.nexus.be.exception.DataIntegrityViolationException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Repositorio para la entidad {@link Producto}.
 * Implementa las operaciones CRUD para los productos, manejando la relación
 * con la entidad {@link Categoria} al leer los datos de la base de datos.
 *
 * @author Henry Wong (hwongu@gmail.com)
 */
public class ProductoRepository implements CrudRepository<Producto> {

    @Override
    public Producto insertar(Producto producto) throws SQLException {
        Connection cn = ConexionDB.getInstancia();
        String sql = "INSERT INTO producto (id_categoria, nombre, precio, stock) VALUES (?, ?, ?, ?)";
        try {
            cn.setAutoCommit(false);
            try (PreparedStatement ps = cn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                ps.setInt(1, producto.getCategoria().getIdCategoria());
                ps.setString(2, producto.getNombre());
                ps.setDouble(3, producto.getPrecio());
                ps.setInt(4, producto.getStock());
                ps.executeUpdate();
                
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        producto.setIdProducto(generatedKeys.getInt(1));
                    } else {
                        throw new SQLException("Fallo al crear el producto, no se obtuvo el ID.");
                    }
                }
                cn.commit();
                System.out.println("[INFO] Producto registrado y transacción confirmada.");
            }
        } catch (SQLException e) {
            if (cn != null) {
                cn.rollback();
                System.err.println("[ERROR] Error al insertar producto. Se realizó rollback.");
            }
            throw e;
        } finally {
            if (cn != null && !cn.isClosed()) {
                cn.close();
                System.out.println("[INFO] Conexión cerrada.");
            }
        }
        return producto;
    }

    @Override
    public void actualizar(Producto producto) throws SQLException {
        Connection cn = ConexionDB.getInstancia();
        String sql = "UPDATE producto SET id_categoria = ?, nombre = ?, precio = ?, stock = ? WHERE id_producto = ?";
        try {
            cn.setAutoCommit(false);
            try (PreparedStatement ps = cn.prepareStatement(sql)) {
                ps.setInt(1, producto.getCategoria().getIdCategoria());
                ps.setString(2, producto.getNombre());
                ps.setDouble(3, producto.getPrecio());
                ps.setInt(4, producto.getStock());
                ps.setInt(5, producto.getIdProducto());
                ps.executeUpdate();
                cn.commit();
                System.out.println("[INFO] Producto actualizado exitosamente.");
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
        String sql = "DELETE FROM producto WHERE id_producto = ?";
        try {
            cn.setAutoCommit(false);
            try (PreparedStatement ps = cn.prepareStatement(sql)) {
                ps.setInt(1, id);
                ps.executeUpdate();
                cn.commit();
                System.out.println("[INFO] Producto eliminado.");
            }
        } catch (SQLException e) {
            if (cn != null) cn.rollback();
            // Verificamos el SQLState. '23503' es un código estándar para violación de clave foránea.
            if ("23503".equals(e.getSQLState())) {
                throw new DataIntegrityViolationException("No se puede eliminar el producto porque está referenciado en un ingreso.", e);
            }
            throw e; // Volvemos a lanzar otros errores de SQL.
        } finally {
            if (cn != null && !cn.isClosed()) cn.close();
        }
    }

    @Override
    public List<Producto> listar() throws SQLException {
        List<Producto> productos = new ArrayList<>();
        String sql = """
                SELECT p.id_producto, p.nombre, p.precio, p.stock, 
                       c.id_categoria, c.nombre as cat_nombre, c.descripcion as cat_desc
                FROM producto p
                INNER JOIN categoria c ON p.id_categoria = c.id_categoria
                ORDER BY p.id_producto
                """;
        Connection cn = ConexionDB.getInstancia();
        try (Statement st = cn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Categoria cat = new Categoria(
                        rs.getInt("id_categoria"),
                        rs.getString("cat_nombre"),
                        rs.getString("cat_desc")
                );
                productos.add(new Producto(
                        rs.getInt("id_producto"),
                        cat,
                        rs.getString("nombre"),
                        rs.getDouble("precio"),
                        rs.getInt("stock")
                ));
            }
        } finally {
            if (cn != null && !cn.isClosed()) cn.close();
        }
        return productos;
    }

    @Override
    public Producto buscarPorId(Integer id) throws SQLException {
        String sql = """
                SELECT p.id_producto, p.nombre, p.precio, p.stock, 
                       c.id_categoria, c.nombre as cat_nombre, c.descripcion as cat_desc
                FROM producto p
                INNER JOIN categoria c ON p.id_categoria = c.id_categoria
                WHERE p.id_producto = ?
                """;
        Connection cn = ConexionDB.getInstancia();
        Producto producto = null;
        try (PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Categoria cat = new Categoria(
                            rs.getInt("id_categoria"),
                            rs.getString("cat_nombre"),
                            rs.getString("cat_desc")
                    );
                    producto = new Producto(
                            rs.getInt("id_producto"),
                            cat,
                            rs.getString("nombre"),
                            rs.getDouble("precio"),
                            rs.getInt("stock")
                    );
                }
            }
        } finally {
            if (cn != null && !cn.isClosed()) cn.close();
        }
        return producto;
    }
}
