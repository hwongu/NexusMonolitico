package net.hwongu.nexus.be.dto;

/**
 * Transporta datos de usuario entre capas.
 *
 * @author Henry Wong
 * GitHub @hwongu
 * https://github.com/hwongu
 */
public class UsuarioDTO {

    private Integer idUsuario;
    private String username;
    private Boolean estado;

    public UsuarioDTO() {
    }

    public UsuarioDTO(Integer idUsuario, String username, Boolean estado) {
        this.idUsuario = idUsuario;
        this.username = username;
        this.estado = estado;
    }

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }
}
