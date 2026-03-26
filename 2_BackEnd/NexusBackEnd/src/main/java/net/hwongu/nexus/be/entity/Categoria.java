package net.hwongu.nexus.be.entity;

/**
 * Representa la entidad Categoria en el sistema.
 * Esta clase es un POJO que mapea la tabla 'categoria' de la base de datos,
 * conteniendo la información fundamental de las categorías de productos.
 *
 * @author Henry Wong (hwongu@gmail.com)
 */
public class Categoria {

    private Integer idCategoria;
    private String nombre;
    private String descripcion;

    /**
     * Constructor por defecto.
     * Crea una instancia de Categoria sin inicializar sus atributos.
     */
    public Categoria() {
    }

    /**
     * Constructor para crear una referencia rápida a una Categoria.
     * Utilizado para establecer relaciones donde solo se necesita el ID.
     *
     * @param idCategoria El identificador único de la categoría.
     */
    public Categoria(Integer idCategoria) {
        this.idCategoria = idCategoria;
    }

    /**
     * Constructor completamente parametrizado.
     * Crea una instancia de Categoria con todos sus atributos inicializados.
     *
     * @param idCategoria El identificador único de la categoría.
     * @param nombre El nombre de la categoría.
     * @param descripcion Una breve descripción de la categoría.
     */
    public Categoria(Integer idCategoria, String nombre, String descripcion) {
        this.idCategoria = idCategoria;
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    /**
     * Obtiene el identificador único de la categoría.
     *
     * @return El ID de la categoría.
     */
    public Integer getIdCategoria() { return idCategoria; }

    /**
     * Establece el identificador único de la categoría.
     *
     * @param idCategoria El nuevo ID para la categoría.
     */
    public void setIdCategoria(Integer idCategoria) { this.idCategoria = idCategoria; }

    /**
     * Obtiene el nombre de la categoría.
     *
     * @return El nombre de la categoría.
     */
    public String getNombre() { return nombre; }

    /**
     * Establece el nombre de la categoría.
     *
     * @param nombre El nuevo nombre para la categoría.
     */
    public void setNombre(String nombre) { this.nombre = nombre; }

    /**
     * Obtiene la descripción de la categoría.
     *
     * @return La descripción de la categoría.
     */
    public String getDescripcion() { return descripcion; }

    /**
     * Establece la descripción de la categoría.
     *
     * @param descripcion La nueva descripción para la categoría.
     */
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
}
