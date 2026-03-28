package net.hwongu.nexus.be.service;

import net.hwongu.nexus.be.entity.Usuario;
import net.hwongu.nexus.be.repository.UsuarioRepository;
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
 * Valida la logica de UsuarioService.
 *
 * @author Henry Wong
 * GitHub @hwongu
 * https://github.com/hwongu
 */
@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioService usuarioService;

    private Usuario usuario;

    @BeforeEach
    void setUp() {
        usuario = new Usuario(1, "testuser", "password123", true);
    }

    @Test
    void testListarUsuarios() throws SQLException {

        when(usuarioRepository.listar()).thenReturn(List.of(usuario));

        List<Usuario> resultado = usuarioService.listarUsuarios();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("testuser", resultado.getFirst().getUsername());
        verify(usuarioRepository, times(1)).listar();
    }

    @Test
    void testBuscarUsuarioPorIdExistente() throws SQLException {

        when(usuarioRepository.buscarPorId(1)).thenReturn(usuario);

        Usuario resultado = usuarioService.buscarUsuarioPorId(1);

        assertNotNull(resultado);
        assertEquals(1, resultado.getIdUsuario());
        verify(usuarioRepository, times(1)).buscarPorId(1);
    }

    @Test
    void testRegistrarUsuario() throws SQLException {

        when(usuarioRepository.insertar(any(Usuario.class))).thenReturn(usuario);

        Usuario usuarioParaRegistrar = new Usuario(null, "testuser", "password123", true);
        Usuario resultado = usuarioService.registrarUsuario(usuarioParaRegistrar);

        assertNotNull(resultado);
        assertEquals(1, resultado.getIdUsuario());
        verify(usuarioRepository, times(1)).insertar(usuarioParaRegistrar);
    }

    @Test
    void testActualizarUsuario() throws SQLException {

        doNothing().when(usuarioRepository).actualizar(usuario);

        assertDoesNotThrow(() -> usuarioService.actualizarUsuario(usuario));

        verify(usuarioRepository, times(1)).actualizar(usuario);
    }

    @Test
    void testEliminarUsuario() throws SQLException {

        doNothing().when(usuarioRepository).eliminar(1);

        assertDoesNotThrow(() -> usuarioService.eliminarUsuario(1));

        verify(usuarioRepository, times(1)).eliminar(1);
    }

    @Test
    void testAutenticarUsuarioExitoso() throws SQLException {

        when(usuarioRepository.buscarPorCredenciales("testuser", "password123")).thenReturn(usuario);

        Usuario resultado = usuarioService.autenticarUsuario("testuser", "password123");

        assertNotNull(resultado);
        assertEquals("testuser", resultado.getUsername());
        verify(usuarioRepository, times(1)).buscarPorCredenciales("testuser", "password123");
    }

    @Test
    void testAutenticarUsuarioFallido() throws SQLException {

        when(usuarioRepository.buscarPorCredenciales("testuser", "wrongpassword")).thenReturn(null);

        Usuario resultado = usuarioService.autenticarUsuario("testuser", "wrongpassword");

        assertNull(resultado);
        verify(usuarioRepository, times(1)).buscarPorCredenciales("testuser", "wrongpassword");
    }

    @Test
    void testListarUsuariosLanzaExcepcion() throws SQLException {

        when(usuarioRepository.listar()).thenThrow(new SQLException("Error de conexión"));

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> usuarioService.listarUsuarios());

        assertTrue(exception.getMessage().contains("Error al listar los usuarios"));
        verify(usuarioRepository, times(1)).listar();
    }
}
