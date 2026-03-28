package net.hwongu.nexus.be.dto;

import java.time.LocalDateTime;

/**
 * Transporta datos de ingreso entre capas.
 *
 * @author Henry Wong
 * GitHub @hwongu
 * https://github.com/hwongu
 */
public class IngresoDTO {

    private Integer idIngreso;
    private Integer idUsuario;
    private String username;
    private LocalDateTime fechaIngreso;
    private String estado;

    public IngresoDTO() {
    }

    public IngresoDTO(Integer idIngreso, Integer idUsuario, String username, LocalDateTime fechaIngreso, String estado) {
        this.idIngreso = idIngreso;
        this.idUsuario = idUsuario;
        this.username = username;
        this.fechaIngreso = fechaIngreso;
        this.estado = estado;
    }

    public Integer getIdIngreso() {
        return idIngreso;
    }

    public void setIdIngreso(Integer idIngreso) {
        this.idIngreso = idIngreso;
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

    public LocalDateTime getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(LocalDateTime fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
