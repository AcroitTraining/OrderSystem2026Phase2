import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.sql.SQLException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.LoginInfo;
import servlet.LoginServlet;


@ExtendWith(MockitoExtension.class)
class LoginServletTest2 {
	// @Mock private OrderStartDAO dao;
	@Mock private HttpServletRequest request;
	@Mock private HttpServletResponse response;
	@Mock private RequestDispatcher dispatcher;
	@InjectMocks private LoginServlet servlet;

	@Test
	void testLoginServlet() throws ServletException, IOException, SQLException, ClassNotFoundException {

		String userId = "order";
		String password = "1234";
		LoginInfo InfoTest = new LoginInfo(userId, password);

		when(request.getParameter("userId")).thenReturn("order");
		when(request.getParameter("password")).thenReturn("1234");
		doNothing().when(dispatcher).forward(request, response);
		when(request.getRequestDispatcher("HomeServlet")).thenReturn(dispatcher);

		// servletの実行
		servlet.doPost(request, response);

		LoginInfo Info = servlet.getLoginInfo();

		assertEquals(InfoTest.getLoginId(), Info.getLoginId());
		assertEquals(InfoTest.getLoginPassword(), Info.getLoginPassword());

		verify(request, times(1)).setAttribute("loginInfo", Info);
		verify(request, times(1)).getRequestDispatcher(anyString());

	}



}
