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
import servlet.EditOrderServlet;

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
    private RequestDispatcher dispatcher;

    @InjectMocks
    private EditOrderServlet servlet;

    @BeforeEach
    void setUp() {
        servlet.setEditOrderDAO(dao);
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

}
