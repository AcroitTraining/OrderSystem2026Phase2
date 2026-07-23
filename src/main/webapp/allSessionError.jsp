<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isErrorPage="true" %>
<%@ page import="java.io.StringWriter, java.io.PrintWriter" %>
<!DOCTYPE html>
<html lang="ja">
<head>
<meta charset="UTF-8">
<title>エラー詳細画面 (デバッグ用)</title>
<style>
    body {
        font-family: monospace, sans-serif;
        background-color: #f8f9fa;
        padding: 20px;
        color: #333;
    }
    .error-container {
        background: #fff;
        border: 2px solid #dc3545;
        border-radius: 8px;
        padding: 20px;
        max-width: 900px;
        margin: 0 auto;
        box-shadow: 0 4px 12px rgba(0,0,0,0.1);
    }
    h1 {
        color: #dc3545;
        font-size: 20px;
        margin-top: 0;
    }
    .msg-box {
        background: #f8d7da;
        color: #721c24;
        padding: 12px;
        border-radius: 4px;
        font-weight: bold;
        margin-bottom: 15px;
        word-break: break-all;
    }
    pre {
        background: #272822;
        color: #f8f8f2;
        padding: 15px;
        border-radius: 6px;
        overflow-x: auto;
        font-size: 13px;
        line-height: 1.4;
    }
    .btn {
        display: inline-block;
        margin-top: 15px;
        padding: 8px 16px;
        background: #0d6efd;
        color: white;
        text-decoration: none;
        border-radius: 4px;
    }
</style>
</head>
<body>

<div class="error-container">
    <h1>⚠️ エラーが発生しました (詳細ログ)</h1>
    
    <%-- 例外メッセージの取得 --%>
    <%
        Throwable exc = exception;
        if (exc == null) {
            exc = (Throwable) request.getAttribute("jakarta.servlet.error.exception");
        }
        if (exc == null) {
            exc = (Throwable) request.getAttribute("javax.servlet.error.exception");
        }
    %>

    <% if (exc != null) { %>
        <div class="msg-box">
            例外の種類: <%= exc.getClass().getName() %><br>
            メッセージ: <%= exc.getMessage() %>
        </div>

        <h3>スタックトレース:</h3>
        <pre><%
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            exc.printStackTrace(pw);
            out.print(sw.toString());
        %></pre>
    <% } else { %>
        <div class="msg-box">
            例外オブジェクト（Exception）が渡されませんでした。<br>
            ステータスコード: <%= request.getAttribute("jakarta.servlet.error.status_code") %>
        </div>
        <p>※サーブレット側で catch してリダイレクト・フォワードしているか、Filter（AllSessionError等）で捕捉されている可能性があります。</p>
    <% } %>

    <a href="index.jsp" class="btn">ログイン画面へ戻る</a>
</div>

</body>
</html>