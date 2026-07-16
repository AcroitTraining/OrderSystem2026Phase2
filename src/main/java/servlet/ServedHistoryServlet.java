package servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

import dao.ServedHistoryDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.ServedHistoryInfo;
import model.ServedHistoryLogic;

@WebServlet("/ServedHistoryServlet")
public class ServedHistoryServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	//DB接続情報
	private final String URL = "jdbc:mysql://localhost:3306/order_management";
	private final String USER = "order";
	private final String PASS = "1234";

	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		
		String tableFilter = request.getParameter("tableFilter");
		
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		try (Connection conn = DriverManager.getConnection(URL, USER, PASS)) {

			ServedHistoryDAO dao = new ServedHistoryDAO(conn);
			List<ServedHistoryInfo> shList = dao.findorderDetails();
			
			if (tableFilter == null) tableFilter = "全ての卓";
			request.setAttribute("currentTable", tableFilter);

			ServedHistoryLogic logic = new ServedHistoryLogic();
			shList = logic.filterByTable(shList, tableFilter);
			
			request.setAttribute("shList", shList);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}

		request.getRequestDispatcher("/WEB-INF/jsp/servedHistory.jsp").forward(request, response);
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		String oidstr = request.getParameter("oid");
		String action = request.getParameter("action");
		
		int orderId = Integer.parseInt(oidstr);
		try (Connection conn = DriverManager.getConnection(URL, USER, PASS)) {
			ServedHistoryDAO dao = new ServedHistoryDAO(conn);

			dao.updateServedFlag(orderId);
			if(action.equals("戻す")) {
				
			}else {
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		response.sendRedirect("ServedHistoryServlet");
		
		
	}
}
