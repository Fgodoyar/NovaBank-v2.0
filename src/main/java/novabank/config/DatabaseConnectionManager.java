package novabank.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnectionManager {
    //Parámetros centralizados (en producción: fichero de propiedades o env vars)
    private static final String URL = "jdbc:postgresql://localhost:5432/NovaBank";
    private static final String USER = "postgres";
    private static final String PASS = "sF0I+S2K2X4c";

    // Singleton: una única instancia del GESTOR en toda la aplicación
    private static DatabaseConnectionManager instance;

    // Constructor privado: nadie puede instanciar esta clase desde fuera
    private DatabaseConnectionManager() {}

    // Único punto de acceso global al gestor
    public static DatabaseConnectionManager getInstance() {
        if (instance == null) {
            instance = new DatabaseConnectionManager();
        }
        return instance;
    }

    // Abre una NUEVA conexión en cada llamada — NO comparte una conexión única
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }
}
