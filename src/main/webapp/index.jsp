<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ja">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>ログイン</title>
<link rel="stylesheet" type="text/css" href="css/Login.css">
</head>
<body>

    <div class="page-container">
        <!-- 左側：入力フォーム -->
        <div class="login-section">
            <h1 class="page-title">ログイン<span class="dot">.</span></h1>

            <form action="LoginServlet" method="POST">
                
                <div class="form-group">
                    <label for="userId">ID</label>
                    <input type="text" id="userId" name="userId" value="${param.userId != null ? param.userId : ''}" autocomplete="off">
                    <% if("empty".equals(request.getParameter("error")) && (request.getParameter("userId") == null || request.getParameter("userId").isEmpty())) { %>
                        <span class="error-message">IDを入力してください</span>
                    <% } %>
                </div>
                
                <div class="form-group">
                    <label for="password">パスワード</label>
                    <input type="password" id="password" name="password">
                    <% if("empty".equals(request.getParameter("error")) && (request.getParameter("password") == null || request.getParameter("password").isEmpty())) { %>
                        <span class="error-message">パスワードを入力してください</span>
                    <% } %>
                </div>
                
                <div class="form-group" style="margin-bottom: 0;">
                    <button type="submit" class="submit-btn">ログイン</button>
                    
                    <% if("wrong".equals(request.getParameter("error"))) { %>
                        <span class="error-message" style="margin-top: 14px;">IDまたはパスワードが間違っています。</span>
                    <% } else if("registered".equals(request.getParameter("msg"))) { %>
                        <span class="success-message">アカウントを作成しました。ログインしてください。</span>
                    <% } %>
                </div>

                <div class="register-link-box">
                    <a href="register.jsp" class="register-btn">アカウントをお持ちでない方はこちら</a>
                </div>
                
            </form>
        </div>

        <!-- 右側：お好み焼き画像背景 -->
        <div class="image-section"></div>
    </div>

</body>
</html>