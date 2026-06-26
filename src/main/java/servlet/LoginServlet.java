package servlet;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
    	request.setCharacterEncoding("UTF-8");
        
        // 入力値を取得
        String userId = request.getParameter("userId");
        String password = request.getParameter("password");

        // 簡単な認証チェック（※本来はデータベースと照合します）
        if ("order".equals(userId) && "1234".equals(password)) {
            // ログイン成功時の処理
            response.sendRedirect("HomeServlet");
        } else {
            // ログイン失敗時の処理（画面に戻す）
            response.sendRedirect("index.jsp?error=1");
        }
    }
}
