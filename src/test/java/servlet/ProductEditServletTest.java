package servlet;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import dao.ProductEditDAO;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.ProductEditInfo;

@ExtendWith(MockitoExtension.class)
class ProductEditServletTest {

    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private RequestDispatcher requestDispatcher;
    @Mock
    private ProductEditDAO dao;

    @InjectMocks
    private ProductEditServlet servlet;

    private ByteArrayOutputStream responseBody;

    @BeforeEach
    void setUp() {
        servlet.setProductEditDAO(dao);
    }

    /** doGetのcheckDuplicateアクション用に、JSON出力を捕捉するPrintWriterを用意する */
    private PrintWriter setupJsonWriter() throws IOException {
        responseBody = new ByteArrayOutputStream();
        PrintWriter writer = new PrintWriter(responseBody, true);
        when(response.getWriter()).thenReturn(writer);
        return writer;
    }

    // ==================== doGet: checkDuplicate ====================

    @Test
    void doGet_checkDuplicate_重複ありならtrueを返す() throws Exception {
        when(request.getParameter("action")).thenReturn("checkDuplicate");
        when(request.getParameter("productName")).thenReturn("たこ焼き");
        when(request.getParameter("productId")).thenReturn("5");
        when(dao.isProductNameExists("たこ焼き", 5)).thenReturn(true);
        setupJsonWriter();

        servlet.doGet(request, response);

        verify(response).setContentType("application/json");
        assertTrue(responseBody.toString().contains("\"duplicate\": true"));
    }

    @Test
    void doGet_checkDuplicate_重複なしならfalseを返す() throws Exception {
        when(request.getParameter("action")).thenReturn("checkDuplicate");
        when(request.getParameter("productName")).thenReturn("新商品");
        when(request.getParameter("productId")).thenReturn(null);
        when(dao.isProductNameExists("新商品", 0)).thenReturn(false);
        setupJsonWriter();

        servlet.doGet(request, response);

        assertTrue(responseBody.toString().contains("\"duplicate\": false"));
    }

    @Test
    void doGet_checkDuplicate_SQLExceptionでもfalseを返しフォワードしない() throws Exception {
        when(request.getParameter("action")).thenReturn("checkDuplicate");
        when(request.getParameter("productName")).thenReturn("商品A");
        when(request.getParameter("productId")).thenReturn(null);
        when(dao.isProductNameExists(anyString(), anyInt())).thenThrow(new SQLException("DB error"));
        setupJsonWriter();

        servlet.doGet(request, response);

        assertTrue(responseBody.toString().contains("\"duplicate\": false"));
        verify(request, never()).getRequestDispatcher(anyString());
    }

    // ==================== doGet: 通常表示 ====================

    @Test
    void doGet_productIdなしなら新規用の空infoでフォワードする() throws Exception {
        when(request.getParameter("action")).thenReturn(null);
        when(request.getParameter("productId")).thenReturn(null);
        List<ProductEditInfo.ToppingMaster> toppings = Collections.emptyList();
        when(dao.findAllToppings()).thenReturn(toppings);
        when(request.getRequestDispatcher("WEB-INF/jsp/productEdit.jsp")).thenReturn(requestDispatcher);

        servlet.doGet(request, response);

        verify(request).setAttribute(eq("allToppings"), eq(toppings));
        verify(request).setAttribute(eq("productEditInfo"), any(ProductEditInfo.class));
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    void doGet_既存商品が見つかればフォワードする() throws Exception {
        ProductEditInfo info = new ProductEditInfo();
        info.setProductId(3);

        when(request.getParameter("action")).thenReturn(null);
        when(request.getParameter("productId")).thenReturn("3");
        when(dao.findAllToppings()).thenReturn(Collections.emptyList());
        when(dao.findProductDetails(3)).thenReturn(info);
        when(request.getRequestDispatcher("WEB-INF/jsp/productEdit.jsp")).thenReturn(requestDispatcher);

        servlet.doGet(request, response);

        verify(request).setAttribute("productEditInfo", info);
        verify(requestDispatcher).forward(request, response);
        verify(response, never()).sendRedirect(anyString());
    }

    @Test
    void doGet_既存商品が見つからなければ一覧へリダイレクトする() throws Exception {
        when(request.getParameter("action")).thenReturn(null);
        when(request.getParameter("productId")).thenReturn("999");
        when(dao.findAllToppings()).thenReturn(Collections.emptyList());
        when(dao.findProductDetails(999)).thenReturn(null);

        servlet.doGet(request, response);

        verify(response).sendRedirect("ProductListServlet");
        verify(request, never()).getRequestDispatcher(anyString());
    }

    @Test
    void doGet_SQLExceptionなら500を返す() throws Exception {
        when(request.getParameter("action")).thenReturn(null);
        when(dao.findAllToppings()).thenThrow(new SQLException("DB error"));

        servlet.doGet(request, response);

        verify(response).sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }

    // ==================== doPost ====================

    @Test
    void doPost_既存商品が有効なら更新してリダイレクトする() throws Exception {
        when(request.getParameter("productId")).thenReturn("7");
        when(request.getParameter("productName")).thenReturn("既存商品");
        when(request.getParameter("productPrice")).thenReturn("800");
        when(request.getParameter("categoryName")).thenReturn("ドリンク");
        when(request.getParameterValues("toppingId")).thenReturn(null);
        when(dao.saveProduct(eq(7), eq("既存商品"), eq(800), eq("ドリンク"), any())).thenReturn(true);

        servlet.doPost(request, response);

        verify(dao).saveProduct(7, "既存商品", 800, "ドリンク", null);
        verify(response).sendRedirect("ProductListServlet");
    }

    @Test
    void doPost_入力が不正ならBadRequestを返す() throws Exception {
        when(request.getParameter("productId")).thenReturn(null);
        when(request.getParameter("productName")).thenReturn("");
        when(request.getParameter("productPrice")).thenReturn("100");
        when(request.getParameter("categoryName")).thenReturn("フード");
        when(dao.saveProduct(anyInt(), anyString(), anyInt(), anyString(), any())).thenReturn(false);

        servlet.doPost(request, response);

        verify(response).sendError(eq(HttpServletResponse.SC_BAD_REQUEST), anyString());
        verify(response, never()).sendRedirect(anyString());
    }

    @Test
    void doPost_SQLExceptionなら500を返す() throws Exception {
        when(request.getParameter("productId")).thenReturn(null);
        when(request.getParameter("productName")).thenReturn("商品X");
        when(request.getParameter("productPrice")).thenReturn("300");
        when(request.getParameter("categoryName")).thenReturn("フード");
        doThrow(new SQLException("DB error"))
                .when(dao).saveProduct(anyInt(), anyString(), anyInt(), anyString(), any());

        servlet.doPost(request, response);

        verify(response).sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        verify(response, never()).sendRedirect(anyString());
    }

    @Test
    void doPost_productIdが数値変換できない形式なら例外が伝播しない範囲を確認() throws Exception {
        // productId="0"扱いになるケースの確認（空文字）
        when(request.getParameter("productId")).thenReturn("");
        when(request.getParameter("productName")).thenReturn("商品Y");
        when(request.getParameter("productPrice")).thenReturn("");
        when(request.getParameter("categoryName")).thenReturn("フード");
        when(dao.saveProduct(eq(0), eq("商品Y"), eq(0), eq("フード"), any())).thenReturn(true);

        servlet.doPost(request, response);

        verify(dao).saveProduct(0, "商品Y", 0, "フード", null);
        verify(response).sendRedirect("ProductListServlet");
    }
}