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
			
			//2. カテゴリごとの件数を集計（既存の処理）
			int countAll = allList.size();
			int countOkonomi = 0;
			int countMonja = 0;
			int countTeppan = 0;
			int countSide = 0;
			int countSoft = 0;
			int countSake = 0;
			int countBottle = 0;

			//卓番ごとの件数を集計
			int countTableAll = allList.size();
			int countTable1 = 0;
			int countTable2 = 0;
			int countTable3 = 0;
			int countTable4 = 0;

			for (OrderManagementInfo item : allList) {
				// カテゴリのカウント
				String cat = item.getCategoryName();
				if ("お好み焼き".equals(cat)) countOkonomi++;
				else if ("もんじゃ焼き".equals(cat)) countMonja++;
				else if ("鉄板焼き".equals(cat)) countTeppan++;
				else if ("サイドメニュー".equals(cat)) countSide++;
				else if ("ソフトドリンク".equals(cat)) countSoft++;
				else if ("お酒".equals(cat)) countSake++;
				else if ("ボトル".equals(cat)) countBottle++;
				
				// 🆕 卓番のカウント（1〜4卓）
				int tid = item.getTableId();
				if (tid == 1) countTable1++;
				else if (tid == 2) countTable2++;
				else if (tid == 3) countTable3++;
				else if (tid == 4) countTable4++;
			}

			// カテゴリバッジの件数を保存
			request.setAttribute("countAll", countAll);
			request.setAttribute("countOkonomi", countOkonomi);
			request.setAttribute("countMonja", countMonja);
			request.setAttribute("countTeppan", countTeppan);
			request.setAttribute("countSide", countSide);
			request.setAttribute("countSoft", countSoft);
			request.setAttribute("countSake", countSake);
			request.setAttribute("countBottle", countBottle);

			// 🆕 卓番バッジの件数をリクエストスコープに保存
			request.setAttribute("countTableAll", countTableAll);
			request.setAttribute("countTable1", countTable1);
			request.setAttribute("countTable2", countTable2);
			request.setAttribute("countTable3", countTable3);
			request.setAttribute("countTable4", countTable4);

			// 🆕 3. 現在「何が選択されているか」の状態もJSPへ引き継ぐために保存
			// 最初（nullのとき）は一律「全て」「全ての卓」にしておく
			if (categoryFilter == null) categoryFilter = "全て";
			if (tableFilter == null) tableFilter = "全ての卓";
			request.setAttribute("currentCategory", categoryFilter);
			request.setAttribute("currentTable", tableFilter);

			// 🟢 4. 修正したロジックに2つのフィルターを渡して掛け合わせ
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
