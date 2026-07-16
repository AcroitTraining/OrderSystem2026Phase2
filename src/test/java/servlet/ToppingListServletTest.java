package servlet;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import dao.ToppingListDAO;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.ToppingListInfo;

class ToppingListServletTest {

	@Mock private HttpServletRequest request;
	@Mock private HttpServletResponse response;
	@Mock private RequestDispatcher dispatcher;
	@Mock private Connection connection;
	@Mock private ToppingListDAO dao;

	private ToppingListServlet servlet;

	@BeforeEach
	void setUp() throws Exception {
		MockitoAnnotations.openMocks(this);
		servlet = spy(new ToppingListServlet());
		doReturn(connection).when(servlet).createConnection();
		doReturn(dao).when(servlet).createDAO(connection);
		when(request.getRequestDispatcher("/WEB-INF/jsp/toppingList.jsp")).thenReturn(dispatcher);
	}

	@Test
	void doGet_トッピング一覧を取得してフォワードする() throws Exception {
		List<ToppingListInfo> tList = Arrays.asList(new ToppingListInfo(), new ToppingListInfo());
		when(dao.findAllTopping()).thenReturn(tList);

		servlet.doGet(request, response);

		verify(request).setAttribute("tList", tList);
		verify(dispatcher).forward(request, response);
	}

	@Test
	void doPost_表示切り替えアクションでフラグ更新される() throws Exception {
		when(request.getParameter("toppingId")).thenReturn("5");
		when(request.getParameter("action")).thenReturn("表示切り替え");

		servlet.doPost(request, response);

		verify(dao).updateToppingDisplayFlag(5);
		verify(dao, never()).updateToppingDeleteFlag(anyInt());
		verify(response).sendRedirect("ToppingListServlet");
	}

	@Test
	void doPost_削除アクションでフラグ更新される() throws Exception {
		when(request.getParameter("toppingId")).thenReturn("7");
		when(request.getParameter("action")).thenReturn("削除");

		servlet.doPost(request, response);

		verify(dao).updateToppingDeleteFlag(7);
		verify(dao, never()).updateToppingDisplayFlag(anyInt());
		verify(response).sendRedirect("ToppingListServlet");
	}
}