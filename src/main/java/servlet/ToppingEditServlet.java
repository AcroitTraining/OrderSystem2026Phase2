package servlet;

import java.io.IOException;
import java.sql.SQLException;

import dao.ToppingEditDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.ToppingEditInfo;

@WebServlet("/ToppingEditServlet")
public class ToppingEditServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String tid = request.getParameter("toppingId");
        ToppingEditDAO dao = null;

        try {
            dao = new ToppingEditDAO();

            // 新規登録モード（toppingIdがない場合）
            if (tid == null || tid.isEmpty()) {
                ToppingEditInfo emptyInfo = new ToppingEditInfo();
                emptyInfo.setToppingId(0); // 0を新規登録の目印とする
                emptyInfo.setToppingName("");
                emptyInfo.setToppingPrice(0);
                
                request.setAttribute("toppingEditInfo", emptyInfo);
                request.getRequestDispatcher("WEB-INF/jsp/toppingEdit.jsp").forward(request, response);
                return;
            }

            // 編集モード（toppingIdがある場合）
            int toppingId = Integer.parseInt(tid);
            ToppingEditInfo info = dao.findToppingDetails(toppingId);

            if (info != null) {
                request.setAttribute("toppingEditInfo", info);
                request.getRequestDispatcher("WEB-INF/jsp/toppingEdit.jsp").forward(request, response);
            } else {
                response.sendRedirect("ToppingListServlet");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        } finally {
            if (dao != null && dao.getConnection() != null) {
                try { dao.getConnection().close(); } catch (SQLException e) { e.printStackTrace(); }
            }
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        request.setCharacterEncoding("UTF-8");

        String toppingIdStr = request.getParameter("toppingId");
        String toppingName = request.getParameter("toppingName");
        String toppingPriceStr = request.getParameter("toppingPrice");

        int toppingPrice = (toppingPriceStr != null && !toppingPriceStr.isEmpty()) ? Integer.parseInt(toppingPriceStr) : 0;
        ToppingEditDAO dao = null;

        try {
            dao = new ToppingEditDAO();
            
            if (toppingIdStr == null || toppingIdStr.isEmpty() || "0".equals(toppingIdStr)) {
                // 新規追加
                dao.insertToppingDetails(toppingName, toppingPrice);
            } else {
                // 更新
                int toppingId = Integer.parseInt(toppingIdStr);
                dao.updateToppingDetails(toppingId, toppingName, toppingPrice);
            }
            
            response.sendRedirect("ToppingListServlet");
            
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        } finally {
            if (dao != null && dao.getConnection() != null) {
                try { dao.getConnection().close(); } catch (SQLException e) { e.printStackTrace(); }
            }
        }
    }
}