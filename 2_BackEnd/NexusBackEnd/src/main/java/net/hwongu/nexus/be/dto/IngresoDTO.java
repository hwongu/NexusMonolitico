package net.hwongu.nexus.be.dto;

import java.time.LocalDateTime;

/**
 * DTO para la entidad {@link net.hwongu.nexus.be.entity.Ingreso}.
 * Facilita la transferencia de datos de la cabecera de un ingreso, aplanando
 * la relación con la entidad Usuario para incluir directamente el ID y el
 * nombre del usuario, simplificando así las respuestas de la API.
 *
 * @author Henry Wong (hwongu@gmail.com)
 */
public class IngresoDTO {

    private Integer idIngreso;
    private Integer idUsuario;
    private String username;
    private LocalDateTime fechaIngreso;
    private String estado;

    /**
     * Constructor por defecto.
     */
    public IngresoDTO() {
    }

    /**
     * Constructor completamente parametrizado.
     *
     * @param idIngreso El ID del ingreso.
     * @param idUsuario El ID del usuario que registró el ingreso.
     * @param username El nombre del usuario para referencia.
     * @param fechaIngreso La fecha y hora del ingreso.
     * @param estado El estado del ingreso (ej. "RECIBIDO").
     */
    public IngresoDTO(Integer idIngreso, Integer idUsuario, String username, LocalDateTime fechaIngreso, String estado) {
        this.idIngreso = idIngreso;
        this.idUsuario = idUsuario;
        this.username = username;
        this.fechaIngreso = fechaIngreso;
        this.estado = estado;
    }

    /**
     * Obtiene el ID del ingreso.
     * @return El ID del ingreso.
     */
    public Integer getIdIngreso() {
        return idIngreso;
    }

    /**
     * Establece el ID del ingreso.
     * @param idIngreso El nuevo ID.
     */
    public void setIdIngreso(Integer idIngreso) {
        this.idIngreso = idIngreso;
    }

    /**
     * Obtiene el ID del usuario asociado.
     * @return El ID del usuario.
     */
    public Integer getIdUsuario() {
        return idUsuario;
    }

    /**
     * Establece el ID del usuario asociado.
     * @param idUsuario El nuevo ID de usuario.
     */
    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    /**
     * Obtiene el nombre del usuario asociado.
     * @return El nombre de usuario.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Establece el nombre del usuario asociado.
     * @param username El nuevo nombre de usuario.
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Obtiene la fecha y hora del ingreso.
     * @return La fecha del ingreso.
     */
    public LocalDateTime getFechaIngreso() {
        return fechaIngreso;
    }

    /**
     * Establece la fecha y hora del ingreso.
     * @param fechaIngreso La nueva fecha.
     */
    public void setFechaIngreso(LocalDateTime fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    /**
     * Obtiene el estado del ingreso.
     * @return El estado.
     */
    public String getEstado() {
        return estado;
    }

    /**
     * Establece el estado del ingreso.
     * @param estado El nuevo estado.
     */
    public void setEstado(String estado) {
        this.estado = estado;
    }
}
