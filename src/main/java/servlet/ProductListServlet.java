package servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import dao.ProductListDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.ProductListInfo;
import model.ProductListLogic;

@WebServlet("/ProductListServlet")
public class ProductListServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private ProductListDAO dao = new ProductListDAO();
    private ProductListLogic logic = new ProductListLogic();

    public void setProductListDAO(ProductListDAO dao) {
        this.dao = dao;
    }

    public void setLogic(ProductListLogic logic) {
        this.logic = logic;
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        int currentCategoryId = 0;
        String categoryIdParam = request.getParameter("categoryId");

        if (categoryIdParam != null && !categoryIdParam.isEmpty()) {
            try {
                currentCategoryId = Integer.parseInt(categoryIdParam);
            } catch (NumberFormatException e) {
                currentCategoryId = 0;
            }
        }

        try {
            // カテゴリタブ一覧を取得
            List<ProductListInfo> categoryList = dao.findAllCategories();

            // 全商品を取得
            List<ProductListInfo> pList = dao.findAllProduct();

            // Logicでカテゴリによる絞り込み
            List<ProductListInfo> filteredList = logic.filterProducts(pList, currentCategoryId);

            request.setAttribute("categoryList", categoryList);
            request.setAttribute("pList", filteredList);
            request.setAttribute("currentCategoryId", currentCategoryId);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        request.getRequestDispatcher("/WEB-INF/jsp/productList.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String pid = request.getParameter("productId");

        if (pid == null || pid.isEmpty()) {
            response.sendRedirect("ProductListServlet");
            return;
        }

        int productId = Integer.parseInt(pid);
        String action = request.getParameter("action");

        try {
            if ("表示切り替え".equals(action)) {
                dao.updateProductDisplayFlag(productId);
            } else if ("削除".equals(action)) {
                dao.updateProductDeleteFlag(productId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // 選択中のカテゴリIDを維持してリダイレクト
        String currentCategoryId = request.getParameter("categoryId");

        if (currentCategoryId != null && !currentCategoryId.isEmpty()) {
            response.sendRedirect("ProductListServlet?categoryId=" + currentCategoryId);
        } else {
            response.sendRedirect("ProductListServlet");
        }
    }
}