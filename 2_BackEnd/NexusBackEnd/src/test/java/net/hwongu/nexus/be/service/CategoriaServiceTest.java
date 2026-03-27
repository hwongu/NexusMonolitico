package net.hwongu.nexus.be.service;

import net.hwongu.nexus.be.entity.Categoria;
import net.hwongu.nexus.be.repository.CategoriaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias para la clase {@link CategoriaService}.
 * Se utiliza Mockito para simular el {@link CategoriaRepository} y probar
 * la lógica de la capa de servicio de forma aislada.
 */
@ExtendWith(MockitoExtension.class)
class CategoriaServiceTest {

    @Mock
    private CategoriaRepository categoriaRepository;

    @InjectMocks
    private CategoriaService categoriaService;

    private Categoria categoria;

    /**
     * Inicializa un objeto Categoria común para ser usado en las pruebas.
     */
    @BeforeEach
    void setUp() {
        categoria = new Categoria(1, "Laptops", "Notebooks de alto rendimiento");
    }

    /**
     * Prueba que el método listarCategorias devuelve una lista de categorías correctamente.
     */
    @Test
    void testListarCategorias() throws SQLException {
        // Simula que el repositorio devuelve una lista con una categoría
        when(categoriaRepository.listar()).thenReturn(List.of(categoria));

        // Llama al método del servicio
        List<Categoria> resultado = categoriaService.listarCategorias();

        // Verifica que el resultado es el esperado
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Laptops", resultado.getFirst().getNombre());
        verify(categoriaRepository, times(1)).listar();
    }

    /**
     * Prueba que el método listarCategorias maneja correctamente una respuesta vacía del repositorio.
     */
    @Test
    void testListarCategoriasVacias() throws SQLException {
        // Simula que el repositorio devuelve una lista vacía
        when(categoriaRepository.listar()).thenReturn(Collections.emptyList());

        // Llama al método del servicio
        List<Categoria> resultado = categoriaService.listarCategorias();

        // Verifica que el resultado es una lista vacía no nula
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        verify(categoriaRepository, times(1)).listar();
    }

    /**
     * Prueba la búsqueda de una categoría por un ID que existe.
     */
    @Test
    void testBuscarCategoriaPorIdExistente() throws SQLException {
        // Simula que el repositorio encuentra y devuelve una categoría
        when(categoriaRepository.buscarPorId(1)).thenReturn(categoria);

        // Llama al método del servicio
        Categoria resultado = categoriaService.buscarCategoriaPorId(1);

        // Verifica que se devolvió la categoría correcta
        assertNotNull(resultado);
        assertEquals(1, resultado.getIdCategoria());
        verify(categoriaRepository, times(1)).buscarPorId(1);
    }

    /**
     * Prueba la búsqueda de una categoría por un ID que no existe.
     */
    @Test
    void testBuscarCategoriaPorIdNoExistente() throws SQLException {
        // Simula que el repositorio no encuentra la categoría y devuelve null
        when(categoriaRepository.buscarPorId(99)).thenReturn(null);

        // Llama al método del servicio
        Categoria resultado = categoriaService.buscarCategoriaPorId(99);

        // Verifica que el resultado es null
        assertNull(resultado);
        verify(categoriaRepository, times(1)).buscarPorId(99);
    }

    /**
     * Prueba el registro exitoso de una nueva categoría.
     */
    @Test
    void testRegistrarCategoria() throws SQLException {
        // Simula que el repositorio inserta la categoría y le asigna un ID
        when(categoriaRepository.insertar(any(Categoria.class))).thenReturn(categoria);

        // Crea una categoría sin ID para simular el registro
        Categoria categoriaParaRegistrar = new Categoria(null, "Laptops", "Notebooks de alto rendimiento");
        Categoria resultado = categoriaService.registrarCategoria(categoriaParaRegistrar);

        // Verifica que la categoría devuelta tiene el ID asignado
        assertNotNull(resultado);
        assertEquals(1, resultado.getIdCategoria());
        verify(categoriaRepository, times(1)).insertar(categoriaParaRegistrar);
    }

    /**
     * Prueba la actualización de una categoría existente.
     */
    @Test
    void testActualizarCategoria() throws SQLException {
        // Simula que el método de actualizar del repositorio no lanza excepciones
        doNothing().when(categoriaRepository).actualizar(categoria);

        // Verifica que la llamada al servicio no lanza excepciones
        assertDoesNotThrow(() -> categoriaService.actualizarCategoria(categoria));

        // Verifica que el método del repositorio fue invocado
        verify(categoriaRepository, times(1)).actualizar(categoria);
    }

    /**
     * Prueba la eliminación de una categoría.
     */
    @Test
    void testEliminarCategoria() throws SQLException {
        // Simula que el método de eliminar del repositorio no lanza excepciones
        doNothing().when(categoriaRepository).eliminar(1);

        // Verifica que la llamada al servicio no lanza excepciones
        assertDoesNotThrow(() -> categoriaService.eliminarCategoria(1));

        // Verifica que el método del repositorio fue invocado
        verify(categoriaRepository, times(1)).eliminar(1);
    }

    /**
     * Prueba que el servicio maneja las SQLException del repositorio
     * y las convierte en RuntimeException.
     */
    @Test
    void testListarCategoriasLanzaExcepcion() throws SQLException {
        // Simula que el repositorio lanza una SQLException
        when(categoriaRepository.listar()).thenThrow(new SQLException("Error de base de datos"));

        // Verifica que el servicio lanza una RuntimeException con el mensaje adecuado
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> categoriaService.listarCategorias());

        assertTrue(exception.getMessage().contains("Error al listar las categorías"));
        verify(categoriaRepository, times(1)).listar();
    }
}
