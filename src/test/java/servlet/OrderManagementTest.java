package servlet;

import static org.mockito.Mockito.*;

import java.io.IOException;
import java.lang.reflect.Method; // 👈 リフレクション用のインポート
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import dao.OrderManagementDAO;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.OrderManagementInfo;

@ExtendWith(MockitoExtension.class)
class OrderManagementTest {

    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private RequestDispatcher requestDispatcher;
    @Mock
    private OrderManagementDAO dao;

    @InjectMocks
    private OrderManagementServlet servlet;

    @BeforeEach
    void setUp() {
        servlet.setOrderManagementDAO(dao);
    }

    @Test
    @DisplayName("doGetのテスト（リフレクション版）")
    void testDoGet() throws Exception {
        when(request.getParameter("categoryFilter")).thenReturn("全て");
        when(request.getParameter("tableFilter")).thenReturn("全ての卓");
        List<OrderManagementInfo> dummyList = new ArrayList<>();
        when(dao.findorderDetails()).thenReturn(dummyList);
        when(request.getRequestDispatcher("/WEB-INF/jsp/orderManagement.jsp")).thenReturn(requestDispatcher);

        // 2. 実行  リフレクションで protected メソッドを呼び出す
        Method doGetMethod = OrderManagementServlet.class.getDeclaredMethod(
            "doGet", HttpServletRequest.class, HttpServletResponse.class
        );
        doGetMethod.setAccessible(true); // 
        doGetMethod.invoke(servlet, request, response); // 実行

        // 3. 検証 (Assert)
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    @DisplayName("doPostのテスト（リフレクション版）")
    void testDoPost() throws Exception {
        // 1. 準備 (Arrange)
        when(request.getParameter("oid")).thenReturn("10");
        when(request.getParameter("action")).thenReturn("提供");

        // 2. 実行 リフレクションで protected メソッドを呼び出す
        Method doPostMethod = OrderManagementServlet.class.getDeclaredMethod(
            "doPost", HttpServletRequest.class, HttpServletResponse.class
        );
        doPostMethod.setAccessible(true); 
        doPostMethod.invoke(servlet, request, response); // 実行

        // 3. 検証 (Assert)
        verify(dao, times(1)).updateServedFlag(10);
        verify(response).sendRedirect("OrderManagementServlet");
    }
    
    @Test
    @DisplayName("doGet: 履歴ボタンが押された時、単に履歴画面JSPへ遷移すること")
    void testDoGet_TransitionToHistory() throws ServletException, IOException, SQLException {
        // 1. 準備 (Arrange)
        // 履歴ボタンが押された（action=history）というシミュレーション
        when(request.getParameter("action")).thenReturn("history");
        
        // 遷移先となる履歴JSPのモックを設定
        when(request.getRequestDispatcher("/WEB-INF/jsp/orderHistory.jsp")).thenReturn(requestDispatcher);

        // 2. 実行 (Act)
        servlet.doGet(request, response);

        // 3. 検証 (Assert)
        // 履歴JSP宛てに正しくフォワードが実行されたかを確認
        verify(requestDispatcher).forward(request, response);
        
        // 単に遷移するだけなので、通常の一覧表示用DAO（findorderDetails）は呼ばれていないことを検証
        verify(dao, never()).findorderDetails();
    }
}