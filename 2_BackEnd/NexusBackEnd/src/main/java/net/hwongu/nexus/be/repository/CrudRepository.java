package net.hwongu.nexus.be.repository;

import java.sql.SQLException;
import java.util.List;

/**
 * Interfaz genérica que define el contrato estándar para operaciones CRUD (Create, Read, Update, Delete).
 * Esta interfaz está diseñada para ser implementada por los repositorios de cada entidad,
 * promoviendo un patrón de diseño consistente a lo largo de la capa de persistencia.
 *
 * @param <T> El tipo de la entidad que el repositorio manejará.
 * @author Henry Wong (hwongu@gmail.com)
 */
public interface CrudRepository<T> {

    /**
     * Inserta una nueva entidad en la base de datos y la devuelve con su ID generado.
     *
     * @param entidad El objeto de la entidad a ser persistido.
     * @return La entidad persistida, incluyendo el ID autogenerado por la base de datos.
     * @throws SQLException Si ocurre un error de acceso a la base de datos.
     */
    T insertar(T entidad) throws SQLException;

    /**
     * Actualiza una entidad existente en la base de datos.
     *
     * @param entidad El objeto de la entidad con los datos actualizados.
     * @throws SQLException Si ocurre un error de acceso a la base de datos.
     */
    void actualizar(T entidad) throws SQLException;

    /**
     * Elimina una entidad de la base de datos utilizando su identificador único.
     *
     * @param id El identificador único de la entidad a eliminar.
     * @throws SQLException Si ocurre un error de acceso a la base de datos.
     */
    void eliminar(Integer id) throws SQLException;

    /**
     * Recupera una lista de todas las entidades de un tipo específico.
     *
     * @return Una {@link List} de objetos de la entidad. La lista puede estar vacía si no hay registros.
     * @throws SQLException Si ocurre un error de acceso a la base de datos.
     */
    List<T> listar() throws SQLException;

    /**
     * Busca y recupera una entidad por su identificador único.
     *
     * @param id El identificador único de la entidad a buscar.
     * @return El objeto de la entidad si se encuentra; de lo contrario, {@code null}.
     * @throws SQLException Si ocurre un error de acceso a la base de datos.
     */
    T buscarPorId(Integer id) throws SQLException;
}
