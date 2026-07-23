package servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Set;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebFilter("/*")
public class AllSessionError implements Filter {

    private static final String SESSION_LOGIN_KEY = "loginUser";
    private static final String ERROR_PAGE = "/allSessionError.jsp";

    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/order_management";
    private static final String DB_USER = "order";
    private static final String DB_PASS = "1234";

    // ログインチェックをスキップするパス（未ログインでも表示・実行していい画面／サーブレット）
    private static final Set<String> SKIP_LOGIN_CHECK = Set.of(
            "/index.jsp", "/LoginServlet", "/register.jsp", "/RegisterServlet"
    );

    // DB接続チェック自体もスキップするパス（DBを使わずに表示できる画面）
    private static final Set<String> SKIP_DB_CHECK = Set.of(
            "/index.jsp", "/register.jsp"
    );

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        String path = req.getServletPath();

        // 静的リソース(css/js/image)とエラー画面自身はノーチェックで通す（無限ループ防止）
        if (path.startsWith("/css/") || path.startsWith("/js/") || path.startsWith("/image/")
                || path.equals(ERROR_PAGE) || path.isEmpty() || path.equals("/")) {
            chain.doFilter(request, response);
            return;
        }

        // 1. DB接続チェック（index.jsp, register.jsp は対象外）
        if (!SKIP_DB_CHECK.contains(path)) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                try (Connection conn = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASS)) {
                }
            } catch (Exception e) {
                e.printStackTrace();
                req.getRequestDispatcher(ERROR_PAGE).forward(req, res);
                return;
            }
        }

        // 2. ログインチェック（index.jsp / LoginServlet / register.jsp / RegisterServlet は対象外）
        if (!SKIP_LOGIN_CHECK.contains(path)) {
            HttpSession session = req.getSession(false);
            boolean isLoggedIn = session != null && session.getAttribute(SESSION_LOGIN_KEY) != null;
            if (!isLoggedIn) {
                req.getRequestDispatcher(ERROR_PAGE).forward(req, res);
                return;
            }
        }

        // 3. キャッシュ無効化（ログアウト後の「戻る」対策）
        res.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        res.setHeader("Pragma", "no-cache");
        res.setDateHeader("Expires", 0);

        // 4. Servlet内の例外もキャッチして共通エラー画面へ
        try {
            chain.doFilter(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            if (!res.isCommitted()) {
                req.getRequestDispatcher(ERROR_PAGE).forward(req, res);
            }
        }
    }
}