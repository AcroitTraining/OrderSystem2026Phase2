<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta charset="UTF-8">
<title>ホーム画面</title>
<link rel="stylesheet" type="text/css" href="css/home.css">
</head>
<body>

    <div class="container">
        <div class="logout-section">
            <form action="index.jsp" method="get">
                <button type="submit" name="logoutButton" class="btn-img">
                    <img src="image/logoutButton.png" alt="ログアウト">
                </button>
            </form>
        </div>

        <h1 class="main-title">ホームメニュー</h1>
        <hr class="separator">

        <section class="menu-section">
            <h2 class="section-title">注文管理</h2>
            <div class="button-group">
                <form action="OrderManagementServlet" method="get">
                    <button type="submit" name="orderManagementButton" class="btn-img">
                        <img src="image/orderListButton.png" alt="注文一覧">
                    </button>
                </form>
            </div>
        </section>

        <hr class="separator">

        <section class="menu-section">
            <h2 class="section-title">メニュー管理</h2>
            <div class="button-group horizontal">
                <form action="ProductListServlet" method="get">
                    <button type="submit" name="productButton" class="btn-img">
                        <img src="image/productButton.png" alt="商品">
                    </button>
                </form>

                <form action="ToppingListServlet" method="get">
                    <button type="submit" name="toppingButton" class="btn-img">
                        <img src="image/toppingButton.png" alt="トッピング">
                    </button>
                </form>

                <form action="CategoryListServlet" method="get">
                    <button type="submit" name="categoryButton" class="btn-img">
                        <img src="image/categoryButton.png" alt="カテゴリ">
                    </button>
                </form>
            </div>
        </section>
    </div>

</body>
</html>