<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>ログイン画面</title>
</head>
<body>
    <h2>ログイン</h2>
    <form action="LoginServlet" method="POST">
        ユーザーID: <input type="text" name="userId" required><br>
        パスワード: <input type="password" name="password" required><br>
        <button type="submit">ログイン</button>
    </form>
</body>
</html>
