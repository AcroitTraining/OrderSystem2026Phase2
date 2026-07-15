package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

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

    private ProductEditDAO dao = new ProductEditDAO();

    public void setProductEditDAO(ProductEditDAO dao) {
        this.dao = dao;
    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if ("checkDuplicate".equals(request.getParameter("action"))) {
            sendJsonDuplicateCheck(request, response);
            return;
        }

        try {
            request.setAttribute("allToppings", dao.findAllToppings());

            String pid = request.getParameter("productId");
            ProductEditInfo info = (pid == null || pid.isEmpty())
                    ? new ProductEditInfo()
                    : dao.findProductDetails(Integer.parseInt(pid));

            if (info == null) {
                response.sendRedirect("ProductListServlet");
                return;
            }

            request.setAttribute("productEditInfo", info);
            request.getRequestDispatcher("WEB-INF/jsp/productEdit.jsp").forward(request, response);

        } catch (SQLException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        int productId = toInt(request.getParameter("productId"));
        int productPrice = toInt(request.getParameter("productPrice"));

        try {
            boolean saved = dao.saveProduct(
                    productId,
                    request.getParameter("productName"),
                    productPrice,
                    request.getParameter("categoryName"),
                    request.getParameterValues("toppingId"));

            if (!saved) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "入力内容に不正な値が含まれています。");
                return;
            }
            response.sendRedirect("ProductListServlet");

        } catch (SQLException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    private void sendJsonDuplicateCheck(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        boolean isDuplicate = false;
        try {
            isDuplicate = dao.isProductNameExists(
                    request.getParameter("productName"),
                    toInt(request.getParameter("productId")));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try (PrintWriter out = response.getWriter()) {
            out.print("{\"duplicate\": " + isDuplicate + "}");
        }
    }

    private int toInt(String s) {
        return (s != null && !s.isEmpty()) ? Integer.parseInt(s) : 0;
    }
}