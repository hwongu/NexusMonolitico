package net.hwongu.nexus.be.dto;

/**
 * Data Transfer Object (DTO) para la entidad {@link net.hwongu.nexus.be.entity.Categoria}.
 * Este objeto se utiliza para transferir datos de categorías entre las capas
 * de la aplicación (por ejemplo, del servicio al controlador) y en las respuestas
 * de la API, proporcionando una representación desacoplada de la entidad de persistencia.
 *
 * @author Henry Wong (hwongu@gmail.com)
 */
public class CategoriaDTO {

    private Integer idCategoria;
    private String nombre;
    private String descripcion;

    /**
     * Constructor por defecto.
     * Crea una instancia de CategoriaDTO sin inicializar sus atributos.
     */
    public CategoriaDTO() {
    }

    /**
     * Constructor completamente parametrizado.
     *
     * @param idCategoria El identificador único de la categoría.
     * @param nombre El nombre de la categoría.
     * @param descripcion La descripción de la categoría.
     */
    public CategoriaDTO(Integer idCategoria, String nombre, String descripcion) {
        this.idCategoria = idCategoria;
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    /**
     * Obtiene el identificador único de la categoría.
     *
     * @return El ID de la categoría.
     */
    public Integer getIdCategoria() {
        return idCategoria;
    }

    /**
     * Establece el identificador único de la categoría.
     *
     * @param idCategoria El nuevo ID para la categoría.
     */
    public void setIdCategoria(Integer idCategoria) {
        this.idCategoria = idCategoria;
    }

    /**
     * Obtiene el nombre de la categoría.
     *
     * @return El nombre de la categoría.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece el nombre de la categoría.
     *
     * @param nombre El nuevo nombre para la categoría.
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Obtiene la descripción de la categoría.
     *
     * @return La descripción de la categoría.
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * Establece la descripción de la categoría.
     *
     * @param descripcion La nueva descripción para la categoría.
     */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
