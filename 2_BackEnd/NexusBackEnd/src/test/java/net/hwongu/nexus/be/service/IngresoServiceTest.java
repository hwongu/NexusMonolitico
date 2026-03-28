package net.hwongu.nexus.be.service;

import net.hwongu.nexus.be.entity.*;
import net.hwongu.nexus.be.repository.IngresoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Valida la logica de IngresoService.
 *
 * @author Henry Wong
 * GitHub @hwongu
 * https://github.com/hwongu
 */
@ExtendWith(MockitoExtension.class)
class IngresoServiceTest {

    @Mock
    private IngresoRepository ingresoRepository;

    @InjectMocks
    private IngresoService ingresoService;

    private Ingreso ingreso;
    private List<DetalleIngreso> detalles;

    @BeforeEach
    void setUp() {
        Usuario usuario = new Usuario(1, "testuser", "pass", true);
        Producto producto = new Producto(1, new Categoria(1), "Laptop", 1500.0, 10);
        ingreso = new Ingreso(1, usuario, LocalDateTime.now(), "RECIBIDO");
        DetalleIngreso detalle = new DetalleIngreso(1, ingreso, producto, 5, 1400.0);
        detalles = List.of(detalle);
    }

    @Test
    void testRegistrarIngresoCompleto() throws SQLException {

        doNothing().when(ingresoRepository).registrarIngresoCompleto(ingreso, detalles);

        assertDoesNotThrow(() -> ingresoService.registrarIngresoCompleto(ingreso, detalles));

        verify(ingresoRepository, times(1)).registrarIngresoCompleto(ingreso, detalles);
    }

    @Test
    void testListarIngresos() throws SQLException {

        when(ingresoRepository.listar()).thenReturn(List.of(ingreso));

        List<Ingreso> resultado = ingresoService.listarIngresos();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(ingresoRepository, times(1)).listar();
    }

    @Test
    void testBuscarDetallesPorIngreso() throws SQLException {

        when(ingresoRepository.buscarDetallesPorIngreso(1)).thenReturn(detalles);

        List<DetalleIngreso> resultado = ingresoService.buscarDetallesPorIngreso(1);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(5, resultado.getFirst().getCantidad());
        verify(ingresoRepository, times(1)).buscarDetallesPorIngreso(1);
    }

    @Test
    void testAnularIngreso() throws SQLException {

        doNothing().when(ingresoRepository).anularIngreso(1);

        assertDoesNotThrow(() -> ingresoService.anularIngreso(1));

        verify(ingresoRepository, times(1)).anularIngreso(1);
    }

    @Test
    void testActualizarEstadoIngreso() throws SQLException {

        doNothing().when(ingresoRepository).actualizarEstado(1, "PROCESADO");

        assertDoesNotThrow(() -> ingresoService.actualizarEstadoIngreso(1, "PROCESADO"));

        verify(ingresoRepository, times(1)).actualizarEstado(1, "PROCESADO");
    }

    @Test
    void testRegistrarIngresoCompletoLanzaExcepcion() throws SQLException {

        doThrow(new SQLException("Error de transacción")).when(ingresoRepository).registrarIngresoCompleto(ingreso, detalles);

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> ingresoService.registrarIngresoCompleto(ingreso, detalles));

        assertTrue(exception.getMessage().contains("Error al registrar el ingreso"));
        verify(ingresoRepository, times(1)).registrarIngresoCompleto(ingreso, detalles);
    }
}
