package net.hwongu.nexus.be.service;

import net.hwongu.nexus.be.entity.DetalleIngreso;
import net.hwongu.nexus.be.entity.Ingreso;
import net.hwongu.nexus.be.repository.IngresoRepository;

import java.sql.SQLException;
import java.util.List;

/**
 * Coordina la logica de negocio de ingresos.
 *
 * @author Henry Wong
 * GitHub @hwongu
 * https://github.com/hwongu
 */
public class IngresoService {

    private final IngresoRepository ingresoRepository;

    public IngresoService(IngresoRepository ingresoRepository) {
        this.ingresoRepository = ingresoRepository;
    }

    public void registrarIngresoCompleto(Ingreso ingreso, List<DetalleIngreso> detalles) {
        try {
            ingresoRepository.registrarIngresoCompleto(ingreso, detalles);
        } catch (SQLException e) {
            throw new RuntimeException("Error al registrar el ingreso: " + e.getMessage(), e);
        }
    }

    public List<Ingreso> listarIngresos() {
        try {
            return ingresoRepository.listar();
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar los ingresos: " + e.getMessage(), e);
        }
    }

    public List<DetalleIngreso> buscarDetallesPorIngreso(Integer idIngreso) {
        try {
            return ingresoRepository.buscarDetallesPorIngreso(idIngreso);
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar los detalles del ingreso: " + e.getMessage(), e);
        }
    }

    public void anularIngreso(Integer idIngreso) {
        try {
            ingresoRepository.anularIngreso(idIngreso);
        } catch (SQLException e) {
            throw new RuntimeException("Error al anular el ingreso: " + e.getMessage(), e);
        }
    }

    public void actualizarEstadoIngreso(Integer idIngreso, String nuevoEstado) {
        try {
            ingresoRepository.actualizarEstado(idIngreso, nuevoEstado);
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar el estado del ingreso: " + e.getMessage(), e);
        }
    }
}
