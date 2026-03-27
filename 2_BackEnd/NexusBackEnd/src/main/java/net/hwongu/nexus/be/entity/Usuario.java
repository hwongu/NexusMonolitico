package net.hwongu.nexus.be.entity;

/**
 * Representa la entidad Usuario en el sistema.
 * Este POJO mapea la tabla 'usuario' de la base de datos y contiene las
 * credenciales y el estado de un usuario del sistema.
 *
 * @author Henry Wong (hwongu@gmail.com)
 */
public class Usuario {

    private Integer idUsuario;
    private String username;
    private String password;
    private Boolean estado;

    /**
     * Constructor por defecto.
     * Crea una instancia de Usuario sin inicializar sus atributos.
     */
    public Usuario() {
    }

    /**
     * Constructor para crear una referencia rápida a un Usuario.
     *
     * @param idUsuario El identificador único del usuario.
     */
    public Usuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    /**
     * Constructor completamente parametrizado.
     *
     * @param idUsuario El identificador único del usuario.
     * @param username El nombre de usuario para el inicio de sesión.
     * @param password La contraseña del usuario (se recomienda almacenar un hash).
     * @param estado El estado del usuario (true para activo, false para inactivo).
     */
    public Usuario(Integer idUsuario, String username, String password, Boolean estado) {
        this.idUsuario = idUsuario;
        this.username = username;
        this.password = password;
        this.estado = estado;
    }

    /**
     * Obtiene el identificador único del usuario.
     *
     * @return El ID del usuario.
     */
    public Integer getIdUsuario() { return idUsuario; }

    /**
     * Establece el identificador único del usuario.
     *
     * @param idUsuario El nuevo ID para el usuario.
     */
    public void setIdUsuario(Integer idUsuario) { this.idUsuario = idUsuario; }

    /**
     * Obtiene el nombre de usuario.
     *
     * @return El nombre de usuario.
     */
    public String getUsername() { return username; }

    /**
     * Establece el nombre de usuario.
     *
     * @param username El nuevo nombre de usuario.
     */
    public void setUsername(String username) { this.username = username; }

    /**
     * Obtiene la contraseña del usuario.
     *
     * @return La contraseña del usuario.
     */
    public String getPassword() { return password; }

    /**
     * Establece la contraseña del usuario.
     *
     * @param password La nueva contraseña.
     */
    public void setPassword(String password) { this.password = password; }

    /**
     * Obtiene el estado del usuario.
     *
     * @return {@code true} si el usuario está activo, {@code false} en caso contrario.
     */
    public Boolean getEstado() { return estado; }

    /**
     * Establece el estado del usuario.
     *
     * @param estado El nuevo estado del usuario.
     */
    public void setEstado(Boolean estado) { this.estado = estado; }
}
