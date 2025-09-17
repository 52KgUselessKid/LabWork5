package DB;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Base64;

public class DbStuff {
    static String url = "jdbc:postgresql://localhost:5432/studs",
    user = "postgres",
    password = "pred_game2k02";

    static Connection connection;

    public static void connectToDB()
    {
        try {
            Connection conn = DriverManager.getConnection(url, user, password);
            if (conn != null) {
                System.out.println("✅ Подключение успешно!");
                connection = conn;
            } else {
                System.out.println("❌ Не удалось подключиться.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static ResultSet exeQuery(String query) throws SQLException {
        var stmt = connection.createStatement();
        return stmt.executeQuery(query);
    }

    public static void exeQueryVoid(String query) throws SQLException {
        var stmt = connection.createStatement();
        stmt.execute(query);
    }

    public static String toHash(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            byte[] hashedBytes = md.digest((password).getBytes(StandardCharsets.UTF_8));

            // конвертация байтов в HEX
            StringBuilder sb = new StringBuilder();
            for (byte b : hashedBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-512 не поддерживается!", e);
        }
    }

    public static String getSalt()
    {
        byte[] salt_b = new byte[16]; // 128 бит
        new SecureRandom().nextBytes(salt_b);
        return Base64.getEncoder().encodeToString(salt_b);
    }
}