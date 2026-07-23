package servlet;

import java.io.IOException;
import java.net.URLEncoder;

import dao.LoginDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.LoginInfo;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private LoginDAO dao = new LoginDAO();

    public void setLoginDAO(LoginDAO dao) {
        this.dao = dao;
    }

    // テストクラス(LoginServletTest2)から参照されるフィールドとゲッター
    private LoginInfo loginInfo;
    public LoginInfo getLoginInfo() { 
        return loginInfo; 
    }

    // 半角英数記号のみ許可する正規表現パターン（全角文字を弾く）
    private static final String HALF_WIDTH_PATTERN = "^[a-zA-Z0-9!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]+$";

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        String userId = request.getParameter("userId");
        String password = request.getParameter("password");

        // 1. 未入力チェック
        if (userId == null || userId.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            String safeId = (userId != null) ? URLEncoder.encode(userId, "UTF-8") : "";
            response.sendRedirect("index.jsp?error=empty&userId=" + safeId);
            return;
        }

        // 2. 半角チェック（全角文字が含まれていれば不正ログインとして弾く）
        if (!userId.matches(HALF_WIDTH_PATTERN) || !password.matches(HALF_WIDTH_PATTERN)) {
            response.sendRedirect("index.jsp?error=wrong");
            return;
        }

        // 遷移先へ渡すデータ（テスト用情報の設定）
        loginInfo = new LoginInfo(userId, password);

        // 3. DB照合
        LoginInfo testInfo = dao.loginCheck(userId, password);
        if (testInfo != null) {
            HttpSession session = request.getSession();
            session.setAttribute("loginUser", testInfo);
            response.sendRedirect("HomeServlet");
        } else {
            // ログイン失敗
            response.sendRedirect("index.jsp?error=wrong");
        }
    }
}