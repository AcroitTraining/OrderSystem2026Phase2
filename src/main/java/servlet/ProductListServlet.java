package servlet;

import java.io.IOException;
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
	
	private ProductListDAO dao = new ProductListDAO();
	
	public void setProductListDAO(ProductListDAO dao) {
		this.dao = dao;
	}
	
	private ProductListLogic logic = new ProductListLogic(dao);
	
	public void setLogic(ProductListLogic logic) {
		this.logic = logic;
	}
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String filter = request.getParameter("filter");
		if (filter == null || filter.isEmpty()) {
	        filter = "全て";
	    }
			
			List<ProductListInfo> pList;
			try {
				pList = dao.findAllProduct();
				pList = logic.filterProducts(pList, filter);
				
				request.setAttribute("pList", pList);
				request.setAttribute("currentCategory", filter);
			} catch (SQLException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			}
		
		request.getRequestDispatcher("/WEB-INF/jsp/productList.jsp").forward(request, response);
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {	
		
		String pid = request.getParameter("productId");
		
		if (pid == null || pid.isEmpty()) {
		    response.sendRedirect("ProductListServlet");
		    return;
		}
		
		int productId = Integer.parseInt(pid);
		
		String action = request.getParameter("action");

		
		try {
			if("表示切り替え".equals(action)) {
				dao.updateProductDisplayFlag(productId);
			}else if("削除".equals(action)) {
				dao.updateProductDeleteFlag(productId);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		response.sendRedirect("ProductListServlet");
	}

}
