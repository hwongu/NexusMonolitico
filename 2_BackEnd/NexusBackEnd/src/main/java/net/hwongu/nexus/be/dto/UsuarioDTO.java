package net.hwongu.nexus.be.dto;

/**
 * DTO para la entidad {@link net.hwongu.nexus.be.entity.Usuario}.
 * Se utiliza para transferir datos de usuario de forma segura, principalmente
 * en las respuestas de la API. Excluye deliberadamente campos sensibles como
 * la contraseña para evitar su exposición.
 *
 * @author Henry Wong (hwongu@gmail.com)
 */
public class UsuarioDTO {

    private Integer idUsuario;
    private String username;
    private Boolean estado;

    /**
     * Constructor por defecto.
     */
    public UsuarioDTO() {
    }

    /**
     * Constructor completamente parametrizado.
     *
     * @param idUsuario El ID del usuario.
     * @param username El nombre de usuario.
     * @param estado El estado de activación del usuario.
     */
    public UsuarioDTO(Integer idUsuario, String username, Boolean estado) {
        this.idUsuario = idUsuario;
        this.username = username;
        this.estado = estado;
    }

    /**
     * Obtiene el ID del usuario.
     * @return El ID del usuario.
     */
    public Integer getIdUsuario() {
        return idUsuario;
    }

    /**
     * Establece el ID del usuario.
     * @param idUsuario El nuevo ID.
     */
    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    /**
     * Obtiene el nombre de usuario.
     * @return El nombre de usuario.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Establece el nombre de usuario.
     * @param username El nuevo nombre de usuario.
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Obtiene el estado del usuario.
     * @return {@code true} si está activo, {@code false} si está inactivo.
     */
    public Boolean getEstado() {
        return estado;
    }

    /**
     * Establece el estado del usuario.
     * @param estado El nuevo estado.
     */
    public void setEstado(Boolean estado) {
        this.estado = estado;
    }
}
