package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.mindrot.jbcrypt.BCrypt;

import model.LoginInfo;

public class LoginDAO { 
    private final String JDBC_URL = "jdbc:mysql://localhost:3306/order_management";
    private final String DB_USER = "order";
    private final String DB_PASS = "1234";

    /**
     * ログインIDとパスワードの照合（大文字小文字を厳密区別、BCryptによるハッシュ照合）
     */
    public LoginInfo loginCheck(String loginId, String loginPassword) {
        LoginInfo loginInfo = null;
        
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }

        // BINARYをつけることで ID の大文字・小文字（order と Order）を厳密に区別します
        String sql = "SELECT login_id, login_password FROM user_login WHERE BINARY login_id = ?";

        try (Connection conn = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASS);
             PreparedStatement pStmt = conn.prepareStatement(sql)) {

            pStmt.setString(1, loginId);

            try (ResultSet rs = pStmt.executeQuery()) {
                if (rs.next()) {
                    String dbPassword = rs.getString("login_password");
                    boolean isMatched = false;

                    if (dbPassword != null) {
                        // 1. DBのデータがBCryptハッシュ形式（$2a$, $2b$, $2y$ など）の場合
                        if (dbPassword.startsWith("$2a$") || dbPassword.startsWith("$2b$") || dbPassword.startsWith("$2y$")) {
                            isMatched = BCrypt.checkpw(loginPassword, dbPassword);
                        } else {
                            // 2. 既存の平文データ（1234など）の場合、そのまま比較（エラー回避）
                            isMatched = dbPassword.equals(loginPassword);
                        }
                    }

                    if (isMatched) {
                        loginInfo = new LoginInfo();
                        loginInfo.setLoginId(rs.getString("login_id"));
                        loginInfo.setLoginPassword(dbPassword);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        
        return loginInfo; 
    }

    /**
     * 新規ユーザー登録（パスワードをBCryptでハッシュ化して保存）
     */
    public boolean registerUser(String loginId, String plainPassword) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }

        // パスワードをハッシュ化
        String hashedPassword = BCrypt.hashpw(plainPassword, BCrypt.gensalt());

        String sql = "INSERT INTO user_login (login_id, login_password) VALUES (?, ?)";

        try (Connection conn = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASS);
             PreparedStatement pStmt = conn.prepareStatement(sql)) {

            pStmt.setString(1, loginId);
            pStmt.setString(2, hashedPassword);

            int result = pStmt.executeUpdate();
            return result > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * IDが既に存在するかチェックする（大文字小文字を厳密区別）
     */
    public boolean isUserIdExists(String loginId) {
        String sql = "SELECT COUNT(*) FROM user_login WHERE BINARY login_id = ?";
        try (Connection conn = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASS);
             PreparedStatement pStmt = conn.prepareStatement(sql)) {

            pStmt.setString(1, loginId);
            try (ResultSet rs = pStmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}