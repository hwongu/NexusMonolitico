package net.hwongu.nexus.be.service;

import net.hwongu.nexus.be.entity.Producto;
import net.hwongu.nexus.be.repository.ProductoRepository;

import java.sql.SQLException;
import java.util.List;

/**
 * Coordina la logica de negocio de productos.
 *
 * @author Henry Wong
 * GitHub @hwongu
 * https://github.com/hwongu
 */
public class ProductoService {

    private final ProductoRepository productoRepository;

    public ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    public Producto registrarProducto(Producto producto) {
        try {
            return productoRepository.insertar(producto);
        } catch (SQLException e) {
            throw new RuntimeException("Error al registrar el producto: " + e.getMessage(), e);
        }
    }

    public void actualizarProducto(Producto producto) {
        try {
            productoRepository.actualizar(producto);
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar el producto: " + e.getMessage(), e);
        }
    }

    public void eliminarProducto(Integer id) {
        try {
            productoRepository.eliminar(id);
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar el producto: " + e.getMessage(), e);
        }
    }

    public List<Producto> listarProductos() {
        try {
            return productoRepository.listar();
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar los productos: " + e.getMessage(), e);
        }
    }

    public Producto buscarProductoPorId(Integer id) {
        try {
            return productoRepository.buscarPorId(id);
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar el producto: " + e.getMessage(), e);
        }
    }
}
