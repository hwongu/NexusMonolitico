package net.hwongu.nexus.be.entity;

import java.time.LocalDateTime;

/**
 * Representa la entidad de ingreso.
 *
 * @author Henry Wong
 * GitHub @hwongu
 * https://github.com/hwongu
 */
public class Ingreso {

    private Integer idIngreso;
    private Usuario usuario;
    private LocalDateTime fechaIngreso;
    private String estado;

    public Ingreso() {
    }

    public Ingreso(Integer idIngreso) {
        this.idIngreso = idIngreso;
    }

    public Ingreso(Integer idIngreso, Usuario usuario, LocalDateTime fechaIngreso, String estado) {
        this.idIngreso = idIngreso;
        this.usuario = usuario;
        this.fechaIngreso = fechaIngreso;
        this.estado = estado;
    }

    public Integer getIdIngreso() { return idIngreso; }

    public void setIdIngreso(Integer idIngreso) { this.idIngreso = idIngreso; }

    public Usuario getUsuario() { return usuario; }

    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public LocalDateTime getFechaIngreso() { return fechaIngreso; }

    public void setFechaIngreso(LocalDateTime fechaIngreso) { this.fechaIngreso = fechaIngreso; }

    public String getEstado() { return estado; }

    public void setEstado(String estado) { this.estado = estado; }
}
