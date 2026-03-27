package net.hwongu.nexus.be.service;

import net.hwongu.nexus.be.entity.DetalleIngreso;
import net.hwongu.nexus.be.entity.Ingreso;
import net.hwongu.nexus.be.repository.IngresoRepository;

import java.sql.SQLException;
import java.util.List;

/**
 * Capa de servicio para la gestión de la entidad {@link Ingreso}.
 * Orquesta operaciones complejas que involucran tanto a la cabecera del ingreso
 * como a sus detalles, delegando la persistencia al {@link IngresoRepository}.
 *
 * @author Henry Wong (hwongu@gmail.com)
 */
public class IngresoService {

    private final IngresoRepository ingresoRepository;

    /**
     * Constructor que inyecta la dependencia del repositorio de ingresos.
     * @param ingresoRepository La instancia del repositorio a utilizar.
     */
    public IngresoService(IngresoRepository ingresoRepository) {
        this.ingresoRepository = ingresoRepository;
    }

    /**
     * Orquesta el registro de un nuevo ingreso junto con sus detalles.
     *
     * @param ingreso El objeto {@link Ingreso} a registrar.
     * @param detalles La lista de {@link DetalleIngreso} asociada al ingreso.
     * @throws RuntimeException Si ocurre un error de base de datos durante la transacción.
     */
    public void registrarIngresoCompleto(Ingreso ingreso, List<DetalleIngreso> detalles) {
        try {
            ingresoRepository.registrarIngresoCompleto(ingreso, detalles);
        } catch (SQLException e) {
            throw new RuntimeException("Error al registrar el ingreso: " + e.getMessage(), e);
        }
    }

    /**
     * Obtiene una lista de todas las cabeceras de ingreso.
     *
     * @return Una lista de objetos {@link Ingreso}.
     * @throws RuntimeException Si ocurre un error de base de datos durante la consulta.
     */
    public List<Ingreso> listarIngresos() {
        try {
            return ingresoRepository.listar();
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar los ingresos: " + e.getMessage(), e);
        }
    }

    /**
     * Busca los detalles de un ingreso específico por su ID.
     *
     * @param idIngreso El ID del ingreso a buscar.
     * @return Una lista de objetos {@link DetalleIngreso}.
     * @throws RuntimeException Si ocurre un error de base de datos durante la búsqueda.
     */
    public List<DetalleIngreso> buscarDetallesPorIngreso(Integer idIngreso) {
        try {
            return ingresoRepository.buscarDetallesPorIngreso(idIngreso);
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar los detalles del ingreso: " + e.getMessage(), e);
        }
    }

    /**
     * Orquesta la anulación de un ingreso, cambiando su estado y revirtiendo el stock.
     *
     * @param idIngreso El ID del ingreso a anular.
     * @throws RuntimeException Si ocurre un error de base de datos durante la transacción.
     */
    public void anularIngreso(Integer idIngreso) {
        try {
            ingresoRepository.anularIngreso(idIngreso);
        } catch (SQLException e) {
            throw new RuntimeException("Error al anular el ingreso: " + e.getMessage(), e);
        }
    }

    /**
     * Actualiza el estado de un ingreso específico.
     *
     * @param idIngreso El ID del ingreso a actualizar.
     * @param nuevoEstado El nuevo estado para el ingreso.
     * @throws RuntimeException Si ocurre un error de base de datos durante la actualización.
     */
    public void actualizarEstadoIngreso(Integer idIngreso, String nuevoEstado) {
        try {
            ingresoRepository.actualizarEstado(idIngreso, nuevoEstado);
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar el estado del ingreso: " + e.getMessage(), e);
        }
    }
}
