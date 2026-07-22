package servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import dao.CategoryEditDAO;
import dao.DBConnectionUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.CategoryEdit;

@WebServlet("/CategoryEditServlet")
public class CategoryEditServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private CategoryEditDAO dao = new CategoryEditDAO();

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String cid = request.getParameter("categoryId");
        try (Connection conn = DBConnectionUtil.getConnection()) {
            // 新規登録モード（categoryIdがない場合）
            if (cid == null || cid.isEmpty()) {
                CategoryEdit emptyInfo = new CategoryEdit();
                emptyInfo.setCategoryId(0); // 0を新規登録の目印とする
                emptyInfo.setCategoryName("");
                request.setAttribute("categoryEditInfo", emptyInfo);
                request.getRequestDispatcher("WEB-INF/jsp/categoryEdit.jsp").forward(request, response);
                return;
            }
            // 編集モード（categoryIdがある場合）
            int categoryId = Integer.parseInt(cid);
            CategoryEdit info = dao.findCategoryDetails(conn, categoryId);
            if (info != null) {
                request.setAttribute("categoryEditInfo", info);
                request.getRequestDispatcher("WEB-INF/jsp/categoryEdit.jsp").forward(request, response);
            } else {
                response.sendRedirect("CategoryListServlet");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String categoryIdStr = request.getParameter("categoryId");
        String categoryName = request.getParameter("categoryName");

        try (Connection conn = DBConnectionUtil.getConnection()) {
            if (categoryIdStr == null || categoryIdStr.isEmpty() || "0".equals(categoryIdStr)) {
                // 新規追加
                dao.insertCategoryDetails(conn, categoryName);
            } else {
                // 更新
                int categoryId = Integer.parseInt(categoryIdStr);
                dao.updateCategoryDetails(conn, categoryId, categoryName);
            }
            response.sendRedirect("CategoryListServlet");
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}