package net.hwongu.nexus.be.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Gestiona la conexión a la base de datos de la aplicación.
 * Esta clase utiliza un patrón Singleton para proporcionar una única instancia
 * de la conexión (Connection) a través de un método estático. Lee la
 * configuración de la base de datos desde la clase {@link ConfiguracionApp}.
 *
 * @author Henry Wong (hwongu@gmail.com)
 */
public class ConexionDB {

    // Las constantes ahora apuntan a las claves del archivo de propiedades.
    private static final String URL = ConfiguracionApp.obtener("db.url");
    private static final String USER = ConfiguracionApp.obtener("db.user");
    private static final String PASS = ConfiguracionApp.obtener("db.password");
    private static Connection conexion = null;

    /**
     * Constructor privado para prevenir la instanciación directa de la clase.
     * Este es un requisito del patrón Singleton.
     */
    private ConexionDB() {
    }

    /**
     * Obtiene una instancia de la conexión a la base de datos.
     * Si la conexión no existe o está cerrada, crea una nueva. Si ya existe
     * una conexión activa, la reutiliza.
     *
     * @return Una instancia de {@link Connection} lista para ser usada.
     * @throws SQLException Si ocurre un error al establecer la conexión, si las
     *                      propiedades de configuración no se encuentran, o si
     *                      el driver de la base de datos no está disponible.
     */
    public static Connection getInstancia() throws SQLException {
        if (conexion == null || conexion.isClosed()) {
            try {
                // Validar que las propiedades se hayan cargado correctamente
                if (URL == null || USER == null || PASS == null) {
                    throw new SQLException("Las propiedades de la base de datos no se pudieron cargar. Verifique el archivo config.properties.");
                }
                
                Class.forName("org.postgresql.Driver");
                conexion = DriverManager.getConnection(URL, USER, PASS);
                System.out.println("[ConexionDB] Conexión exitosa a la base de datos.");
            } catch (ClassNotFoundException e) {
                System.err.println("[Error] No se encontró el driver de PostgreSQL.");
                // Lanzamos una SQLException para que la capa de servicio la maneje.
                throw new SQLException("Driver de PostgreSQL no encontrado.", e);
            }
        }
        return conexion;
    }
}
