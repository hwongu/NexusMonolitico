package net.hwongu.nexus.be.dto;

/**
 * Transporta datos de detalle de ingreso entre capas.
 *
 * @author Henry Wong
 * GitHub @hwongu
 * https://github.com/hwongu
 */
public class DetalleIngresoDTO {

    private Integer idDetalle;
    private Integer idIngreso;
    private Integer idProducto;
    private String nombreProducto;
    private Integer cantidad;
    private Double precioCompra;

    public DetalleIngresoDTO() {
    }

    public DetalleIngresoDTO(Integer idDetalle, Integer idIngreso, Integer idProducto, String nombreProducto, Integer cantidad, Double precioCompra) {
        this.idDetalle = idDetalle;
        this.idIngreso = idIngreso;
        this.idProducto = idProducto;
        this.nombreProducto = nombreProducto;
        this.cantidad = cantidad;
        this.precioCompra = precioCompra;
    }

    public Integer getIdDetalle() {
        return idDetalle;
    }

    public void setIdDetalle(Integer idDetalle) {
        this.idDetalle = idDetalle;
    }

    public Integer getIdIngreso() {
        return idIngreso;
    }

    public void setIdIngreso(Integer idIngreso) {
        this.idIngreso = idIngreso;
    }

    public Integer getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(Integer idProducto) {
        this.idProducto = idProducto;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public Double getPrecioCompra() {
        return precioCompra;
    }

    public void setPrecioCompra(Double precioCompra) {
        this.precioCompra = precioCompra;
    }
}
