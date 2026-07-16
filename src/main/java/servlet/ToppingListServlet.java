package servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

import dao.ToppingListDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.ToppingListInfo;

@WebServlet("/ToppingListServlet")
public class ToppingListServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	//DB接続情報
		private final String URL = "jdbc:mysql://localhost:3306/order_management";
		private final String USER = "order";
		private final String PASS = "1234";
	    
		protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
			try {
				Class.forName("com.mysql.cj.jdbc.Driver");
			} catch (ClassNotFoundException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			}
			request.setCharacterEncoding("UTF-8");
			try (Connection conn = DriverManager.getConnection(URL, USER, PASS)) {
				ToppingListDAO dao = new ToppingListDAO(conn);	
				List<ToppingListInfo> tList = dao.findAllTopping();
				request.setAttribute("tList", tList);
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			request.getRequestDispatcher("/WEB-INF/jsp/toppingList.jsp").forward(request, response);
		}
		
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		
		request.setCharacterEncoding("UTF-8");
		String tid = request.getParameter("toppingId");
		int toppingId = Integer.parseInt(tid);
		String action = request.getParameter("action");
		
		try (Connection conn = DriverManager.getConnection(URL, USER, PASS)) {
			ToppingListDAO dao = new ToppingListDAO(conn);


			if("表示切り替え".equals(action)) { 
				dao.updateToppingDisplayFlag(toppingId);
			} else if("削除".equals(action)) {
				dao.updateToppingDeleteFlag(toppingId);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			// 必要に応じてエラー画面への遷移処理など
		}
		response.sendRedirect("ToppingListServlet");
	}

}
