package servlet;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import dao.LoginDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.LoginInfo;

@ExtendWith(MockitoExtension.class)
class LoginServletTest2 {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private HttpSession session;

    @Mock
    private LoginDAO dao;

    @InjectMocks
    private LoginServlet servlet;

    @BeforeEach
    void setUp() {
        servlet.setLoginDAO(dao);
    }

    @Test
    void testLoginServlet() throws ServletException, IOException {

        // テストデータ
        String userId = "order";
        String password = "1234";

        LoginInfo testInfo = new LoginInfo(userId, password);

        // モックの設定
        when(request.getParameter("userId")).thenReturn(userId);
        when(request.getParameter("password")).thenReturn(password);
        when(request.getSession()).thenReturn(session);

        // DAOの戻り値を設定
        when(dao.loginCheck(userId, password)).thenReturn(testInfo);

        // Servlet実行
        servlet.doPost(request, response);

        // Servlet内に保持されたLoginInfoを確認
        LoginInfo info = servlet.getLoginInfo();

        assertNotNull(info);
        assertEquals(userId, info.getLoginId());
        assertEquals(password, info.getLoginPassword());

        // 動作確認
        verify(request).getParameter("userId");
        verify(request).getParameter("password");
        verify(request).getSession();

        verify(dao).loginCheck(userId, password);

        verify(session).setAttribute("loginUser", testInfo);

        verify(response).sendRedirect("HomeServlet");
        System.out.println(info.getLoginId());
        System.out.println(info.getLoginPassword());
    }
}