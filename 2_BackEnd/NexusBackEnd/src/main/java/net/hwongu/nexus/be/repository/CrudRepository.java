package net.hwongu.nexus.be.repository;

import java.sql.SQLException;
import java.util.List;

/**
 * Define operaciones CRUD para acceso a datos.
 *
 * @author Henry Wong
 * GitHub @hwongu
 * https://github.com/hwongu
 */
public interface CrudRepository<T> {

    T insertar(T entidad) throws SQLException;

    void actualizar(T entidad) throws SQLException;

    void eliminar(Integer id) throws SQLException;

    List<T> listar() throws SQLException;

    T buscarPorId(Integer id) throws SQLException;
}
