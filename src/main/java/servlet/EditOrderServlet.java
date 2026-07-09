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
            // ==========================================
            // 【注文取り消し】時のストック一括返却＆削除ロジック
            // ==========================================
            if ("delete_order".equals(button)) {
                EditOrderInfo dbOriginal = dao.findOrderDetails(orderId);
                if (dbOriginal != null) {
                    int dbProductQty = dbOriginal.getProductQuantity(); // 注文されていた商品数
                    
                    // 1. メイン商品の在庫を戻す（消去されるので、注文個数分をそのままプラスする）
                    dao.updateProductStock(dbOriginal.getProductId(), dbProductQty);

                    // 2. 紐づいていたトッピングの在庫を「掛け算」で計算してすべて戻す
                    List<EditOrderInfo.ToppingList> dbToppingOriginal = dao.findToppingListByProductId(dbOriginal.getProductId(), orderId);
                    if (dbToppingOriginal != null) {
                        for (EditOrderInfo.ToppingList topping : dbToppingOriginal) {
                            int dbToppingQty = topping.getQuantity(); // 1商品あたりのトッピング選択数
                            
                            if (dbToppingQty > 0) {
                                // 総消費量 ＝ トッピング選択数 × 商品注文数
                                int totalToppingCount = dbToppingQty * dbProductQty;
                                // 在庫に戻す（加算する）ため、そのままプラスで渡す
                                dao.updateToppingStock(topping.getToppingId(), totalToppingCount);
                            }
                        }
                    }
                }

                // 3. 在庫をすべて戻し終えた後、安全にデータを物理削除
                dao.deleteOrderComplete(orderId);

                // 画面リダイレクト
                if ("servedHistory".equals(from)) {
                    response.sendRedirect("ServedHistoryServlet");
                } else {
                    response.sendRedirect("OrderManagementServlet");
                }
                return;
            }

            // --- 通常の読み込み＆「変更」確定処理 ---
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
                    info.addTopping(t.getToppingId(), t.getName(), t.getQuantity(), t.getPrice());
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

            // 「＋」「－」ボタンの一時計算処理
            if (button != null && ("main_plus".equals(button) || "main_minus".equals(button) || button.startsWith("+") || button.startsWith("-"))) {
                logic.calcQuantity(info, button);
                request.setAttribute("editOrderInfo", info);
                request.setAttribute("from", from);
                request.getRequestDispatcher("WEB-INF/jsp/editOrder.jsp").forward(request, response);
                return;
            }

            // 「変更」ボタン確定時の裏在庫掛け算連動
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
                    }

                    int newTotalToppingCount = screenQty * screenProductQty;
                    int oldTotalToppingCount = dbQty * dbProductQty;
                    int toppingStockDiff = newTotalToppingCount - oldTotalToppingCount;

                    if (toppingStockDiff != 0) {
                        dao.updateToppingStock(screen.getToppingId(), -toppingStockDiff);
                    }
                }

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