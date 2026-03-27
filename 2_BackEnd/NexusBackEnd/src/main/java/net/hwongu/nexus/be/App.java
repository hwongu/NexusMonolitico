package net.hwongu.nexus.be;

import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpServer;
import net.hwongu.nexus.be.config.ConfiguracionApp;
import net.hwongu.nexus.be.config.CorsFilter;
import net.hwongu.nexus.be.controller.CategoriaController;
import net.hwongu.nexus.be.controller.IngresoController;
import net.hwongu.nexus.be.controller.ProductoController;
import net.hwongu.nexus.be.controller.UsuarioController;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

/**
 * Punto de Entrada Principal de la Aplicación (NexusBackEnd).
 * Esta clase es la responsable de inicializar y configurar el servidor HTTP
 * nativo de Java. Actúa como el motor central que lee la configuración,
 * establece los contextos de enrutamiento para cada controlador, aplica los
 * filtros necesarios y arranca el servidor para escuchar las peticiones entrantes.
 *
 * @author Henry Wong (hwongu@gmail.com)
 */
public class App {

    /**
     * Método principal que arranca la aplicación.
     * Configura y lanza el servidor HTTP, gestionando las excepciones críticas
     * que puedan ocurrir durante el proceso de arranque.
     *
     * @param args Argumentos de la línea de comandos (no utilizados).
     */
    public static void main(String[] args) {
        try {
            int serverPort = ConfiguracionApp.obtenerEntero("server.port");
            String contextPath = ConfiguracionApp.obtenerContextPath();

            System.out.println("Iniciando el motor del servidor Nexus...");
            System.out.println("Context Path configurado: '" + (contextPath.isEmpty() ? "/" : contextPath) + "'");

            HttpServer server = HttpServer.create(new InetSocketAddress(serverPort), 0);

            // Se crea una instancia del filtro CORS que será compartida por todos los contextos.
            CorsFilter corsFilter = new CorsFilter();

            HttpContext categoriaContext = server.createContext(contextPath + "/categorias", new CategoriaController());
            categoriaContext.getFilters().add(corsFilter);

            HttpContext productoContext = server.createContext(contextPath + "/productos", new ProductoController());
            productoContext.getFilters().add(corsFilter);

            HttpContext usuarioContext = server.createContext(contextPath + "/usuarios", new UsuarioController());
            usuarioContext.getFilters().add(corsFilter);

            HttpContext ingresoContext = server.createContext(contextPath + "/ingresos", new IngresoController());
            ingresoContext.getFilters().add(corsFilter);

            // Configuración del gestor de concurrencia.
            server.setExecutor(Executors.newCachedThreadPool());

            // Arranque del servidor.
            server.start();
            System.out.println("Servidor Nexus iniciado con exito.");
            System.out.println("Escuchando en el puerto: " + serverPort);
            System.out.println("Presione Ctrl+C para detener el servidor.");
            //Apagado del servidor
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                System.out.println("Iniciando secuencia de apagado del servidor...");
                server.stop(2); // Tiempo de gracia de 2 segundos.
                System.out.println("Servidor detenido.");
            }));

        } catch (IOException e) {
            System.err.println("Error critico al iniciar el servidor: " + e.getMessage());
            e.printStackTrace();
        } catch (RuntimeException e) {
            System.err.println("Error critico durante la configuracion: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
