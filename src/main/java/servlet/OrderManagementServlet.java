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
		String tableFilter = request.getParameter("tableFilter");
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		try (Connection conn = DriverManager.getConnection(URL, USER, PASS)) {

			OrderManagementDAO dao = new OrderManagementDAO(conn);


			List<OrderManagementInfo> omList = dao.findorderDetails();
			logic.calculateOrderTimes(omList);

			// 受け取った値を使って、カテゴリーまたは卓番で絞り込む
			omList = logic.filterOrders(omList, tableFilter);

			//  絞り込まれたデータに対して時間の計算をして色を決定する
			logic.calculateOrderTimes(omList);
			// 4. リクエストスコープに保存してJSPへ渡す
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
