package net.hwongu.nexus.be.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Centraliza la conexion a la base de datos.
 *
 * @author Henry Wong
 * GitHub @hwongu
 * https://github.com/hwongu
 */
public class ConexionDB {

    private static final String URL = ConfiguracionApp.obtener("db.url");
    private static final String USER = ConfiguracionApp.obtener("db.user");
    private static final String PASS = ConfiguracionApp.obtener("db.password");
    private static Connection conexion = null;

    private ConexionDB() {
    }

    public static Connection getInstancia() throws SQLException {
        if (conexion == null || conexion.isClosed()) {
            try {

                if (URL == null || USER == null || PASS == null) {
                    throw new SQLException("Las propiedades de la base de datos no se pudieron cargar. Verifique el archivo config.properties.");
                }

                Class.forName("org.postgresql.Driver");
                conexion = DriverManager.getConnection(URL, USER, PASS);
                System.out.println("[ConexionDB] Conexión exitosa a la base de datos.");
            } catch (ClassNotFoundException e) {
                System.err.println("[Error] No se encontró el driver de PostgreSQL.");

                throw new SQLException("Driver de PostgreSQL no encontrado.", e);
            }
        }
        return conexion;
    }
}
