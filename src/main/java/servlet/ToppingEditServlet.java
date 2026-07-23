package servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import dao.DBConnectionUtil;
import dao.ToppingEditDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.ToppingEditInfo;

@WebServlet("/ToppingEditServlet")
public class ToppingEditServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private ToppingEditDAO dao = new ToppingEditDAO();


	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response)
			throws ServletException, IOException {

		String tid = request.getParameter("toppingId");

		try (Connection conn = DBConnectionUtil.getConnection()) {

			// 新規登録モード
			if (tid == null || tid.isEmpty()) {

				ToppingEditInfo emptyInfo =
						new ToppingEditInfo();

				emptyInfo.setToppingId(0);
				emptyInfo.setToppingName("");
				emptyInfo.setToppingPrice(0);
				emptyInfo.setToppingStock(0);

				request.setAttribute(
						"toppingEditInfo",
						emptyInfo);

				request.getRequestDispatcher(
						"WEB-INF/jsp/toppingEdit.jsp")
						.forward(request, response);

				return;
			}


			// 編集モード
			int toppingId =
					Integer.parseInt(tid);

			ToppingEditInfo info =
					dao.findToppingDetails(
							conn,
							toppingId);


			if (info != null) {

				request.setAttribute(
						"toppingEditInfo",
						info);

				request.getRequestDispatcher(
						"WEB-INF/jsp/toppingEdit.jsp")
						.forward(request, response);

			} else {

				response.sendRedirect(
						"ToppingListServlet");
			}


		} catch (SQLException e) {

			e.printStackTrace();

			response.sendError(
					HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
	}


	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response)
			throws ServletException, IOException {

		request.setCharacterEncoding("UTF-8");


		String toppingIdStr =
				request.getParameter("toppingId");

		String toppingName =
				request.getParameter("toppingName");

		String toppingPriceStr =
				request.getParameter("toppingPrice");

		String toppingStockStr =
				request.getParameter("toppingStock");


		int toppingPrice =
				(toppingPriceStr != null
				&& !toppingPriceStr.isEmpty())
				? Integer.parseInt(toppingPriceStr)
				: 0;


		int toppingStock =
				(toppingStockStr != null
				&& !toppingStockStr.isEmpty())
				? Integer.parseInt(toppingStockStr)
				: 0;


		try (Connection conn =
				DBConnectionUtil.getConnection()) {


			if (toppingIdStr == null
					|| toppingIdStr.isEmpty()
					|| "0".equals(toppingIdStr)) {


				// 新規追加
				dao.insertToppingDetails(
						conn,
						toppingName,
						toppingPrice,
						toppingStock);


			} else {


				// 更新
				int toppingId =
						Integer.parseInt(toppingIdStr);

				dao.updateToppingDetails(
						conn,
						toppingId,
						toppingName,
						toppingPrice,
						toppingStock);
			}


			response.sendRedirect(
					"ToppingListServlet");


		} catch (SQLException e) {

			e.printStackTrace();

			response.sendError(
					HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
	}
}