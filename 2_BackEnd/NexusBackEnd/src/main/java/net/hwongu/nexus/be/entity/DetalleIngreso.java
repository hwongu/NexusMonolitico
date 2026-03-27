package net.hwongu.nexus.be.entity;

/**
 * Representa una línea de detalle dentro de un registro de ingreso.
 * Esta entidad mapea la tabla 'detalle_ingreso' y establece una relación
 * muchos a uno con las entidades {@link Ingreso} y {@link Producto}.
 *
 * @author Henry Wong (hwongu@gmail.com)
 */
public class DetalleIngreso {

    private Integer idDetalle;
    private Ingreso ingreso;
    private Producto producto;
    private Integer cantidad;
    private Double precioCompra;

    /**
     * Constructor por defecto.
     * Crea una instancia de DetalleIngreso sin inicializar sus atributos.
     */
    public DetalleIngreso() {
    }

    /**
     * Constructor para crear una referencia rápida a un DetalleIngreso.
     *
     * @param idDetalle El identificador único del detalle de ingreso.
     */
    public DetalleIngreso(Integer idDetalle) {
        this.idDetalle = idDetalle;
    }

    /**
     * Constructor completamente parametrizado.
     *
     * @param idDetalle El identificador único del detalle.
     * @param ingreso El ingreso al que pertenece este detalle.
     * @param producto El producto que se está ingresando.
     * @param cantidad La cantidad de unidades del producto.
     * @param precioCompra El costo unitario del producto en esta compra.
     */
    public DetalleIngreso(Integer idDetalle, Ingreso ingreso, Producto producto, Integer cantidad, Double precioCompra) {
        this.idDetalle = idDetalle;
        this.ingreso = ingreso;
        this.producto = producto;
        this.cantidad = cantidad;
        this.precioCompra = precioCompra;
    }

    /**
     * Obtiene el identificador único del detalle de ingreso.
     *
     * @return El ID del detalle.
     */
    public Integer getIdDetalle() { return idDetalle; }

    /**
     * Establece el identificador único del detalle de ingreso.
     *
     * @param idDetalle El nuevo ID para el detalle.
     */
    public void setIdDetalle(Integer idDetalle) { this.idDetalle = idDetalle; }

    /**
     * Obtiene el ingreso al que está asociado este detalle.
     *
     * @return El objeto {@link Ingreso} padre.
     */
    public Ingreso getIngreso() { return ingreso; }

    /**
     * Establece el ingreso al que pertenece este detalle.
     *
     * @param ingreso El objeto {@link Ingreso} a asociar.
     */
    public void setIngreso(Ingreso ingreso) { this.ingreso = ingreso; }

    /**
     * Obtiene el producto asociado a este detalle de ingreso.
     *
     * @return El objeto {@link Producto} del detalle.
     */
    public Producto getProducto() { return producto; }

    /**
     * Establece el producto para este detalle de ingreso.
     *
     * @param producto El objeto {@link Producto} a asociar.
     */
    public void setProducto(Producto producto) { this.producto = producto; }

    /**
     * Obtiene la cantidad de unidades del producto ingresado.
     *
     * @return La cantidad de productos.
     */
    public Integer getCantidad() { return cantidad; }

    /**
     * Establece la cantidad de unidades del producto ingresado.
     *
     * @param cantidad La nueva cantidad.
     */
    public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }

    /**
     * Obtiene el precio de compra unitario del producto en este ingreso.
     *
     * @return El precio de compra.
     */
    public Double getPrecioCompra() { return precioCompra; }

    /**
     * Establece el precio de compra unitario del producto.
     *
     * @param precioCompra El nuevo precio de compra.
     */
    public void setPrecioCompra(Double precioCompra) { this.precioCompra = precioCompra; }
}
