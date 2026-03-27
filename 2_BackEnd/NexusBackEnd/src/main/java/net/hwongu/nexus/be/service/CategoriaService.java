package net.hwongu.nexus.be.service;

import net.hwongu.nexus.be.entity.Categoria;
import net.hwongu.nexus.be.repository.CategoriaRepository;

import java.sql.SQLException;
import java.util.List;

/**
 * Capa de servicio para la gestión de la entidad {@link Categoria}.
 * Esta clase encapsula la lógica de negocio relacionada con las categorías,
 * actuando como intermediario entre los controladores y el repositorio.
 * Maneja las excepciones de la capa de persistencia y las convierte en
 * excepciones de tiempo de ejecución.
 *
 * @author Henry Wong (hwongu@gmail.com)
 */
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;

    /**
     * Constructor que inyecta la dependencia del repositorio de categorías.
     * @param categoriaRepository La instancia del repositorio a utilizar.
     */
    public CategoriaService(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    /**
     * Registra una nueva categoría en el sistema y la devuelve con su ID.
     *
     * @param categoria El objeto {@link Categoria} a registrar.
     * @return La entidad {@link Categoria} persistida con su ID autogenerado.
     * @throws RuntimeException Si ocurre un error de acceso a la base de datos durante la inserción.
     */
    public Categoria registrarCategoria(Categoria categoria) {
        try {
            return categoriaRepository.insertar(categoria);
        } catch (SQLException e) {
            throw new RuntimeException("Error al registrar la categoría: " + e.getMessage(), e);
        }
    }

    /**
     * Actualiza los datos de una categoría existente.
     *
     * @param categoria El objeto {@link Categoria} con la información a actualizar.
     * @throws RuntimeException Si ocurre un error de acceso a la base de datos durante la actualización.
     */
    public void actualizarCategoria(Categoria categoria) {
        try {
            categoriaRepository.actualizar(categoria);
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar la categoría: " + e.getMessage(), e);
        }
    }

    /**
     * Elimina una categoría del sistema por su identificador único.
     *
     * @param id El ID de la categoría a eliminar.
     * @throws RuntimeException Si ocurre un error de acceso a la base de datos durante la eliminación.
     */
    public void eliminarCategoria(Integer id) {
        try {
            categoriaRepository.eliminar(id);
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar la categoría: " + e.getMessage(), e);
        }
    }

    /**
     * Obtiene una lista de todas las categorías registradas.
     *
     * @return Una {@link List} de objetos {@link Categoria}.
     * @throws RuntimeException Si ocurre un error de acceso a la base de datos durante la consulta.
     */
    public List<Categoria> listarCategorias() {
        try {
            return categoriaRepository.listar();
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar las categorías: " + e.getMessage(), e);
        }
    }

    /**
     * Busca y recupera una categoría por su identificador único.
     *
     * @param id El ID de la categoría a buscar.
     * @return El objeto {@link Categoria} si se encuentra; de lo contrario, {@code null}.
     * @throws RuntimeException Si ocurre un error de acceso a la base de datos durante la búsqueda.
     */
    public Categoria buscarCategoriaPorId(Integer id) {
        try {
            return categoriaRepository.buscarPorId(id);
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar la categoría: " + e.getMessage(), e);
        }
    }
}
