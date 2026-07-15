package servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import dao.OrderManagementDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.OrderManagementInfo;
import model.OrderManagementLogic;

@WebServlet("/OrderManagementServlet")
public class OrderManagementServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	// 引数なしコンストラクタで生成するように修正
	private OrderManagementDAO dao = new OrderManagementDAO();
	
	public void setOrderManagementDAO(OrderManagementDAO dao) {
		this.dao = dao;
	}
	
	private OrderManagementInfo orderManagementInfo;
	public OrderManagementInfo getOderManagement() { return orderManagementInfo; }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		OrderManagementLogic logic = new OrderManagementLogic();

		String categoryFilter = request.getParameter("categoryFilter");
		String tableFilter = request.getParameter("tableFilter");

		try {
			// 全件データを取得（throws SQLException に対するハンドリング）
			List<OrderManagementInfo> allList = dao.findorderDetails();

			// ロジッククラスの新しいメソッドを呼び出して一撃で集計する
			java.util.Map<String, Integer> counts = logic.calculateBadgeCounts(allList);

			// Mapから集計データを取得してJSPスコープにセット
			request.setAttribute("countAll", counts.get("countAll"));
			request.setAttribute("countOkonomi", counts.get("countOkonomi"));
			request.setAttribute("countMonja", counts.get("countMonja"));
			request.setAttribute("countTeppan", counts.get("countTeppan"));
			request.setAttribute("countSide", counts.get("countSide"));
			request.setAttribute("countSoft", counts.get("countSoft"));
			request.setAttribute("countSake", counts.get("countSake"));
			request.setAttribute("countBottle", counts.get("countBottle"));

			request.setAttribute("countTableAll", counts.get("countTableAll"));
			request.setAttribute("countTable1", counts.get("countTable1"));
			request.setAttribute("countTable2", counts.get("countTable2"));
			request.setAttribute("countTable3", counts.get("countTable3"));
			request.setAttribute("countTable4", counts.get("countTable4"));

			// 現在「何が選択されているか」の状態もJSPへ引き継ぐために保存
			if (categoryFilter == null) categoryFilter = "全て";
			if (tableFilter == null) tableFilter = "全ての卓";
			request.setAttribute("currentCategory", categoryFilter);
			request.setAttribute("currentTable", tableFilter);

			// ロジックに2つのフィルターを渡して掛け合わせ
			logic.calculateOrderTimes(allList);
			List<OrderManagementInfo> omList = logic.filterOrders(allList, categoryFilter, tableFilter);
			logic.calculateOrderTimes(omList);

			// 絞り込まれたリストをJSPへ渡す
			request.setAttribute("omList", omList);

		} catch (SQLException e) {
			// SQLエラーが発生した場合のハンドリング
			e.printStackTrace();
			throw new ServletException("データベース処理中にエラーが発生しました。", e);
		}

		request.getRequestDispatcher("/WEB-INF/jsp/orderManagement.jsp").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String oidstr = request.getParameter("oid");
		String action = request.getParameter("action");

		if (oidstr != null && action != null) {
			int orderId = Integer.parseInt(oidstr);
			try {
				if (action.equals("提供")) {
					dao.updateServedFlag(orderId);
				}
			} catch (SQLException e) {
				e.printStackTrace();
				throw new ServletException("ステータス更新中にエラーが発生しました。", e);
			}
		}

		response.sendRedirect("OrderManagementServlet");
	}
}