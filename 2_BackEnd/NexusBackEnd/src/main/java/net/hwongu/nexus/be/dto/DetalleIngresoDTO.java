package net.hwongu.nexus.be.dto;

/**
 * DTO para la entidad {@link net.hwongu.nexus.be.entity.DetalleIngreso}.
 * Representa una versión aplanada de un detalle de ingreso, diseñada para
 * ser utilizada en las respuestas de la API y en las solicitudes que requieren
 * una estructura de datos simplificada, reemplazando objetos complejos por sus IDs.
 *
 * @author Henry Wong (hwongu@gmail.com)
 */
public class DetalleIngresoDTO {

    private Integer idDetalle;
    private Integer idIngreso;
    private Integer idProducto;
    private String nombreProducto;
    private Integer cantidad;
    private Double precioCompra;

    /**
     * Constructor por defecto.
     */
    public DetalleIngresoDTO() {
    }

    /**
     * Constructor completamente parametrizado.
     *
     * @param idDetalle El ID del detalle.
     * @param idIngreso El ID del ingreso al que pertenece.
     * @param idProducto El ID del producto ingresado.
     * @param nombreProducto El nombre del producto para referencia rápida.
     * @param cantidad La cantidad de unidades del producto.
     * @param precioCompra El costo unitario del producto en esta compra.
     */
    public DetalleIngresoDTO(Integer idDetalle, Integer idIngreso, Integer idProducto, String nombreProducto, Integer cantidad, Double precioCompra) {
        this.idDetalle = idDetalle;
        this.idIngreso = idIngreso;
        this.idProducto = idProducto;
        this.nombreProducto = nombreProducto;
        this.cantidad = cantidad;
        this.precioCompra = precioCompra;
    }

    /**
     * Obtiene el ID del detalle de ingreso.
     * @return El ID del detalle.
     */
    public Integer getIdDetalle() {
        return idDetalle;
    }

    /**
     * Establece el ID del detalle de ingreso.
     * @param idDetalle El nuevo ID.
     */
    public void setIdDetalle(Integer idDetalle) {
        this.idDetalle = idDetalle;
    }

    /**
     * Obtiene el ID del ingreso padre.
     * @return El ID del ingreso.
     */
    public Integer getIdIngreso() {
        return idIngreso;
    }

    /**
     * Establece el ID del ingreso padre.
     * @param idIngreso El nuevo ID de ingreso.
     */
    public void setIdIngreso(Integer idIngreso) {
        this.idIngreso = idIngreso;
    }

    /**
     * Obtiene el ID del producto asociado.
     * @return El ID del producto.
     */
    public Integer getIdProducto() {
        return idProducto;
    }

    /**
     * Establece el ID del producto asociado.
     * @param idProducto El nuevo ID de producto.
     */
    public void setIdProducto(Integer idProducto) {
        this.idProducto = idProducto;
    }

    /**
     * Obtiene el nombre del producto.
     * @return El nombre del producto.
     */
    public String getNombreProducto() {
        return nombreProducto;
    }

    /**
     * Establece el nombre del producto.
     * @param nombreProducto El nuevo nombre del producto.
     */
    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

    /**
     * Obtiene la cantidad de unidades ingresadas.
     * @return La cantidad.
     */
    public Integer getCantidad() {
        return cantidad;
    }

    /**
     * Establece la cantidad de unidades ingresadas.
     * @param cantidad La nueva cantidad.
     */
    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    /**
     * Obtiene el precio de compra unitario.
     * @return El precio de compra.
     */
    public Double getPrecioCompra() {
        return precioCompra;
    }

    /**
     * Establece el precio de compra unitario.
     * @param precioCompra El nuevo precio de compra.
     */
    public void setPrecioCompra(Double precioCompra) {
        this.precioCompra = precioCompra;
    }
}
