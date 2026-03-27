package net.hwongu.nexus.be.entity;

import java.time.LocalDateTime;

/**
 * Representa la cabecera de un registro de ingreso de mercancía.
 * Esta entidad mapea la tabla 'ingreso' y contiene la información general
 * de la transacción, como el usuario que la realizó y la fecha.
 *
 * @author Henry Wong (hwongu@gmail.com)
 */
public class Ingreso {

    private Integer idIngreso;
    private Usuario usuario;
    private LocalDateTime fechaIngreso;
    private String estado;

    /**
     * Constructor por defecto.
     * Crea una instancia de Ingreso sin inicializar sus atributos.
     */
    public Ingreso() {
    }

    /**
     * Constructor para crear una referencia rápida a un Ingreso.
     *
     * @param idIngreso El identificador único del ingreso.
     */
    public Ingreso(Integer idIngreso) {
        this.idIngreso = idIngreso;
    }

    /**
     * Constructor completamente parametrizado.
     *
     * @param idIngreso El identificador único del ingreso.
     * @param usuario El usuario que registra el ingreso.
     * @param fechaIngreso La fecha y hora en que se realizó el ingreso.
     * @param estado El estado actual del ingreso (ej. RECIBIDO, ANULADO).
     */
    public Ingreso(Integer idIngreso, Usuario usuario, LocalDateTime fechaIngreso, String estado) {
        this.idIngreso = idIngreso;
        this.usuario = usuario;
        this.fechaIngreso = fechaIngreso;
        this.estado = estado;
    }

    /**
     * Obtiene el identificador único del ingreso.
     *
     * @return El ID del ingreso.
     */
    public Integer getIdIngreso() { return idIngreso; }

    /**
     * Establece el identificador único del ingreso.
     *
     * @param idIngreso El nuevo ID para el ingreso.
     */
    public void setIdIngreso(Integer idIngreso) { this.idIngreso = idIngreso; }

    /**
     * Obtiene el usuario que registró el ingreso.
     *
     * @return El objeto {@link Usuario} asociado.
     */
    public Usuario getUsuario() { return usuario; }

    /**
     * Establece el usuario que registra el ingreso.
     *
     * @param usuario El objeto {@link Usuario} a asociar.
     */
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    /**
     * Obtiene la fecha y hora del ingreso.
     *
     * @return Un objeto {@link LocalDateTime} con la marca de tiempo.
     */
    public LocalDateTime getFechaIngreso() { return fechaIngreso; }

    /**
     * Establece la fecha y hora del ingreso.
     *
     * @param fechaIngreso El nuevo objeto {@link LocalDateTime}.
     */
    public void setFechaIngreso(LocalDateTime fechaIngreso) { this.fechaIngreso = fechaIngreso; }

    /**
     * Obtiene el estado del ingreso.
     *
     * @return El estado como una cadena de texto.
     */
    public String getEstado() { return estado; }

    /**
     * Establece el estado del ingreso.
     *
     * @param estado El nuevo estado (ej. "RECIBIDO").
     */
    public void setEstado(String estado) { this.estado = estado; }
}
