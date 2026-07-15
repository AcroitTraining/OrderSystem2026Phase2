package servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import dao.EditOrderDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.EditOrderInfo;
import model.EditOrderLogic;

@WebServlet("/EditOrderServlet")
public class EditOrderServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    private EditOrderDAO dao = new EditOrderDAO();
    
    public void setEditOrderDAO(EditOrderDAO dao) {
    	this.dao = dao;
    }
    
    private EditOrderLogic logic = new EditOrderLogic(dao);

    public void setLogic(EditOrderLogic logic){
        this.logic = logic;
    }
    
    private EditOrderInfo editOrderInfo;

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String oid = request.getParameter("oid");
        String from = request.getParameter("from"); 
        if (oid == null || oid.isEmpty()) {
            if ("servedHistory".equals(from)) {
                response.sendRedirect("ServedHistoryServlet");
            } else {
                response.sendRedirect("OrderManagementServlet");
            }
            return;
        }

        int orderId = Integer.parseInt(oid);

        try {
            EditOrderInfo info = dao.findOrderDetails(orderId);
            if (info != null) {
                List<EditOrderInfo.ToppingList> toppings = dao.findToppingListByProductId(info.getProductId(), orderId);
                if (toppings != null) {
                    for (EditOrderInfo.ToppingList t : toppings) {
                        info.addTopping(t.getToppingId(), t.getName(), t.getQuantity(), t.getPrice());
                    }
                }
                request.setAttribute("editOrderInfo", info);
                request.setAttribute("from", from); 
                request.getRequestDispatcher("WEB-INF/jsp/editOrder.jsp").forward(request, response);
            } else {
                if ("servedHistory".equals(from)) {
                    response.sendRedirect("ServedHistoryServlet");
                } else {
                    response.sendRedirect("OrderManagementServlet");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String oid = request.getParameter("oid");
        String from = request.getParameter("from");

        if (oid == null || oid.isEmpty()) {
            redirect(response, from);
            return;
        }

        int orderId = Integer.parseInt(oid);

        String button = request.getParameter("Button");
        String mode = request.getParameter("mode");



        try {

            // 注文削除
            if ("delete_order".equals(button)) {
                logic.deleteOrder(orderId);
                redirect(response, from);
                return;
            }

            // 注文情報取得
            EditOrderInfo info = logic.loadOrder(orderId);

            if (info == null) {
                redirect(response, from);
                return;
            }

            // 画面の値をEditOrderInfoへ反映
            logic.updateInfoFromRequest(info, request);

            // ＋－ボタン
            if (button != null &&
                    ("main_plus".equals(button)
                    || "main_minus".equals(button)
                    || button.startsWith("+")
                    || button.startsWith("-"))) {

                logic.calcQuantity(info, button);

                request.setAttribute("editOrderInfo", info);
                request.setAttribute("from", from);
                request.getRequestDispatcher("WEB-INF/jsp/editOrder.jsp")
                        .forward(request, response);
                return;
            }

            // 更新
            if ("update".equals(mode)) {
                logic.updateOrder(info, orderId);
                redirect(response, from);
                return;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }

    }
    private void redirect(HttpServletResponse response, String from)
            throws IOException {

        if ("servedHistory".equals(from)) {
            response.sendRedirect("ServedHistoryServlet");
        } else {
            response.sendRedirect("OrderManagementServlet");
        }
    }

	
}