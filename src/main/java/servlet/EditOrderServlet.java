package servlet;

import java.io.IOException;
import java.util.List;

import dao.EditOrderDAO;
import dao.EditOrderLogic;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.EditOrderInfo;

@WebServlet("/EditOrderServlet")
public class EditOrderServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String orderIdStr = request.getParameter("orderId");
        if (orderIdStr == null || orderIdStr.isEmpty()) {
            response.sendRedirect("OrderManagementServlet");
            return;
        }
        
        int orderId = Integer.parseInt(orderIdStr);
        EditOrderDAO dao = new EditOrderDAO();
        EditOrderInfo orderInfo = dao.findOrderDetails(orderId);
        
        if (orderInfo == null) {
            response.sendRedirect("OrderManagementServlet");
            return;
        }
        
        request.setAttribute("orderInfo", orderInfo);
        request.getRequestDispatcher("/WEB-INF/jsp/editOrder.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        
        int orderId = Integer.parseInt(request.getParameter("orderId"));
        String button = request.getParameter("Button");
        String mode = request.getParameter("mode");
        
        EditOrderDAO dao = new EditOrderDAO();
        EditOrderInfo orderInfo = dao.findOrderDetails(orderId);
        if (orderInfo == null) {
            response.sendRedirect("OrderManagementServlet");
            return;
        }
        
        EditOrderLogic logic = new EditOrderLogic();
        List<EditOrderInfo.ToppingInfo> toppingList = orderInfo.getToppingList();

        // 1. 画面上の商品数量とトッピング数量の復元
        int screenProductQty = Integer.parseInt(request.getParameter("oldProductQty"));
        for (int i = 0; i < toppingList.size(); i++) {
            String qty = request.getParameter("oldToppingQty_" + i);
            if (qty != null && !qty.isEmpty()) {
                toppingList.get(i).setToppingQuantity(Integer.parseInt(qty));
            }
        }

        // 2. プラス・マイナスボタン処理
        if (button != null) {
            if ("product_plus".equals(button)) {
                screenProductQty = logic.calcProductQuantity(screenProductQty, "plus");
            } else if ("product_minus".equals(button)) {
                screenProductQty = logic.calcProductQuantity(screenProductQty, "minus");
            } else if (button.startsWith("+")) {
                int index = Integer.parseInt(button.substring(1));
                logic.calcToppingQuantity(toppingList, index, "plus");
            } else if (button.startsWith("-")) {
                int index = Integer.parseInt(button.substring(1));
                logic.calcToppingQuantity(toppingList, index, "minus");
            }
            
            // 状態を維持して画面再描画
            orderInfo.setOrderQuantity(screenProductQty);
            request.setAttribute("orderInfo", orderInfo);
            request.getRequestDispatcher("/WEB-INF/jsp/editOrder.jsp").forward(request, response);
            return;
        }

        // 3. 注文取り消し、または商品数量0での確定時の削除処理
        if ("delete".equals(mode) || screenProductQty == 0) {
            dao.deleteOrder(orderId);
            response.sendRedirect("OrderManagementServlet");
            return;
        }

        // 4. 「変更確定」処理
        if ("update".equals(mode)) {
            // 商品数量更新
            dao.updateProductQuantity(orderId, screenProductQty);
            
            // トッピングの変更・削除・追加を一括判定
            EditOrderInfo dbInfo = dao.findOrderDetails(orderId);
            List<EditOrderInfo.ToppingInfo> dbList = dbInfo.getToppingList();
            
            for (int i = 0; i < toppingList.size(); i++) {
                EditOrderInfo.ToppingInfo screenTopping = toppingList.get(i);
                EditOrderInfo.ToppingInfo dbTopping = dbList.get(i);
                
                int sQty = screenTopping.getToppingQuantity();
                int dQty = dbTopping.getToppingQuantity();
                
                if (dQty == 0 && sQty > 0) {
                    dao.insertTopping(orderId, screenTopping.getToppingId(), sQty);
                } else if (dQty > 0 && sQty == 0) {
                    dao.deleteTopping(orderId, screenTopping.getToppingId());
                } else if (dQty > 0 && sQty > 0 && dQty != sQty) {
                    dao.updateToppingQuantity(orderId, screenTopping.getToppingId(), sQty);
                }
            }
            response.sendRedirect("OrderManagementServlet");
            return;
        }
        response.sendRedirect("OrderManagementServlet");
    }
}