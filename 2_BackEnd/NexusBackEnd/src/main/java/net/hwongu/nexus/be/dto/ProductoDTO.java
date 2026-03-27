package net.hwongu.nexus.be.dto;

/**
 * DTO para la entidad {@link net.hwongu.nexus.be.entity.Producto}.
 * Proporciona una representación aplanada del producto, ideal para las
 * respuestas de la API. La relación con Categoria se simplifica incluyendo
 * directamente el ID y el nombre de la categoría.
 *
 * @author Henry Wong (hwongu@gmail.com)
 */
public class ProductoDTO {

    private Integer idProducto;
    private Integer idCategoria;
    private String nombreCategoria;
    private String nombre;
    private Double precio;
    private Integer stock;

    /**
     * Constructor por defecto.
     */
    public ProductoDTO() {
    }

    /**
     * Constructor completamente parametrizado.
     *
     * @param idProducto El ID del producto.
     * @param idCategoria El ID de la categoría a la que pertenece.
     * @param nombreCategoria El nombre de la categoría para referencia.
     * @param nombre El nombre del producto.
     * @param precio El precio unitario del producto.
     * @param stock La cantidad de stock disponible.
     */
    public ProductoDTO(Integer idProducto, Integer idCategoria, String nombreCategoria, String nombre, Double precio, Integer stock) {
        this.idProducto = idProducto;
        this.idCategoria = idCategoria;
        this.nombreCategoria = nombreCategoria;
        this.nombre = nombre;
        this.precio = precio;
        this.stock = stock;
    }

    /**
     * Obtiene el ID del producto.
     * @return El ID del producto.
     */
    public Integer getIdProducto() {
        return idProducto;
    }

    /**
     * Establece el ID del producto.
     * @param idProducto El nuevo ID.
     */
    public void setIdProducto(Integer idProducto) {
        this.idProducto = idProducto;
    }

    /**
     * Obtiene el ID de la categoría asociada.
     * @return El ID de la categoría.
     */
    public Integer getIdCategoria() {
        return idCategoria;
    }

    /**
     * Establece el ID de la categoría asociada.
     * @param idCategoria El nuevo ID de categoría.
     */
    public void setIdCategoria(Integer idCategoria) {
        this.idCategoria = idCategoria;
    }

    /**
     * Obtiene el nombre de la categoría asociada.
     * @return El nombre de la categoría.
     */
    public String getNombreCategoria() {
        return nombreCategoria;
    }

    /**
     * Establece el nombre de la categoría asociada.
     * @param nombreCategoria El nuevo nombre de categoría.
     */
    public void setNombreCategoria(String nombreCategoria) {
        this.nombreCategoria = nombreCategoria;
    }

    /**
     * Obtiene el nombre del producto.
     * @return El nombre del producto.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece el nombre del producto.
     * @param nombre El nuevo nombre.
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Obtiene el precio del producto.
     * @return El precio.
     */
    public Double getPrecio() {
        return precio;
    }

    /**
     * Establece el precio del producto.
     * @param precio El nuevo precio.
     */
    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    /**
     * Obtiene el stock disponible del producto.
     * @return La cantidad en stock.
     */
    public Integer getStock() {
        return stock;
    }

    /**
     * Establece el stock disponible del producto.
     * @param stock La nueva cantidad en stock.
     */
    public void setStock(Integer stock) {
        this.stock = stock;
    }
}
