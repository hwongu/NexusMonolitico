package net.hwongu.nexus.be.service;

import net.hwongu.nexus.be.entity.Categoria;
import net.hwongu.nexus.be.repository.CategoriaRepository;

import java.sql.SQLException;
import java.util.List;

/**
 * Coordina la logica de negocio de categorias.
 *
 * @author Henry Wong
 * GitHub @hwongu
 * https://github.com/hwongu
 */
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;

    public CategoriaService(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    public Categoria registrarCategoria(Categoria categoria) {
        try {
            return categoriaRepository.insertar(categoria);
        } catch (SQLException e) {
            throw new RuntimeException("Error al registrar la categoría: " + e.getMessage(), e);
        }
    }

    public void actualizarCategoria(Categoria categoria) {
        try {
            categoriaRepository.actualizar(categoria);
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar la categoría: " + e.getMessage(), e);
        }
    }

    public void eliminarCategoria(Integer id) {
        try {
            categoriaRepository.eliminar(id);
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar la categoría: " + e.getMessage(), e);
        }
    }

    public List<Categoria> listarCategorias() {
        try {
            return categoriaRepository.listar();
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar las categorías: " + e.getMessage(), e);
        }
    }

    public Categoria buscarCategoriaPorId(Integer id) {
        try {
            return categoriaRepository.buscarPorId(id);
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar la categoría: " + e.getMessage(), e);
        }
    }
}
