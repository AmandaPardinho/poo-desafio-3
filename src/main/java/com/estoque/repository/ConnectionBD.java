package com.estoque.repository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class ConnectionBD {

    private static final String URL = "jdbc:h2:./estoque_db";
    private static final String USER = "sa";
    private static final String PASSWORD = "";

    public static Connection getConnect() throws SQLException {
        try {
            Class.forName("org.h2.Driver");
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            throw new SQLException("Driver H2 não encontrado", e);
        }
    }

    public static void initializeDB() {
        try (Connection conn = getConnect();
                Statement stmt = conn.createStatement()) {

            String sql = readSqlFile("schema.sql");

            stmt.execute(sql);
            System.out.println("Banco inicializado a partir do schema.sql!");

        } catch (SQLException | IOException e) {
            System.err.println("Erro ao inicializar banco: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static String readSqlFile(String fileName) throws IOException {
        Path path = Paths.get("schema.sql");
        return Files.readString(path);
    }

    public static boolean connectionTest() {
        try (Connection conn = getConnect()) {
            return conn != null && !conn.isClosed();
        } catch (SQLException e) {
            System.err.println("Erro ao testar conexão: " + e.getMessage());
            return false;
        }
    }
}
