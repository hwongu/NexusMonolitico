package net.hwongu.nexus.be.entity;

/**
 * Representa la entidad de usuario.
 *
 * @author Henry Wong
 * GitHub @hwongu
 * https://github.com/hwongu
 */
public class Usuario {

    private Integer idUsuario;
    private String username;
    private String password;
    private Boolean estado;

    public Usuario() {
    }

    public Usuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Usuario(Integer idUsuario, String username, String password, Boolean estado) {
        this.idUsuario = idUsuario;
        this.username = username;
        this.password = password;
        this.estado = estado;
    }

    public Integer getIdUsuario() { return idUsuario; }

    public void setIdUsuario(Integer idUsuario) { this.idUsuario = idUsuario; }

    public String getUsername() { return username; }

    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }

    public void setPassword(String password) { this.password = password; }

    public Boolean getEstado() { return estado; }

    public void setEstado(Boolean estado) { this.estado = estado; }
}
