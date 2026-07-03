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
			e.printStackTrace();
		}
		try (Connection conn = DriverManager.getConnection(URL, USER, PASS)) {

			OrderManagementDAO dao = new OrderManagementDAO(conn);


			// 1. 全件データを取得
		    List<OrderManagementInfo> allList = dao.findorderDetails();
		    
		    // 2. 全件データからカテゴリごとの件数を集計する
		    int countAll = allList.size();
		    int countOkonomi = 0;
		    int countMonja = 0;
		    int countTeppan = 0;
		    int countSide = 0;
		    int countSoft = 0;
		    int countSake = 0;
		    int countBottle = 0;

		    for (OrderManagementInfo item : allList) {
		        String cat = item.getCategoryName();
		        if ("お好み焼き".equals(cat)) countOkonomi++;
		        else if ("もんじゃ焼き".equals(cat)) countMonja++;
		        else if ("鉄板焼き".equals(cat)) countTeppan++;
		        else if ("サイドメニュー".equals(cat)) countSide++;
		        else if ("ソフトドリンク".equals(cat)) countSoft++;
		        else if ("お酒".equals(cat)) countSake++;
		        else if ("ボトル".equals(cat)) countBottle++;
		    }

		    // 3. バッジの件数をリクエストスコープに保存
		    request.setAttribute("countAll", countAll);
		    request.setAttribute("countOkonomi", countOkonomi);
		    request.setAttribute("countMonja", countMonja);
		    request.setAttribute("countTeppan", countTeppan);
		    request.setAttribute("countSide", countSide);
		    request.setAttribute("countSoft", countSoft);
		    request.setAttribute("countSake", countSake);
		    request.setAttribute("countBottle", countBottle);

		    // 4. 既存の絞り込み処理を行う
		    logic.calculateOrderTimes(allList);
		    List<OrderManagementInfo> omList = logic.filterOrders(allList, tableFilter);
		    logic.calculateOrderTimes(omList);

		    // 5. 絞り込まれたリストをJSPへ渡す
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
