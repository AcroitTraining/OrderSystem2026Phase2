package servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

import dao.OrderManagementDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.OrderManagementInfo;
import model.OrderManagementLogic;

@WebServlet("/OrderManagementAjaxServlet")
public class OrderManagementAjaxServlet extends HttpServlet {

    private OrderManagementDAO dao = new OrderManagementDAO();

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        try {
            List<OrderManagementInfo> allList = dao.findorderDetails();

            OrderManagementLogic logic = new OrderManagementLogic();

            // 件数計算
            Map<String, Integer> counts = logic.calculateBadgeCounts(allList);

            // フィルター取得
            String categoryFilter = request.getParameter("categoryFilter");
            String tableFilter = request.getParameter("tableFilter");

            if (categoryFilter == null) categoryFilter = "全て";
            if (tableFilter == null) tableFilter = "全ての卓";

            // 絞り込み
            List<OrderManagementInfo> omList =
                    logic.filterOrders(allList, categoryFilter, tableFilter);

            logic.calculateOrderTimes(omList);

            // ←ここでJSON用のデータを作る
            Map<String, Object> result = new HashMap<>();

            result.put("orders", omList);
            result.put("counts", counts);
            result.put("currentCategory", categoryFilter);
            result.put("currentTable", tableFilter);

            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(response.getWriter(), result);

        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }
}
