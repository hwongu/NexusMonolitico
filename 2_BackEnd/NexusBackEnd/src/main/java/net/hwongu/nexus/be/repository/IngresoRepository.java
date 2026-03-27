package net.hwongu.nexus.be.repository;

import net.hwongu.nexus.be.config.ConexionDB;
import net.hwongu.nexus.be.entity.DetalleIngreso;
import net.hwongu.nexus.be.entity.Ingreso;
import net.hwongu.nexus.be.entity.Producto;
import net.hwongu.nexus.be.entity.Usuario;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Repositorio para la gestión de las entidades {@link Ingreso} y {@link DetalleIngreso}.
 * Esta clase maneja operaciones complejas y transaccionales que involucran
 * múltiples tablas, como el registro completo de un ingreso y su anulación.
 *
 * @author Henry Wong (hwongu@gmail.com)
 */
public class IngresoRepository {

    /**
     * Registra un ingreso completo de forma transaccional.
     * Inserta la cabecera del ingreso, sus detalles, y actualiza el stock
     * de los productos correspondientes. Si alguna operación falla, se
     * revierte toda la transacción.
     *
     * @param ingreso El objeto {@link Ingreso} con los datos de la cabecera.
     * @param detalles Una lista de {@link DetalleIngreso} con los productos a ingresar.
     * @throws SQLException Si ocurre un error de acceso a la base de datos.
     */
    public void registrarIngresoCompleto(Ingreso ingreso, List<DetalleIngreso> detalles) throws SQLException {
        Connection cn = ConexionDB.getInstancia();
        String sqlIngreso = "INSERT INTO ingreso (id_usuario, fecha_ingreso, estado) VALUES (?, ?, ?)";
        String sqlDetalle = "INSERT INTO detalle_ingreso (id_ingreso, id_producto, cantidad, precio_compra) VALUES (?, ?, ?, ?)";
        String sqlUpdateStock = "UPDATE producto SET stock = stock + ? WHERE id_producto = ?";
        try {
            cn.setAutoCommit(false);
            int idGenerado = 0;
            try (PreparedStatement psI = cn.prepareStatement(sqlIngreso, Statement.RETURN_GENERATED_KEYS)) {
                psI.setInt(1, ingreso.getUsuario().getIdUsuario());
                psI.setTimestamp(2, Timestamp.valueOf(ingreso.getFechaIngreso()));
                psI.setString(3, ingreso.getEstado());
                psI.executeUpdate();
                try (ResultSet rs = psI.getGeneratedKeys()) {
                    if (rs.next()) idGenerado = rs.getInt(1);
                }
            }
            try (PreparedStatement psD = cn.prepareStatement(sqlDetalle);
                 PreparedStatement psS = cn.prepareStatement(sqlUpdateStock)) {
                for (DetalleIngreso det : detalles) {
                    psD.setInt(1, idGenerado);
                    psD.setInt(2, det.getProducto().getIdProducto());
                    psD.setInt(3, det.getCantidad());
                    psD.setDouble(4, det.getPrecioCompra());
                    psD.addBatch();

                    psS.setInt(1, det.getCantidad());
                    psS.setInt(2, det.getProducto().getIdProducto());
                    psS.addBatch();
                }
                psD.executeBatch();
                psS.executeBatch();
            }

            cn.commit();
            System.out.println("[INFO] Transacción de ingreso realizada con éxito.");

        } catch (SQLException e) {
            if (cn != null) cn.rollback();
            throw e;
        } finally {
            if (cn != null && !cn.isClosed()) cn.close();
        }
    }

