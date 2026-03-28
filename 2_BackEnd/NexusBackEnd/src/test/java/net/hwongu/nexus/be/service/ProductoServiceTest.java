package net.hwongu.nexus.be.service;

import net.hwongu.nexus.be.entity.Categoria;
import net.hwongu.nexus.be.entity.Producto;
import net.hwongu.nexus.be.repository.ProductoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Valida la logica de ProductoService.
 *
 * @author Henry Wong
 * GitHub @hwongu
 * https://github.com/hwongu
 */
@ExtendWith(MockitoExtension.class)
class ProductoServiceTest {

    @Mock
    private ProductoRepository productoRepository;

    @InjectMocks
    private ProductoService productoService;

    private Producto producto;
    private Categoria categoria;

    @BeforeEach
    void setUp() {
        categoria = new Categoria(1, "Teclados", "Periféricos de entrada");
        producto = new Producto(1, categoria, "Teclado Mecánico", 99.99, 50);
    }

    @Test
    void testListarProductos() throws SQLException {

        when(productoRepository.listar()).thenReturn(List.of(producto));

        List<Producto> resultado = productoService.listarProductos();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Teclado Mecánico", resultado.getFirst().getNombre());
        verify(productoRepository, times(1)).listar();
    }

    @Test
    void testBuscarProductoPorIdExistente() throws SQLException {

        when(productoRepository.buscarPorId(1)).thenReturn(producto);

        Producto resultado = productoService.buscarProductoPorId(1);

        assertNotNull(resultado);
        assertEquals(1, resultado.getIdProducto());
        verify(productoRepository, times(1)).buscarPorId(1);
    }

    @Test
    void testBuscarProductoPorIdNoExistente() throws SQLException {

        when(productoRepository.buscarPorId(99)).thenReturn(null);

        Producto resultado = productoService.buscarProductoPorId(99);

        assertNull(resultado);
        verify(productoRepository, times(1)).buscarPorId(99);
    }

    @Test
    void testRegistrarProducto() throws SQLException {

        when(productoRepository.insertar(any(Producto.class))).thenReturn(producto);

        Producto productoParaRegistrar = new Producto(null, categoria, "Teclado Mecánico", 99.99, 50);
        Producto resultado = productoService.registrarProducto(productoParaRegistrar);

        assertNotNull(resultado);
        assertEquals(1, resultado.getIdProducto());
        verify(productoRepository, times(1)).insertar(productoParaRegistrar);
    }

    @Test
    void testActualizarProducto() throws SQLException {

        doNothing().when(productoRepository).actualizar(producto);

        assertDoesNotThrow(() -> productoService.actualizarProducto(producto));

        verify(productoRepository, times(1)).actualizar(producto);
    }

    @Test
    void testEliminarProducto() throws SQLException {

        doNothing().when(productoRepository).eliminar(1);

        assertDoesNotThrow(() -> productoService.eliminarProducto(1));

        verify(productoRepository, times(1)).eliminar(1);
    }

    @Test
    void testListarProductosLanzaExcepcion() throws SQLException {

        when(productoRepository.listar()).thenThrow(new SQLException("Error de BD"));

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> productoService.listarProductos());

        assertTrue(exception.getMessage().contains("Error al listar los productos"));
        verify(productoRepository, times(1)).listar();
    }
}
