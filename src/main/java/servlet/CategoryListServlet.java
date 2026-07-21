package servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import dao.CategoryListDAO;
import dao.DBConnectionUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.CategoryListInfo;

@WebServlet("/CategoryListServlet")
public class CategoryListServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected Connection createConnection() throws SQLException {
        return DBConnectionUtil.getConnection();
    }

    protected CategoryListDAO createDAO(Connection conn) {
        return new CategoryListDAO(conn);
    }

    // 🟢 画面表示処理（カテゴリ一覧の取得）
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        try (Connection conn = createConnection()) {
            CategoryListDAO dao = createDAO(conn);
            
            // DAOを使ってDBからカテゴリリストを取得
            List<CategoryListInfo> cList = dao.findAllCategory();
            
            // JSPで ${cList} として参照できるようにリクエスト属性へ格納
            request.setAttribute("cList", cList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        // categoryList.jsp へフォワード
        request.getRequestDispatcher("/WEB-INF/jsp/categoryList.jsp").forward(request, response);
    }

    // 🟢 表示切り替え・削除ボタン押下時の処理
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String cid = request.getParameter("categoryId");
        int categoryId = Integer.parseInt(cid);
        String action = request.getParameter("action");

        try (Connection conn = createConnection()) {
            CategoryListDAO dao = createDAO(conn);
            if ("表示切り替え".equals(action)) {
                dao.updateCategoryDisplayOrder(categoryId);
            } else if ("削除".equals(action)) {
                dao.updateCategoryDeleteFlag(categoryId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        // 処理後は再度一覧画面をリロード（再取得）
        response.sendRedirect("CategoryListServlet");
    }
}