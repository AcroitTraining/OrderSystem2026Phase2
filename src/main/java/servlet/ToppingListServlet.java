package servlet;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import dao.DBConnectionUtil;
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

	protected Connection createConnection() throws SQLException {
		return DBConnectionUtil.getConnection();
	}

	protected ToppingListDAO createDAO(Connection conn) {
		return new ToppingListDAO(conn);
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		try (Connection conn = createConnection()) {
			ToppingListDAO dao = createDAO(conn);
			List<ToppingListInfo> tList = dao.findAllTopping();
			request.setAttribute("tList", tList);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		request.getRequestDispatcher("/WEB-INF/jsp/toppingList.jsp").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		String tid = request.getParameter("toppingId");
		int toppingId = Integer.parseInt(tid);
		String action = request.getParameter("action");

		try (Connection conn = createConnection()) {
			ToppingListDAO dao = createDAO(conn);
			if ("表示切り替え".equals(action)) {
				dao.updateToppingDisplayFlag(toppingId);
			} else if ("削除".equals(action)) {
				dao.updateToppingDeleteFlag(toppingId);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		response.sendRedirect("ToppingListServlet");
	}
}