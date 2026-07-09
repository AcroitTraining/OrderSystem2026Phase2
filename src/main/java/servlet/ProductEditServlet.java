package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;

import dao.ProductEditDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.ProductEditInfo;

@WebServlet("/ProductEditServlet")
public class ProductEditServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String action = request.getParameter("action");
        
        // --- 【追加】JavaScript(Fetch API)からの非同期重複チェック処理 ---
        if ("checkDuplicate".equals(action)) {
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            String productName = request.getParameter("productName");
            String pidStr = request.getParameter("productId");
            int productId = (pidStr != null && !pidStr.isEmpty()) ? Integer.parseInt(pidStr) : 0;
            
            ProductEditDAO dao = null;
            boolean isDuplicate = false;
            try {
                dao = new ProductEditDAO();
                // DAOで重複判定SQLを実行
                isDuplicate = dao.isProductNameExists(productName, productId);
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                if (dao != null && dao.getConnection() != null) {
                    try { dao.getConnection().close(); } catch (SQLException e) { e.printStackTrace(); }
                }
            }
            
            // JSON形式 {"duplicate": true/false} で結果を返却
            try (PrintWriter out = response.getWriter()) {
                out.print("{\"duplicate\": " + isDuplicate + "}");
                out.flush();
            }
            return;
        }

        // --- 従来の通常画面表示ロジック ---
        String pid = request.getParameter("productId");
        ProductEditDAO dao = null;

        try {
            dao = new ProductEditDAO();
            List<ProductEditInfo.ToppingMaster> allToppings = dao.findAllToppings();
            request.setAttribute("allToppings", allToppings);
            if (pid == null || pid.isEmpty()) {
                ProductEditInfo emptyInfo = new ProductEditInfo();
                emptyInfo.setProductId(0); // 0は新規の印
                emptyInfo.setProductName("");
                emptyInfo.setProductPrice(0);
                emptyInfo.setCategoryName("");
                
                request.setAttribute("productEditInfo", emptyInfo);
                request.getRequestDispatcher("WEB-INF/jsp/productEdit.jsp").forward(request, response);
                return;
            }

            int productId = Integer.parseInt(pid);
            ProductEditInfo info = dao.findProductDetails(productId);

            if (info != null) {
                request.setAttribute("productEditInfo", info);
                request.getRequestDispatcher("WEB-INF/jsp/productEdit.jsp").forward(request, response);
            } else {
                response.sendRedirect("ProductListServlet");
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

        String productIdStr = request.getParameter("productId");
        String productName = request.getParameter("productName");
        String productPriceStr = request.getParameter("productPrice");
        String categoryName = request.getParameter("categoryName");
        String[] toppingIds = request.getParameterValues("toppingId");

        int productPrice = (productPriceStr != null && !productPriceStr.isEmpty()) ? Integer.parseInt(productPriceStr) : 0;
        ProductEditDAO dao = null;

        try {
            dao = new ProductEditDAO();
            int productId = (productIdStr == null || productIdStr.isEmpty()) ? 0 : Integer.parseInt(productIdStr);
            
            // --- サーバーサイドでの最終防衛バリデーション ---
            if (productName == null || productName.trim().isEmpty() || productName.length() > 18 
                || productPrice < 0 || productPrice > 99999 
                || categoryName == null || categoryName.isEmpty() 
                || dao.isProductNameExists(productName, productId)) {
                
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "入力内容に不正な値が含まれています。");
                return;
            }
            
            // productIdStrが空、または "0" の場合は新規作成を行う
            if (productId == 0) {
                dao.insertProductDetails(productName, productPrice, categoryName, toppingIds);
            } else {
                // 既存の商品の更新処理
                dao.updateProductDetails(productId, productName, productPrice, categoryName, toppingIds);
            }
            
            response.sendRedirect("ProductListServlet");
            
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