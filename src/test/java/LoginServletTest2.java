import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.LoginInfo;
import servlet.LoginServlet;

@ExtendWith(MockitoExtension.class)
class LoginServletTest2 {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private HttpSession session;

    @InjectMocks
    private LoginServlet servlet;

    @Test
    void testLoginServlet() throws ServletException, IOException {

        // テストデータ
        String userId = "order";
        String password = "1234";

        when(request.getParameter("userId")).thenReturn(userId);
        when(request.getParameter("password")).thenReturn(password);
        when(request.getSession()).thenReturn(session);

        // Servlet実行
        servlet.doPost(request, response);

        // LoginInfoの確認
        LoginInfo info = servlet.getLoginInfo();

        assertEquals(userId, info.getLoginId());
        assertEquals(password, info.getLoginPassword());

        // パラメータ取得確認
        verify(request).getParameter("userId");
        verify(request).getParameter("password");
    }
}