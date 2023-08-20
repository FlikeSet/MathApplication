package parts;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;

public class EquationDatabaseHandler {
    private static Connection connection;
    private final String jdbcUrl;
    private final String username;
    private final String password;

    public EquationDatabaseHandler(String jdbcUrl, String username, String password) {
        this.jdbcUrl = jdbcUrl;
        this.username = username;
        this.password = password;
    }

    public void connect() throws SQLException {
        connection = DriverManager.getConnection(jdbcUrl, username, password);
    }

    public void createEquationsDB() throws SQLException {
        Statement statement = connection.createStatement();
        String createDatabaseSQL = "CREATE DATABASE IF NOT EXISTS equation_db";
        statement.executeUpdate(createDatabaseSQL);
    }
    public void createEquationsTable() throws SQLException {
        Statement statement = connection.createStatement();
        String createTableSQL = "CREATE TABLE IF NOT EXISTS equations (id INT AUTO_INCREMENT PRIMARY KEY, equation_hash VARCHAR(64) UNIQUE, equation TEXT, root DOUBLE)";
        statement.executeUpdate(createTableSQL);
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder result = new StringBuilder();
        for (byte b : bytes) {
            result.append(String.format("%02x", b));
        }
        return result.toString();
    }

    public void insertEquation(String equation) throws SQLException, NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] hashBytes = md.digest(equation.getBytes());
        String hash = bytesToHex(hashBytes);
        String insertEquationSQL = "INSERT IGNORE INTO equations (equation_hash, equation, root) VALUES (?,?,?)";
        try (PreparedStatement insertStmt = connection.prepareStatement(insertEquationSQL)) {
            insertStmt.setString(1, hash);
            insertStmt.setString(2, equation);
            insertStmt.setNull(3, java.sql.Types.DOUBLE);
            insertStmt.executeUpdate();
        }
    }
    public void addRootInDatabase(String equation, double x) throws SQLException, NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] hashBytes = md.digest(equation.getBytes());
        String hash = bytesToHex(hashBytes);
        String insertEquationSQL = "INSERT IGNORE INTO equations (equation_hash,equation, root) VALUES (?,?,?) ON DUPLICATE KEY UPDATE root = VALUES(root)";

        try (PreparedStatement addStmt = connection.prepareStatement(insertEquationSQL)) {

            addStmt.setString(1, hash);
            addStmt.setString(2, equation);
            addStmt.setDouble(3, x);
            addStmt.executeUpdate();
        }
    }

    public void displayEquations() throws SQLException {
        String selectEquationsSQL = "SELECT * FROM equations WHERE root IS NOT NULL";
        try (PreparedStatement selectStmt = connection.prepareStatement(selectEquationsSQL)) {
            ResultSet resultSet = selectStmt.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String equation = resultSet.getString("equation");
                double root = resultSet.getDouble("root");
                System.out.println("ID: " + id + ", Equation: " + equation + ", Root: "+ root);
            }
        }
    }
    public void selectEquationsWithSameRoot(double x) throws SQLException {
        String selectEquationsSQL = "SELECT id, equation_hash, equation FROM equations WHERE root = ?";
        try (PreparedStatement selectStmt = connection.prepareStatement(selectEquationsSQL)) {
            selectStmt.setDouble(1, x);
            ResultSet resultSet = selectStmt.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String equation = resultSet.getString("equation");
                System.out.println("ID: " + id + ", Equation: " + equation);
            }
        }
    }
    public static String selectLastInsertEquation() throws SQLException {
        String selectEquationsSQL = "SELECT * FROM equations WHERE id = LAST_INSERT_ID()";
        try (PreparedStatement selectStmt = connection.prepareStatement(selectEquationsSQL)) {
            ResultSet resultSet = selectStmt.executeQuery();
            if (resultSet.next()) { // Move cursor to the first row
                return resultSet.getString("equation"); // Retrieve data from the current row
            } else {
                // Handle the case where no results were found
                return null; // or throw an exception
            }
        }
    }


    public void dropEquationsTable() throws SQLException {
        Statement statement = connection.createStatement();
        String dropTableQuery = "DROP TABLE IF EXISTS equations";
        statement.executeUpdate(dropTableQuery);
    }

    public void disconnect() throws SQLException {
        if (connection != null) {
            connection.close();
        }
    }
}