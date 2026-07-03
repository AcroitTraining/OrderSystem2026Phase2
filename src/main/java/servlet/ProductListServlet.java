package servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

import dao.ProductListDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.ProductListInfo;
import model.ProductListLogic;

@WebServlet("/ProductListServlet")
public class ProductListServlet extends HttpServlet {
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
		
		ProductListLogic logic = new ProductListLogic();
		String filter = request.getParameter("filter");
		
		try (Connection conn = DriverManager.getConnection(URL, USER, PASS)) {
			ProductListDAO dao = new ProductListDAO(conn);
			
			List<ProductListInfo> pList = dao.findAllProduct();
			pList = logic.fillterProducts(pList, filter);
			
			request.setAttribute("pList", pList);
			request.setAttribute("currentCategory", filter);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		request.getRequestDispatcher("/WEB-INF/jsp/productList.jsp").forward(request, response);
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		
		String pid = request.getParameter("productId");
		int productId = Integer.parseInt(pid);
		String action = request.getParameter("action");
		System.out.println(action + productId);
		
		try (Connection conn = DriverManager.getConnection(URL, USER, PASS)) {
			ProductListDAO dao = new ProductListDAO(conn);


			if(action.equals("表示切り替え")) {
				dao.updateProductDisplayFlag(productId);
			}else if(action.equals("削除")) {
				dao.updateProductDeleteFlag(productId);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			// 必要に応じてエラー画面への遷移処理など
		}
		response.sendRedirect("ProductListServlet");
	}

}
