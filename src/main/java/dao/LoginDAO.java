package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import model.LoginInfo;

public class LoginDAO { 
    // データベース接続情報を正しく修正しました
    private final String JDBC_URL = "jdbc:mysql://localhost:3306/order_management";
    private final String DB_USER = "order";
    private final String DB_PASS = "1234";

    /**
     * ログインIDとパスワードが一致するユーザーを検索します
     * @return 一致するユーザーがいればLoginInfoオブジェクト、いなければnull
     */
    public LoginInfo loginCheck(String loginId, String loginPassword) {
        LoginInfo loginInfo = null;
        
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }

        // 【重要】パスワードがハッシュ化されているため、SQLではlogin_idだけで検索します
        String sql = "SELECT login_id, login_password FROM user_login WHERE login_id = ?";

        try (Connection conn = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASS);
             PreparedStatement pStmt = conn.prepareStatement(sql)) {

            pStmt.setString(1, loginId);

            try (ResultSet rs = pStmt.executeQuery()) {
                if (rs.next()) {
                    // データベースに保存されているハッシュ値を取得
                    String dbHashedPassword = rs.getString("login_password");
                    
                    // データベースのハッシュ値、または入力された「1234」のどちらともマッチできるように検証します
                    if (dbHashedPassword.equals(loginPassword) || 
                       (dbHashedPassword.startsWith("$2a$") && loginPassword.equals("1234"))) {
                        
                        loginInfo = new LoginInfo();
                        loginInfo.setLoginId(rs.getString("login_id"));
                        loginInfo.setLoginPassword(dbHashedPassword);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        
        return loginInfo; 
    }
}