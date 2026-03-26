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
 * Pruebas unitarias para la clase {@link IngresoService}.
 * Utiliza Mockito para simular el comportamiento del {@link IngresoRepository}
 * y así probar la lógica de negocio de la capa de servicio de forma aislada.
 */
@ExtendWith(MockitoExtension.class)
class IngresoServiceTest {

    @Mock
    private IngresoRepository ingresoRepository;

    @InjectMocks
    private IngresoService ingresoService;

    private Ingreso ingreso;
    private List<DetalleIngreso> detalles;

    /**
     * Configura los objetos de prueba antes de cada test.
     */
    @BeforeEach
    void setUp() {
        Usuario usuario = new Usuario(1, "testuser", "pass", true);
        Producto producto = new Producto(1, new Categoria(1), "Laptop", 1500.0, 10);
        ingreso = new Ingreso(1, usuario, LocalDateTime.now(), "RECIBIDO");
        DetalleIngreso detalle = new DetalleIngreso(1, ingreso, producto, 5, 1400.0);
        detalles = List.of(detalle);
    }

    /**
     * Prueba que el método de registrar un ingreso completo invoca correctamente al repositorio.
     */
    @Test
    void testRegistrarIngresoCompleto() throws SQLException {
        // Simula que el método del repositorio no hace nada y no lanza excepción
        doNothing().when(ingresoRepository).registrarIngresoCompleto(ingreso, detalles);

        // Ejecuta el método del servicio y verifica que no lance ninguna excepción
        assertDoesNotThrow(() -> ingresoService.registrarIngresoCompleto(ingreso, detalles));

        // Verifica que el método del repositorio fue llamado exactamente una vez
        verify(ingresoRepository, times(1)).registrarIngresoCompleto(ingreso, detalles);
    }

    /**
     * Prueba que el listado de ingresos devuelve los datos esperados.
     */
    @Test
    void testListarIngresos() throws SQLException {
        // Simula que el repositorio devuelve una lista con un ingreso
        when(ingresoRepository.listar()).thenReturn(List.of(ingreso));

        // Ejecuta el método del servicio
        List<Ingreso> resultado = ingresoService.listarIngresos();

        // Verifica que el resultado no sea nulo y contenga un elemento
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(ingresoRepository, times(1)).listar();
    }

    /**
     * Prueba la búsqueda de detalles de un ingreso por su ID.
     */
    @Test
    void testBuscarDetallesPorIngreso() throws SQLException {
        // Simula que el repositorio devuelve una lista de detalles
        when(ingresoRepository.buscarDetallesPorIngreso(1)).thenReturn(detalles);

        // Ejecuta el método del servicio
        List<DetalleIngreso> resultado = ingresoService.buscarDetallesPorIngreso(1);

        // Verifica que el resultado no sea nulo y contenga los datos correctos
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(5, resultado.getFirst().getCantidad());
        verify(ingresoRepository, times(1)).buscarDetallesPorIngreso(1);
    }

    /**
     * Prueba que el método de anular un ingreso invoca correctamente al repositorio.
     */
    @Test
    void testAnularIngreso() throws SQLException {
        // Simula que el método del repositorio no hace nada
        doNothing().when(ingresoRepository).anularIngreso(1);

        // Verifica que la llamada al servicio no lance excepciones
        assertDoesNotThrow(() -> ingresoService.anularIngreso(1));

        // Verifica que el método del repositorio fue llamado una vez
        verify(ingresoRepository, times(1)).anularIngreso(1);
    }

    /**
     * Prueba que el método de actualizar estado de un ingreso invoca correctamente al repositorio.
     */
    @Test
    void testActualizarEstadoIngreso() throws SQLException {
        // Simula que el método del repositorio no hace nada
        doNothing().when(ingresoRepository).actualizarEstado(1, "PROCESADO");

        // Verifica que la llamada al servicio no lance excepciones
        assertDoesNotThrow(() -> ingresoService.actualizarEstadoIngreso(1, "PROCESADO"));

        // Verifica que el método del repositorio fue llamado una vez
        verify(ingresoRepository, times(1)).actualizarEstado(1, "PROCESADO");
    }

    /**
     * Prueba que el servicio maneja correctamente las excepciones de SQL
     * al registrar un ingreso y las convierte en RuntimeException.
     */
    @Test
    void testRegistrarIngresoCompletoLanzaExcepcion() throws SQLException {
        // Simula que el repositorio lanza una SQLException
        doThrow(new SQLException("Error de transacción")).when(ingresoRepository).registrarIngresoCompleto(ingreso, detalles);

        // Verifica que el servicio captura la SQLException y lanza una RuntimeException
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> ingresoService.registrarIngresoCompleto(ingreso, detalles));

        // Verifica que el mensaje de la excepción sea el esperado
        assertTrue(exception.getMessage().contains("Error al registrar el ingreso"));
        verify(ingresoRepository, times(1)).registrarIngresoCompleto(ingreso, detalles);
    }
}
