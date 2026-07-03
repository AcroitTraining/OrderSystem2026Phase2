<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>ログイン</title>
<link rel="stylesheet" type="text/css" href="css/Login.css">
</head>
<body>

    <div class="page-title">ログイン</div>

    <div class="login-container">
        <form action="LoginServlet" method="POST">
            
            <div class="form-group">
                <label tensor-id="userId">ID</label>
                <input type="text" id="userId" name="userId" value="${param.userId != null ? param.userId : ''}">
                <%-- 未入力の時に、IDのすぐ下に赤文字を表示する(画像1を再現) --%>
                <% if("empty".equals(request.getParameter("error")) && (request.getParameter("userId") == null || request.getParameter("userId").isEmpty())) { %>
                    <span class="error-message">IDを入力してください</span>
                <% } %>
            </div>
            
            <div class="form-group">
                <label tensor-id="password">パスワード</label>
                <input type="password" id="password" name="password">
                <%-- 未入力の時に、パスワードのすぐ下に赤文字を表示する(画像1を再現) --%>
                <% if("empty".equals(request.getParameter("error")) && (request.getParameter("password") == null || request.getParameter("password").isEmpty())) { %>
                    <span class="error-message">パスワードを入力してください</span>
                <% } %>
            </div>
            
            <div class="form-group" style="margin-bottom: 0;">
                <button type="submit" class="submit-btn">ログイン</button>
                
                <%-- IDまたはパスワードが違った時に、ボタンの下に赤文字を表示する(画像2・3を再現) --%>
                <% if("wrong".equals(request.getParameter("error"))) { %>
                    <span class="error-message" style="margin-top: 12px;">IDまたはパスワードが間違っています。</span>
                <% } %>
            </div>
            
        </form>
    </div>

</body>
</html>