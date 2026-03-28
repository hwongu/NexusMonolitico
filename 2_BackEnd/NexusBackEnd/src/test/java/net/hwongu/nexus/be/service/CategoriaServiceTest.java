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
 * Valida la logica de CategoriaService.
 *
 * @author Henry Wong
 * GitHub @hwongu
 * https://github.com/hwongu
 */
@ExtendWith(MockitoExtension.class)
class CategoriaServiceTest {

    @Mock
    private CategoriaRepository categoriaRepository;

    @InjectMocks
    private CategoriaService categoriaService;

    private Categoria categoria;

    @BeforeEach
    void setUp() {
        categoria = new Categoria(1, "Laptops", "Notebooks de alto rendimiento");
    }

    @Test
    void testListarCategorias() throws SQLException {

        when(categoriaRepository.listar()).thenReturn(List.of(categoria));

        List<Categoria> resultado = categoriaService.listarCategorias();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Laptops", resultado.getFirst().getNombre());
        verify(categoriaRepository, times(1)).listar();
    }

    @Test
    void testListarCategoriasVacias() throws SQLException {

        when(categoriaRepository.listar()).thenReturn(Collections.emptyList());

        List<Categoria> resultado = categoriaService.listarCategorias();

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        verify(categoriaRepository, times(1)).listar();
    }

    @Test
    void testBuscarCategoriaPorIdExistente() throws SQLException {

        when(categoriaRepository.buscarPorId(1)).thenReturn(categoria);

        Categoria resultado = categoriaService.buscarCategoriaPorId(1);

        assertNotNull(resultado);
        assertEquals(1, resultado.getIdCategoria());
        verify(categoriaRepository, times(1)).buscarPorId(1);
    }

    @Test
    void testBuscarCategoriaPorIdNoExistente() throws SQLException {

        when(categoriaRepository.buscarPorId(99)).thenReturn(null);

        Categoria resultado = categoriaService.buscarCategoriaPorId(99);

        assertNull(resultado);
        verify(categoriaRepository, times(1)).buscarPorId(99);
    }

    @Test
    void testRegistrarCategoria() throws SQLException {

        when(categoriaRepository.insertar(any(Categoria.class))).thenReturn(categoria);

        Categoria categoriaParaRegistrar = new Categoria(null, "Laptops", "Notebooks de alto rendimiento");
        Categoria resultado = categoriaService.registrarCategoria(categoriaParaRegistrar);

        assertNotNull(resultado);
        assertEquals(1, resultado.getIdCategoria());
        verify(categoriaRepository, times(1)).insertar(categoriaParaRegistrar);
    }

    @Test
    void testActualizarCategoria() throws SQLException {

        doNothing().when(categoriaRepository).actualizar(categoria);

        assertDoesNotThrow(() -> categoriaService.actualizarCategoria(categoria));

        verify(categoriaRepository, times(1)).actualizar(categoria);
    }

    @Test
    void testEliminarCategoria() throws SQLException {

        doNothing().when(categoriaRepository).eliminar(1);

        assertDoesNotThrow(() -> categoriaService.eliminarCategoria(1));

        verify(categoriaRepository, times(1)).eliminar(1);
    }

    @Test
    void testListarCategoriasLanzaExcepcion() throws SQLException {

        when(categoriaRepository.listar()).thenThrow(new SQLException("Error de base de datos"));

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> categoriaService.listarCategorias());

        assertTrue(exception.getMessage().contains("Error al listar las categorías"));
        verify(categoriaRepository, times(1)).listar();
    }
}
