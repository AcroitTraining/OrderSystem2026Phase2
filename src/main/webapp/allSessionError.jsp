<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ja">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="color-scheme" content="light only">
<title>エラー</title>
<style>
    :root {
        color-scheme: light only;
    }
    html, body {
        margin: 0;
        padding: 0;
        background-color: #ffffff;
    }
    body {
        min-height: 100vh;
        display: flex;
        align-items: center;
        justify-content: center;
        font-family: "Hiragino Kaku Gothic ProN", "Meiryo", sans-serif;
        padding: 24px;
        box-sizing: border-box;
    }
    .error-card {
        width: 100%;
        max-width: 340px;
        padding: 40px 28px;
        border: 1px solid #e2e2e2;
        border-radius: 16px;
        box-shadow: 0 2px 10px rgba(0, 0, 0, 0.06);
        text-align: center;
        box-sizing: border-box;
        background-color: #ffffff;
    }
    .error-title {
        font-size: 22px;
        font-weight: bold;
        color: #222222;
        margin: 0 0 20px;
        white-space: nowrap;
    }
    .error-message {
        font-size: 14px;
        color: #555555;
        line-height: 1.8;
        margin: 0 0 24px;
    }
    .login-button {
        display: inline-block;
        background-color: #2e7d32;
        color: #ffffff;
        text-decoration: none;
        font-size: 14px;
        font-weight: bold;
        padding: 10px 32px;
        border-radius: 24px;
    }
    .login-button:hover {
        background-color: #256428;
    }

    /* 極端に幅の狭い端末のみタイトルの折り返しを許可 */
    @media (max-width: 280px) {
        .error-title {
            white-space: normal;
            font-size: 18px;
        }
    }
</style>
</head>
<body>
    <div class="error-card">
        <p class="error-title">問題が発生しました</p>
        <p class="error-message">お手数ですが、ログイン画面に<br>再度アクセスしてください</p>
        <a class="login-button" href="index.jsp">ログインへ</a>
    </div>
</body>
</html>