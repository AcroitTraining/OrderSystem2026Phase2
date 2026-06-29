<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>ホーム画面 - I am the lightning!</title>
</head>
<body>
    <p>this is menu</p>

    <div class="menu-group">
        <form action="OrderManagementServlet" method="doGet">
            <button type="submit" name="orderManagementButton">注文管理</button>
        </form>
    </div>

    <div class="menu-group">
        <form action="ProductListServlet" method="doget">
            <button type="submit" name="productButton">商品一覧</button>
        </form>
    </div>

    <div class="menu-group">
        <form action="ToppingListServlet" method="doGet">
            <button type="submit" name="toppingButton">トッピング一覧</button>
        </form>
    </div>

    <div class="menu-group">
        <form action="CategoryListServlet" method="doGet">
            <button type="submit" name="categoryButton">カテゴリ一覧</button>
        </form>
    </div>

    <div class="logout-section">
        <form action="index.jsp" method="doGet">
            <button type="submit" name="logoutButton">ログアウト</button>
        </form>
    </div>

</body>
</html>