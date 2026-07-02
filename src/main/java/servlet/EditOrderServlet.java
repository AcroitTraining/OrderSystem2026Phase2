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

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String oid = request.getParameter("oid");
        // ★ 遷移元パラメータ（"orderManagement" または "servedHistory"）を取得
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
        EditOrderDAO dao = new EditOrderDAO();

        try {
            EditOrderInfo info = dao.findOrderDetails(orderId);
            if (info != null) {
                List<EditOrderInfo.ToppingList> toppings = dao.findToppingListByProductId(info.getProductId(), orderId);
                if (toppings != null) {
                    for (EditOrderInfo.ToppingList t : toppings) {
                        info.addTopping(t.getToppingId(), t.getName(), t.getQuantity(), 0);
                    }
                }
                request.setAttribute("editOrderInfo", info);
                // ★ JSPへ渡す
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
        // ★ hiddenから遷移元を回収
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
        String button = request.getParameter("Button");
        String mode = request.getParameter("mode");

        EditOrderDAO dao = new EditOrderDAO();

        try {
            // 注文削除時のリダイレクト
            if ("delete_order".equals(button)) {
                dao.deleteOrderComplete(orderId);
                if ("servedHistory".equals(from)) {
                    response.sendRedirect("ServedHistoryServlet");
                } else {
                    response.sendRedirect("OrderManagementServlet");
                }
                return;
            }

            EditOrderInfo info = dao.findOrderDetails(orderId);
            if (info == null) {
                if ("servedHistory".equals(from)) {
                    response.sendRedirect("ServedHistoryServlet");
                } else {
                    response.sendRedirect("OrderManagementServlet");
                }
                return;
            }

            List<EditOrderInfo.ToppingList> toppings = dao.findToppingListByProductId(info.getProductId(), orderId);
            if (toppings != null) {
                for (EditOrderInfo.ToppingList t : toppings) {
                    info.addTopping(t.getToppingId(), t.getName(), t.getQuantity(), 0);
                }
            }

            String oldProductQty = request.getParameter("oldProductQty");
            if (oldProductQty != null && !oldProductQty.isEmpty()) {
                info.setProductQuantity(Integer.parseInt(oldProductQty));
            }

            for (int i = 0; i < info.getToppings().size(); i++) {
                String qty = request.getParameter("oldQty_" + i);
                if (qty != null && !qty.isEmpty()) {
                    info.getToppings().get(i).setQuantity(Integer.parseInt(qty));
                }
            }

            EditOrderLogic logic = new EditOrderLogic();

            // 「＋」「－」ボタン処理（計算画面の再表示）
            if (button != null && ("main_plus".equals(button) || "main_minus".equals(button) || button.startsWith("+") || button.startsWith("-"))) {
                logic.calcQuantity(info, button);
                request.setAttribute("editOrderInfo", info);
                request.setAttribute("from", from); // fromを引き継ぐ
                request.getRequestDispatcher("WEB-INF/jsp/editOrder.jsp").forward(request, response);
                return;
            }

            // 「変更」確定処理
            if ("update".equals(mode)) {
                EditOrderInfo dbOriginal = dao.findOrderDetails(orderId);
                List<EditOrderInfo.ToppingList> dbToppingOriginal = dao.findToppingListByProductId(info.getProductId(), orderId);

                int screenProductQty = info.getProductQuantity(); 
                int dbProductQty = dbOriginal.getProductQuantity();   

                if (screenProductQty != dbProductQty) {
                    dao.updateProductQuantity(orderId, screenProductQty);
                    int productDiff = screenProductQty - dbProductQty;
                    dao.updateProductStock(info.getProductId(), -productDiff);
                }

                for (int i = 0; i < info.getToppings().size(); i++) {
                    EditOrderInfo.ToppingList screen = info.getToppings().get(i);
                    EditOrderInfo.ToppingList db = dbToppingOriginal.get(i); 
                    
                    int screenQty = screen.getQuantity();
                    int dbQty = db.getQuantity();

                    if (dbQty != screenQty) {
                        if (dbQty == 0 && screenQty > 0) {
                            dao.insertTopping(orderId, screen.getToppingId(), screenQty);
                        } else if (dbQty > 0 && screenQty == 0) {
                            dao.deleteToppingSingle(orderId, screen.getToppingId());
                        } else if (dbQty > 0 && screenQty > 0) {
                            dao.updateToppingQuantity(orderId, screen.getToppingId(), screenQty);
                        }

                        int toppingDiff = screenQty - dbQty;
                        dao.updateToppingStock(screen.getToppingId(), -toppingDiff);
                    }
                }

                // ★ 正しい名前に合わせた動的リダイレクト切り替え
                if ("servedHistory".equals(from)) {
                    response.sendRedirect("ServedHistoryServlet");
                } else {
                    response.sendRedirect("OrderManagementServlet");
                }
                return;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}