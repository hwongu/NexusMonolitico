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
 * Pruebas unitarias para la clase {@link UsuarioService}.
 * Se utiliza Mockito para simular el {@link UsuarioRepository} y probar
 * la lógica de la capa de servicio de forma aislada.
 */
@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioService usuarioService;

    private Usuario usuario;

    /**
     * Inicializa un objeto Usuario común para ser usado en las pruebas.
     */
    @BeforeEach
    void setUp() {
        usuario = new Usuario(1, "testuser", "password123", true);
    }

    /**
     * Prueba que el método listarUsuarios devuelve una lista de usuarios correctamente.
     */
    @Test
    void testListarUsuarios() throws SQLException {
        // Simula que el repositorio devuelve una lista con un usuario
        when(usuarioRepository.listar()).thenReturn(List.of(usuario));

        // Llama al método del servicio
        List<Usuario> resultado = usuarioService.listarUsuarios();

        // Verifica que el resultado es el esperado
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("testuser", resultado.getFirst().getUsername());
        verify(usuarioRepository, times(1)).listar();
    }

    /**
     * Prueba la búsqueda de un usuario por un ID que existe.
     */
    @Test
    void testBuscarUsuarioPorIdExistente() throws SQLException {
        // Simula que el repositorio encuentra y devuelve un usuario
        when(usuarioRepository.buscarPorId(1)).thenReturn(usuario);

        // Llama al método del servicio
        Usuario resultado = usuarioService.buscarUsuarioPorId(1);

        // Verifica que se devolvió el usuario correcto
        assertNotNull(resultado);
        assertEquals(1, resultado.getIdUsuario());
        verify(usuarioRepository, times(1)).buscarPorId(1);
    }

    /**
     * Prueba el registro exitoso de un nuevo usuario.
     */
    @Test
    void testRegistrarUsuario() throws SQLException {
        // Simula que el repositorio inserta el usuario y le asigna un ID
        when(usuarioRepository.insertar(any(Usuario.class))).thenReturn(usuario);

        // Crea un usuario sin ID para simular el registro
        Usuario usuarioParaRegistrar = new Usuario(null, "testuser", "password123", true);
        Usuario resultado = usuarioService.registrarUsuario(usuarioParaRegistrar);

        // Verifica que el usuario devuelto tiene el ID asignado
        assertNotNull(resultado);
        assertEquals(1, resultado.getIdUsuario());
        verify(usuarioRepository, times(1)).insertar(usuarioParaRegistrar);
    }

    /**
     * Prueba la actualización de un usuario existente.
     */
    @Test
    void testActualizarUsuario() throws SQLException {
        // Simula que el método de actualizar del repositorio no lanza excepciones
        doNothing().when(usuarioRepository).actualizar(usuario);

        // Verifica que la llamada al servicio no lanza excepciones
        assertDoesNotThrow(() -> usuarioService.actualizarUsuario(usuario));

        // Verifica que el método del repositorio fue invocado
        verify(usuarioRepository, times(1)).actualizar(usuario);
    }

    /**
     * Prueba la eliminación de un usuario.
     */
    @Test
    void testEliminarUsuario() throws SQLException {
        // Simula que el método de eliminar del repositorio no lanza excepciones
        doNothing().when(usuarioRepository).eliminar(1);

        // Verifica que la llamada al servicio no lanza excepciones
        assertDoesNotThrow(() -> usuarioService.eliminarUsuario(1));

        // Verifica que el método del repositorio fue invocado
        verify(usuarioRepository, times(1)).eliminar(1);
    }

    /**
     * Prueba la autenticación de un usuario con credenciales correctas.
     */
    @Test
    void testAutenticarUsuarioExitoso() throws SQLException {
        // Simula que el repositorio encuentra al usuario por sus credenciales
        when(usuarioRepository.buscarPorCredenciales("testuser", "password123")).thenReturn(usuario);

        // Llama al método de autenticación del servicio
        Usuario resultado = usuarioService.autenticarUsuario("testuser", "password123");

        // Verifica que se devolvió el usuario correcto
        assertNotNull(resultado);
        assertEquals("testuser", resultado.getUsername());
        verify(usuarioRepository, times(1)).buscarPorCredenciales("testuser", "password123");
    }

    /**
     * Prueba la autenticación de un usuario con credenciales incorrectas.
     */
    @Test
    void testAutenticarUsuarioFallido() throws SQLException {
        // Simula que el repositorio no encuentra al usuario y devuelve null
        when(usuarioRepository.buscarPorCredenciales("testuser", "wrongpassword")).thenReturn(null);

        // Llama al método de autenticación
        Usuario resultado = usuarioService.autenticarUsuario("testuser", "wrongpassword");

        // Verifica que el resultado es null
        assertNull(resultado);
        verify(usuarioRepository, times(1)).buscarPorCredenciales("testuser", "wrongpassword");
    }

    /**
     * Prueba que el servicio maneja las SQLException del repositorio
     * y las convierte en RuntimeException.
     */
    @Test
    void testListarUsuariosLanzaExcepcion() throws SQLException {
        // Simula que el repositorio lanza una SQLException
        when(usuarioRepository.listar()).thenThrow(new SQLException("Error de conexión"));

        // Verifica que el servicio lanza una RuntimeException con el mensaje adecuado
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> usuarioService.listarUsuarios());

        assertTrue(exception.getMessage().contains("Error al listar los usuarios"));
        verify(usuarioRepository, times(1)).listar();
    }
}
