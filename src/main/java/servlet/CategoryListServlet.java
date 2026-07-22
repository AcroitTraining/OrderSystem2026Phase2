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

    // 画面表示処理（カテゴリ一覧の取得）
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        try (Connection conn = createConnection()) {
            CategoryListDAO dao = createDAO(conn);
            
            // DAOを使ってDBからカテゴリリストを取得（delete_flag = 1 のみ）
            List<CategoryListInfo> cList = dao.findAllCategory();
            
            // JSPで ${cList} として参照できるようにリクエスト属性へ格納
            request.setAttribute("cList", cList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        // categoryList.jsp へフォワード
        request.getRequestDispatcher("/WEB-INF/jsp/categoryList.jsp").forward(request, response);
    }

    // 削除ボタン押下時の処理
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String cid = request.getParameter("categoryId");
        String action = request.getParameter("action");

        if (cid != null && "削除".equals(action)) {
            try (Connection conn = createConnection()) {
                int categoryId = Integer.parseInt(cid);
                CategoryListDAO dao = createDAO(conn);
                
                // delete_flag を 0 に変更（削除処理）
                dao.updateCategoryDeleteFlag(categoryId);
            } catch (SQLException | NumberFormatException e) {
                e.printStackTrace();
            }
        }
        
        // 処理後は再度一覧画面をリロード（再取得）
        response.sendRedirect("CategoryListServlet");
    }
}