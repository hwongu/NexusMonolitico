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
 * Pruebas unitarias para la clase {@link ProductoService}.
 * Se utiliza Mockito para simular el {@link ProductoRepository} y probar
 * la lógica de la capa de servicio de forma aislada.
 */
@ExtendWith(MockitoExtension.class)
class ProductoServiceTest {

    @Mock
    private ProductoRepository productoRepository;

    @InjectMocks
    private ProductoService productoService;

    private Producto producto;
    private Categoria categoria;

    /**
     * Inicializa objetos comunes (Categoria, Producto) para ser usados en las pruebas.
     */
    @BeforeEach
    void setUp() {
        categoria = new Categoria(1, "Teclados", "Periféricos de entrada");
        producto = new Producto(1, categoria, "Teclado Mecánico", 99.99, 50);
    }

    /**
     * Prueba que el método listarProductos devuelve una lista de productos correctamente.
     */
    @Test
    void testListarProductos() throws SQLException {
        // Simula que el repositorio devuelve una lista con un producto
        when(productoRepository.listar()).thenReturn(List.of(producto));

        // Llama al método del servicio
        List<Producto> resultado = productoService.listarProductos();

        // Verifica que el resultado es el esperado
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Teclado Mecánico", resultado.getFirst().getNombre());
        verify(productoRepository, times(1)).listar();
    }

    /**
     * Prueba la búsqueda de un producto por un ID que existe.
     */
    @Test
    void testBuscarProductoPorIdExistente() throws SQLException {
        // Simula que el repositorio encuentra y devuelve un producto
        when(productoRepository.buscarPorId(1)).thenReturn(producto);

        // Llama al método del servicio
        Producto resultado = productoService.buscarProductoPorId(1);

        // Verifica que se devolvió el producto correcto
        assertNotNull(resultado);
        assertEquals(1, resultado.getIdProducto());
        verify(productoRepository, times(1)).buscarPorId(1);
    }

    /**
     * Prueba la búsqueda de un producto por un ID que no existe.
     */
    @Test
    void testBuscarProductoPorIdNoExistente() throws SQLException {
        // Simula que el repositorio no encuentra el producto y devuelve null
        when(productoRepository.buscarPorId(99)).thenReturn(null);

        // Llama al método del servicio
        Producto resultado = productoService.buscarProductoPorId(99);

        // Verifica que el resultado es null
        assertNull(resultado);
        verify(productoRepository, times(1)).buscarPorId(99);
    }

    /**
     * Prueba el registro exitoso de un nuevo producto.
     */
    @Test
    void testRegistrarProducto() throws SQLException {
        // Simula que el repositorio inserta el producto y le asigna un ID
        when(productoRepository.insertar(any(Producto.class))).thenReturn(producto);

        // Crea un producto sin ID para simular el registro
        Producto productoParaRegistrar = new Producto(null, categoria, "Teclado Mecánico", 99.99, 50);
        Producto resultado = productoService.registrarProducto(productoParaRegistrar);

        // Verifica que el producto devuelto tiene el ID asignado
        assertNotNull(resultado);
        assertEquals(1, resultado.getIdProducto());
        verify(productoRepository, times(1)).insertar(productoParaRegistrar);
    }

    /**
     * Prueba la actualización de un producto existente.
     */
    @Test
    void testActualizarProducto() throws SQLException {
        // Simula que el método de actualizar del repositorio no lanza excepciones
        doNothing().when(productoRepository).actualizar(producto);

        // Verifica que la llamada al servicio no lanza excepciones
        assertDoesNotThrow(() -> productoService.actualizarProducto(producto));

        // Verifica que el método del repositorio fue invocado
        verify(productoRepository, times(1)).actualizar(producto);
    }

    /**
     * Prueba la eliminación de un producto.
     */
    @Test
    void testEliminarProducto() throws SQLException {
        // Simula que el método de eliminar del repositorio no lanza excepciones
        doNothing().when(productoRepository).eliminar(1);

        // Verifica que la llamada al servicio no lanza excepciones
        assertDoesNotThrow(() -> productoService.eliminarProducto(1));

        // Verifica que el método del repositorio fue invocado
        verify(productoRepository, times(1)).eliminar(1);
    }

    /**
     * Prueba que el servicio maneja las SQLException del repositorio
     * y las convierte en RuntimeException.
     */
    @Test
    void testListarProductosLanzaExcepcion() throws SQLException {
        // Simula que el repositorio lanza una SQLException
        when(productoRepository.listar()).thenThrow(new SQLException("Error de BD"));

        // Verifica que el servicio lanza una RuntimeException con el mensaje adecuado
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> productoService.listarProductos());

        assertTrue(exception.getMessage().contains("Error al listar los productos"));
        verify(productoRepository, times(1)).listar();
    }
}
