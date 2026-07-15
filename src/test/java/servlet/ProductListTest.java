package servlet;

import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import dao.ProductListDAO;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.ProductListInfo;
import model.ProductListLogic;

@ExtendWith(MockitoExtension.class)
class ProductListTest {

	@Mock
	private HttpServletRequest request;
	@Mock
	private HttpServletResponse response;
	@Mock
	private HttpSession session;
	@Mock
	private ProductListDAO dao;
	@Mock
	private ProductListLogic logic;
	@Mock
	private RequestDispatcher dispatcher;
	@InjectMocks
	private ProductListServlet servlet;
	@BeforeEach
	void setUp() {
		servlet.setProductListDAO(dao);
		servlet.setLogic(logic);
	}

	@Test
	void testDoGet() throws Exception {
		String filter = "お好み焼き";

		List<ProductListInfo> list = new ArrayList<>();

		when(request.getParameter("filter"))
		.thenReturn(filter);

		when(dao.findAllProduct())
		.thenReturn(list);

		when(logic.filterProducts(list, filter))
		.thenReturn(list);

		when(request.getRequestDispatcher("/WEB-INF/jsp/productList.jsp"))
		.thenReturn(dispatcher);

		servlet.doGet(request, response);

		//DAO呼んだ？
		verify(dao).findAllProduct();

		//Logic呼んだ？
		verify(logic).filterProducts(list, filter);

		//requestへセットした？
		verify(request).setAttribute("pList", list);

		verify(request).setAttribute("currentCategory", filter);

		//forwardした？
		verify(dispatcher).forward(request, response);	
	}

	@Test
	void testDoPost_DisplayFlagChange() throws Exception {

		String productId = "1";
		String action = "表示切り替え";

		when(request.getParameter("productId")).thenReturn(productId);
		when(request.getParameter("action")).thenReturn(action);

		doNothing().when(dao).updateProductDisplayFlag(1);

		servlet.doPost(request, response);

		verify(request).getParameter("productId");
		verify(request).getParameter("action");

		verify(dao).updateProductDisplayFlag(1);
		verify(response).sendRedirect("ProductListServlet");
	}

	@Test
	void testDoPost_DeleteFlagChange() throws Exception {

		String productId = "1";
		String action = "削除";

		when(request.getParameter("productId")).thenReturn(productId);
		when(request.getParameter("action")).thenReturn(action);

		doNothing().when(dao).updateProductDisplayFlag(1);

		servlet.doPost(request, response);

		verify(request).getParameter("productId");
		verify(request).getParameter("action");

		verify(dao).updateProductDeleteFlag(1);
		verify(response).sendRedirect("ProductListServlet");
	}
	
	@Test
	void testDoPost_ProductIdIsNull() throws Exception{
				
		when(request.getParameter("productId")).thenReturn(null);
		
		servlet.doPost(request, response);
		
		verify(response).sendRedirect("ProductListServlet");
		
		verifyNoInteractions(dao);
	}

}
