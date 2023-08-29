import java.sql.*;
import java.util.Scanner;

public class ConnectionFactory {
    public static final String URL = System.getenv("url");
    public static final String USER = System.getenv("user");
    public static final String PASSWORD = System.getenv("password");

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException("Error connecting to the database", e);
        }
    }
    public static void main(String[] args) throws SQLException {
        getConnection();
        DAO userDao = new UserDaoImpl();
        ConnectionFactory connectionFactory = new ConnectionFactory();
        userDao.run();


    }
}
