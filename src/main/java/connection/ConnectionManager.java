package connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionManager {
    private static Connection connection;

    private static Connection createConnection() throws SQLException {
        String url = "jdbc:postgresql://localhost/JDBCTest";
        Properties props = new Properties();
        props.setProperty("user", "user");
        props.setProperty("password", "password");
        return DriverManager.getConnection(url, props);
    }

    public static Connection getConnection()throws SQLException {
        if (connection == null) {
            connection = createConnection();
        }
        return connection;
    }
}
