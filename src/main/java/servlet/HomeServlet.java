package servlet;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * ホーム画面を表示するサーブレット
 */
@WebServlet("/HomeServlet")
public class HomeServlet extends HttpServlet { private static final long serialVersionUID = 1L;
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
    	request.getRequestDispatcher("WEB-INF/jsp/home.jsp").forward(request, response);
    }

    /**
     * POSTリクエストが来てもGETと同じ処理をするようにマッピング
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        doGet(request, response);
    }
}