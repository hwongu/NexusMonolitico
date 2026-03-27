package net.hwongu.nexus.be.entity;

/**
 * Representa la entidad Producto en el sistema.
 * Esta clase es un Plain Old Java Object (POJO) que mapea la tabla 'producto'
 * de la base de datos. Contiene los atributos del producto, incluyendo su
 * relación con la entidad Categoria.
 *
 * @author Henry Wong (hwongu@gmail.com)
 */
public class Producto {

    private Integer idProducto;
    private Categoria categoria;
    private String nombre;
    private Double precio;
    private Integer stock;

    /**
     * Constructor por defecto.
     * Crea una instancia de Producto sin inicializar sus atributos.
     */
    public Producto() {
    }

    /**
     * Constructor para crear una referencia rápida a un Producto.
     * Utilizado principalmente para establecer relaciones donde solo se necesita
     * el identificador único de la entidad.
     *
     * @param idProducto El identificador único del producto.
     */
    public Producto(Integer idProducto) {
        this.idProducto = idProducto;
    }

    /**
     * Constructor completamente parametrizado.
     * Crea una instancia de Producto con todos sus atributos inicializados.
     *
     * @param idProducto El identificador único del producto.
     * @param categoria La categoría a la que pertenece el producto.
     * @param nombre El nombre comercial del producto.
     * @param precio El precio unitario de venta del producto.
     * @param stock La cantidad de unidades disponibles en inventario.
     */
    public Producto(Integer idProducto, Categoria categoria, String nombre, Double precio, Integer stock) {
        this.idProducto = idProducto;
        this.categoria = categoria;
        this.nombre = nombre;
        this.precio = precio;
        this.stock = stock;
    }

    /**
     * Obtiene el identificador único del producto.
     *
     * @return El ID del producto.
     */
    public Integer getIdProducto() { return idProducto; }

    /**
     * Establece el identificador único del producto.
     *
     * @param idProducto El nuevo ID para el producto.
     */
    public void setIdProducto(Integer idProducto) { this.idProducto = idProducto; }

    /**
     * Obtiene la categoría asociada a este producto.
     *
     * @return El objeto Categoria del producto.
     */
    public Categoria getCategoria() { return categoria; }

    /**
     * Establece la categoría para este producto.
     *
     * @param categoria El nuevo objeto Categoria a asociar.
     */
    public void setCategoria(Categoria categoria) { this.categoria = categoria; }

    /**
     * Obtiene el nombre del producto.
     *
     * @return El nombre del producto.
     */
    public String getNombre() { return nombre; }

    /**
     * Establece el nombre del producto.
     *
     * @param nombre El nuevo nombre para el producto.
     */
    public void setNombre(String nombre) { this.nombre = nombre; }

    /**
     * Obtiene el precio unitario del producto.
     *
     * @return El precio del producto.
     */
    public Double getPrecio() { return precio; }

    /**
     * Establece el precio unitario del producto.
     *
     * @param precio El nuevo precio para el producto.
     */
    public void setPrecio(Double precio) { this.precio = precio; }

    /**
     * Obtiene la cantidad de stock disponible para el producto.
     *
     * @return El stock actual del producto.
     */
    public Integer getStock() { return stock; }

    /**
     * Establece la cantidad de stock disponible para el producto.
     *
     * @param stock La nueva cantidad de stock para el producto.
     */
    public void setStock(Integer stock) { this.stock = stock; }
}
