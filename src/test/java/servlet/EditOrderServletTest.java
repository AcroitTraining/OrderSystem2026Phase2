package servlet;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import dao.EditOrderDAO;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.EditOrderInfo;
import model.EditOrderLogic;

class EditOrderServletTest {
	
	@Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private HttpSession session;

    @Mock
    private EditOrderDAO dao;
    
    @Mock
    private EditOrderLogic logic;
    
    @Mock
    private RequestDispatcher dispatcher;

    @InjectMocks
    private EditOrderServlet servlet;

    @BeforeEach
    void setUp() {
        servlet.setEditOrderDAO(dao);
        servlet.setLogic(logic);
    }

    @Test
    void testDoGet() throws Exception {

        EditOrderInfo testInfo = new EditOrderInfo();
        testInfo.setProductId(1);

        when(request.getParameter("oid")).thenReturn("100");
        when(request.getParameter("from")).thenReturn("order");

        when(dao.findOrderDetails(100)).thenReturn(testInfo);
        when(dao.findToppingListByProductId(1, 100)).thenReturn(new ArrayList<>());

        when(request.getRequestDispatcher("WEB-INF/jsp/editOrder.jsp"))
                .thenReturn(dispatcher);

        servlet.doGet(request, response);

        verify(dao).findOrderDetails(100);
        verify(dao).findToppingListByProductId(1, 100);

        verify(request).setAttribute("editOrderInfo", testInfo);
        verify(request).setAttribute("from", "order");

        verify(dispatcher).forward(request, response);
    }
    
    @Test
    void testDoPost_MainMinus() throws Exception {

        // テストデータ
        EditOrderInfo info = new EditOrderInfo();
        info.setProductQuantity(2);

        // モック設定
        when(request.getParameter("oid")).thenReturn("100");
        when(request.getParameter("from")).thenReturn("order");
        when(request.getParameter("Button")).thenReturn("main_minus");
        when(request.getParameter("mode")).thenReturn(null);

        when(logic.loadOrder(100)).thenReturn(info);

        when(request.getRequestDispatcher("WEB-INF/jsp/editOrder.jsp"))
                .thenReturn(dispatcher);

        // 実行
        servlet.doPost(request, response);

        // Logicが呼ばれたか
        verify(logic).loadOrder(100);
        verify(logic).updateInfoFromRequest(info, request);
        verify(logic).calcQuantity(info, "main_minus");

        // requestへセットされたか
        verify(request).setAttribute("editOrderInfo", info);
        verify(request).setAttribute("from", "order");

        // forwardされたか
        verify(dispatcher).forward(request, response);

        // redirectされていないか
        verify(response, never()).sendRedirect(anyString());
    }

}
