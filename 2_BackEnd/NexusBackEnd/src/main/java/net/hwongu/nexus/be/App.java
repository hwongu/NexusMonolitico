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
 * Inicia el microservicio NexusBackEnd.
 *
 * @author Henry Wong
 * GitHub @hwongu
 * https://github.com/hwongu
 */
public class App {

    public static void main(String[] args) {
        try {
            int serverPort = ConfiguracionApp.obtenerEntero("server.port");
            String contextPath = ConfiguracionApp.obtenerContextPath();

            System.out.println("Iniciando el motor del servidor Nexus...");
            System.out.println("Context Path configurado: '" + (contextPath.isEmpty() ? "/" : contextPath) + "'");

            HttpServer server = HttpServer.create(new InetSocketAddress(serverPort), 0);

            CorsFilter corsFilter = new CorsFilter();

            HttpContext categoriaContext = server.createContext(contextPath + "/categorias", new CategoriaController());
            categoriaContext.getFilters().add(corsFilter);

            HttpContext productoContext = server.createContext(contextPath + "/productos", new ProductoController());
            productoContext.getFilters().add(corsFilter);

            HttpContext usuarioContext = server.createContext(contextPath + "/usuarios", new UsuarioController());
            usuarioContext.getFilters().add(corsFilter);

            HttpContext ingresoContext = server.createContext(contextPath + "/ingresos", new IngresoController());
            ingresoContext.getFilters().add(corsFilter);

            server.setExecutor(Executors.newCachedThreadPool());

            server.start();
            System.out.println("Servidor Nexus iniciado con exito.");
            System.out.println("Escuchando en el puerto: " + serverPort);
            System.out.println("Presione Ctrl+C para detener el servidor.");

            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                System.out.println("Iniciando secuencia de apagado del servidor...");
                server.stop(2);
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
