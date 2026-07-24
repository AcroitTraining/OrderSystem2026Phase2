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
import model.ProductListInfo;
import model.ProductListLogic;

@ExtendWith(MockitoExtension.class)
class ProductListTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

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

        String categoryId = "1";

        List<ProductListInfo> categoryList = new ArrayList<>();
        List<ProductListInfo> pList = new ArrayList<>();
        List<ProductListInfo> filteredList = new ArrayList<>();

        when(request.getParameter("categoryId")).thenReturn(categoryId);
        when(dao.findAllCategories()).thenReturn(categoryList);
        when(dao.findAllProduct()).thenReturn(pList);
        when(logic.filterProducts(pList, 1)).thenReturn(filteredList);
        when(request.getRequestDispatcher("/WEB-INF/jsp/productList.jsp")).thenReturn(dispatcher);

        servlet.doGet(request, response);

        verify(dao).findAllCategories();
        verify(dao).findAllProduct();
        verify(logic).filterProducts(pList, 1);

        verify(request).setAttribute("categoryList", categoryList);
        verify(request).setAttribute("pList", filteredList);
        verify(request).setAttribute("currentCategoryId", 1);

        verify(dispatcher).forward(request, response);
    }

    @Test
    void testDoPost_DisplayFlagChange() throws Exception {

        String productId = "1";
        String action = "表示切り替え";

        when(request.getParameter("productId")).thenReturn(productId);
        when(request.getParameter("action")).thenReturn(action);

        servlet.doPost(request, response);

        verify(dao).updateProductDisplayFlag(1);
        verify(response).sendRedirect("ProductListServlet");
    }

    @Test
    void testDoPost_DeleteFlagChange() throws Exception {

        String productId = "1";
        String action = "削除";

        when(request.getParameter("productId")).thenReturn(productId);
        when(request.getParameter("action")).thenReturn(action);

        servlet.doPost(request, response);

        verify(dao).updateProductDeleteFlag(1);
        verify(response).sendRedirect("ProductListServlet");
    }

    @Test
    void testDoPost_ProductIdIsNull() throws Exception {

        when(request.getParameter("productId")).thenReturn(null);

        servlet.doPost(request, response);

        verify(response).sendRedirect("ProductListServlet");

        verifyNoInteractions(dao);
    }
}