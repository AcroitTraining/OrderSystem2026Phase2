package servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
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
	//DB接続情報
	private final String URL = "jdbc:mysql://localhost:3306/order_management";
	private final String USER = "order";
	private final String PASS = "1234";

	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		OrderManagementLogic logic = new OrderManagementLogic();

		//1. 2つのパラメータを別々に受け取る（名前を明快に分けました）
		String categoryFilter = request.getParameter("categoryFilter");
		String tableFilter = request.getParameter("tableFilter");

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		try (Connection conn = DriverManager.getConnection(URL, USER, PASS)) {

			OrderManagementDAO dao = new OrderManagementDAO(conn);

			// 全件データを取得
			List<OrderManagementInfo> allList = dao.findorderDetails();

			// 🟢 修正：ロジッククラスの新しいメソッドを呼び出して一撃で集計する
			java.util.Map<String, Integer> counts = logic.calculateBadgeCounts(allList);

			// 🟢 修正：Mapから集計データを取得してJSPスコープにセット（名前は元のままなのでJSPの変更不要）
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

			// 🆕 3. 現在「何が選択されているか」の状態もJSPへ引き継ぐために保存（ここから下はそのまま）
			if (categoryFilter == null) categoryFilter = "全て";
			if (tableFilter == null) tableFilter = "全ての卓";
			request.setAttribute("currentCategory", categoryFilter);
			request.setAttribute("currentTable", tableFilter);

			// 4. ロジックに2つのフィルターを渡して掛け合わせ
			logic.calculateOrderTimes(allList);
			List<OrderManagementInfo> omList = logic.filterOrders(allList, categoryFilter, tableFilter);
			logic.calculateOrderTimes(omList);

			// 絞り込まれたリストをJSPへ渡す
			request.setAttribute("omList", omList);

		} catch (SQLException e) {
			e.printStackTrace();
		}

		request.getRequestDispatcher("/WEB-INF/jsp/orderManagement.jsp").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		String oidstr = request.getParameter("oid");
		String action = request.getParameter("action");

		int orderId = Integer.parseInt(oidstr);

		try (Connection conn = DriverManager.getConnection(URL, USER, PASS)) {
			OrderManagementDAO dao = new OrderManagementDAO(conn);


			if(action.equals("提供")) {
				dao.updateServedFlag(orderId);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			// 必要に応じてエラー画面への遷移処理など
		}
		response.sendRedirect("OrderManagementServlet");
	}
}
