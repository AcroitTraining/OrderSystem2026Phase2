package servlet;

import java.io.IOException;

import dao.LoginDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/RegisterServlet")
public class RegisterServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private LoginDAO dao = new LoginDAO();

    private static final String HALF_WIDTH_PATTERN = "^[a-zA-Z0-9!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]+$";

    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        String userId = request.getParameter("userId");
        String password = request.getParameter("password");

        // 未入力チェック
        if (userId == null || userId.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            response.sendRedirect("register.jsp?error=empty");
            return;
        }

        // 半角英数・記号制限チェック（全角「ｏｒｄｅｒ」などを弾く）
        if (!userId.matches(HALF_WIDTH_PATTERN) || !password.matches(HALF_WIDTH_PATTERN)) {
            response.sendRedirect("register.jsp?error=invalid_char");
            return;
        }

        // 重複チェック
        if (dao.isUserIdExists(userId)) {
            response.sendRedirect("register.jsp?error=exists");
            return;
        }

        // DBへ登録（ハッシュ化して登録されます）
        boolean isSuccess = dao.registerUser(userId, password);

        if (isSuccess) {
            // 登録成功したらログイン画面へ移動
            response.sendRedirect("index.jsp?msg=registered");
        } else {
            response.sendRedirect("register.jsp?error=db_error");
        }
    }
}