    /**
     * Lista todos los ingresos registrados, incluyendo datos del usuario que lo realizó.
     *
     * @return Una lista de objetos {@link Ingreso}.
     * @throws SQLException Si ocurre un error de acceso a la base de datos.
     */
    public List<Ingreso> listar() throws SQLException {
        List<Ingreso> ingresos = new ArrayList<>();
        String sql = """
            SELECT i.id_ingreso, i.fecha_ingreso, i.estado, 
                   u.id_usuario, u.username 
            FROM ingreso i 
            INNER JOIN usuario u ON i.id_usuario = u.id_usuario 
            ORDER BY i.id_ingreso DESC
            """;

        Connection cn = ConexionDB.getInstancia();
        try (Statement st = cn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Usuario user = new Usuario(rs.getInt("id_usuario"));
                user.setUsername(rs.getString("username"));

                ingresos.add(new Ingreso(
                        rs.getInt("id_ingreso"),
                        user,
                        rs.getTimestamp("fecha_ingreso").toLocalDateTime(),
                        rs.getString("estado")
                ));
            }
        } finally {
            if (cn != null && !cn.isClosed()) cn.close();
        }
        return ingresos;
    }

    /**
     * Busca y recupera todos los detalles asociados a un ingreso específico.
     *
     * @param idIngreso El ID del ingreso del cual se quieren obtener los detalles.
     * @return Una lista de objetos {@link DetalleIngreso}.
     * @throws SQLException Si ocurre un error de acceso a la base de datos.
     */
    public List<DetalleIngreso> buscarDetallesPorIngreso(Integer idIngreso) throws SQLException {
        List<DetalleIngreso> detalles = new ArrayList<>();
        String sql = """
            SELECT d.id_detalle, d.cantidad, d.precio_compra, 
                   p.id_producto, p.nombre as prod_nombre
            FROM detalle_ingreso d
            INNER JOIN producto p ON d.id_producto = p.id_producto
            WHERE d.id_ingreso = ?
            """;

        Connection cn = ConexionDB.getInstancia();
        try (PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, idIngreso);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Producto prod = new Producto(rs.getInt("id_producto"));
                    prod.setNombre(rs.getString("prod_nombre"));

                    detalles.add(new DetalleIngreso(
                            rs.getInt("id_detalle"),
                            new Ingreso(idIngreso),
                            prod,
                            rs.getInt("cantidad"),
                            rs.getDouble("precio_compra")
                    ));
                }
            }
        } finally {
            if (cn != null && !cn.isClosed()) cn.close();
        }
        return detalles;
    }

    /**
     * Anula un ingreso de forma transaccional.
     * Cambia el estado del ingreso a 'ANULADO' y revierte el stock de los
     * productos involucrados, restando las cantidades que se habían sumado.
     *
     * @param idIngreso El ID del ingreso a anular.
     * @throws SQLException Si ocurre un error de acceso a la base de datos o si el ingreso no se encuentra.
     */
    public void anularIngreso(Integer idIngreso) throws SQLException {
        Connection cn = ConexionDB.getInstancia();
        String sqlAnular = "UPDATE ingreso SET estado = 'ANULADO' WHERE id_ingreso = ?";
        String sqlConsultarDetalles = "SELECT id_producto, cantidad FROM detalle_ingreso WHERE id_ingreso = ?";
        String sqlRestarStock = "UPDATE producto SET stock = stock - ? WHERE id_producto = ?";

        try {
            cn.setAutoCommit(false);

            try (PreparedStatement psA = cn.prepareStatement(sqlAnular)) {
                psA.setInt(1, idIngreso);
                int filasAfectadas = psA.executeUpdate();
                if (filasAfectadas == 0) {
                    throw new SQLException("No se encontró el ingreso con ID: " + idIngreso);
                }
            }

            List<DetalleIngreso> detallesParaRevertir = new ArrayList<>();
            try (PreparedStatement psC = cn.prepareStatement(sqlConsultarDetalles)) {
                psC.setInt(1, idIngreso);
                try (ResultSet rs = psC.executeQuery()) {
                    while (rs.next()) {
                        DetalleIngreso d = new DetalleIngreso();
                        d.setProducto(new Producto(rs.getInt("id_producto")));
                        d.setCantidad(rs.getInt("cantidad"));
                        detallesParaRevertir.add(d);
                    }
                }
            }

            try (PreparedStatement psS = cn.prepareStatement(sqlRestarStock)) {
                for (DetalleIngreso det : detallesParaRevertir) {
                    psS.setInt(1, det.getCantidad());
                    psS.setInt(2, det.getProducto().getIdProducto());
                    psS.addBatch();
                }
                psS.executeBatch();
            }

            cn.commit();
            System.out.println("[INFO] Ingreso anulado y stock revertido correctamente.");

        } catch (SQLException e) {
            if (cn != null) {
                cn.rollback();
                System.err.println("[ERROR] No se pudo anular el ingreso. Se realizó rollback.");
            }
            throw e;
        } finally {
            if (cn != null && !cn.isClosed()) {
                cn.close();
                System.out.println("[INFO] Conexión cerrada.");
            }
        }
    }

    /**
     * Actualiza el estado de un ingreso específico.
     *
     * @param idIngreso El ID del ingreso a actualizar.
     * @param nuevoEstado El nuevo estado para el ingreso.
     * @throws SQLException Si ocurre un error de acceso a la base de datos o si el ingreso no se encuentra.
     */
    public void actualizarEstado(Integer idIngreso, String nuevoEstado) throws SQLException {
        String sql = "UPDATE ingreso SET estado = ? WHERE id_ingreso = ?";
        Connection cn = ConexionDB.getInstancia();
        try (PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, nuevoEstado);
            ps.setInt(2, idIngreso);
            int filasAfectadas = ps.executeUpdate();
            if (filasAfectadas == 0) {
                throw new SQLException("No se encontró el ingreso con ID: " + idIngreso);
            }
        } finally {
            if (cn != null && !cn.isClosed()) {
                cn.close();
            }
        }
    }


}
