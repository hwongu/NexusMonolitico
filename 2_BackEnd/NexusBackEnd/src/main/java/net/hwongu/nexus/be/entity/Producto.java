package net.hwongu.nexus.be.entity;

/**
 * Representa la entidad de producto.
 *
 * @author Henry Wong
 * GitHub @hwongu
 * https://github.com/hwongu
 */
public class Producto {

    private Integer idProducto;
    private Categoria categoria;
    private String nombre;
    private Double precio;
    private Integer stock;

    public Producto() {
    }

    public Producto(Integer idProducto) {
        this.idProducto = idProducto;
    }

    public Producto(Integer idProducto, Categoria categoria, String nombre, Double precio, Integer stock) {
        this.idProducto = idProducto;
        this.categoria = categoria;
        this.nombre = nombre;
        this.precio = precio;
        this.stock = stock;
    }

    public Integer getIdProducto() { return idProducto; }

    public void setIdProducto(Integer idProducto) { this.idProducto = idProducto; }

    public Categoria getCategoria() { return categoria; }

    public void setCategoria(Categoria categoria) { this.categoria = categoria; }

    public String getNombre() { return nombre; }

    public void setNombre(String nombre) { this.nombre = nombre; }

    public Double getPrecio() { return precio; }

    public void setPrecio(Double precio) { this.precio = precio; }

    public Integer getStock() { return stock; }

    public void setStock(Integer stock) { this.stock = stock; }
}
