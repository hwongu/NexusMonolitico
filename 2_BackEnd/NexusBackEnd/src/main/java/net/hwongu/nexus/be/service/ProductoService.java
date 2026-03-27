package net.hwongu.nexus.be.service;

import net.hwongu.nexus.be.entity.Producto;
import net.hwongu.nexus.be.repository.ProductoRepository;

import java.sql.SQLException;
import java.util.List;

/**
 * Capa de servicio para la gestión de la entidad {@link Producto}.
 * Encapsula la lógica de negocio para las operaciones CRUD de productos,
 * sirviendo como intermediario entre los controladores y el repositorio de productos.
 *
 * @author Henry Wong (hwongu@gmail.com)
 */
public class ProductoService {

    private final ProductoRepository productoRepository;

    /**
     * Constructor que inyecta la dependencia del repositorio de productos.
     * @param productoRepository La instancia del repositorio a utilizar.
     */
    public ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    /**
     * Registra un nuevo producto y lo devuelve con su ID.
     *
     * @param producto El objeto {@link Producto} a registrar.
     * @return La entidad {@link Producto} persistida con su ID autogenerado.
     * @throws RuntimeException Si ocurre un error de base de datos durante la inserción.
     */
    public Producto registrarProducto(Producto producto) {
        try {
            return productoRepository.insertar(producto);
        } catch (SQLException e) {
            throw new RuntimeException("Error al registrar el producto: " + e.getMessage(), e);
        }
    }

    /**
     * Actualiza un producto existente.
     *
     * @param producto El objeto {@link Producto} con los datos a actualizar.
     * @throws RuntimeException Si ocurre un error de base de datos durante la actualización.
     */
    public void actualizarProducto(Producto producto) {
        try {
            productoRepository.actualizar(producto);
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar el producto: " + e.getMessage(), e);
        }
    }

    /**
     * Elimina un producto por su ID.
     *
     * @param id El ID del producto a eliminar.
     * @throws RuntimeException Si ocurre un error de base de datos durante la eliminación.
     */
    public void eliminarProducto(Integer id) {
        try {
            productoRepository.eliminar(id);
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar el producto: " + e.getMessage(), e);
        }
    }

    /**
     * Obtiene una lista de todos los productos.
     *
     * @return Una {@link List} de objetos {@link Producto}.
     * @throws RuntimeException Si ocurre un error de base de datos durante la consulta.
     */
    public List<Producto> listarProductos() {
        try {
            return productoRepository.listar();
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar los productos: " + e.getMessage(), e);
        }
    }

    /**
     * Busca un producto por su ID.
     *
     * @param id El ID del producto a buscar.
     * @return El objeto {@link Producto} si se encuentra; de lo contrario, {@code null}.
     * @throws RuntimeException Si ocurre un error de base de datos durante la búsqueda.
     */
    public Producto buscarProductoPorId(Integer id) {
        try {
            return productoRepository.buscarPorId(id);
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar el producto: " + e.getMessage(), e);
        }
    }
}
