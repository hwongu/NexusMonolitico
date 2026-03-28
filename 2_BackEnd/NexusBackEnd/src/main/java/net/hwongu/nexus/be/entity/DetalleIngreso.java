package net.hwongu.nexus.be.entity;

/**
 * Representa la entidad de detalle de ingreso.
 *
 * @author Henry Wong
 * GitHub @hwongu
 * https://github.com/hwongu
 */
public class DetalleIngreso {

    private Integer idDetalle;
    private Ingreso ingreso;
    private Producto producto;
    private Integer cantidad;
    private Double precioCompra;

    public DetalleIngreso() {
    }

    public DetalleIngreso(Integer idDetalle) {
        this.idDetalle = idDetalle;
    }

    public DetalleIngreso(Integer idDetalle, Ingreso ingreso, Producto producto, Integer cantidad, Double precioCompra) {
        this.idDetalle = idDetalle;
        this.ingreso = ingreso;
        this.producto = producto;
        this.cantidad = cantidad;
        this.precioCompra = precioCompra;
    }

    public Integer getIdDetalle() { return idDetalle; }

    public void setIdDetalle(Integer idDetalle) { this.idDetalle = idDetalle; }

    public Ingreso getIngreso() { return ingreso; }

    public void setIngreso(Ingreso ingreso) { this.ingreso = ingreso; }

    public Producto getProducto() { return producto; }

    public void setProducto(Producto producto) { this.producto = producto; }

    public Integer getCantidad() { return cantidad; }

    public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }

    public Double getPrecioCompra() { return precioCompra; }

    public void setPrecioCompra(Double precioCompra) { this.precioCompra = precioCompra; }
}
