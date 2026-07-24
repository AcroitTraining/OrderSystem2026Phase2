<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ja">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>新規アカウント作成</title>
<link rel="stylesheet" type="text/css" href="css/Login.css">
</head>
<body>

    <div class="page-container">
        <!-- 左側：入力フォーム -->
        <div class="login-section">
            <h1 class="page-title">新規作成<span class="dot">.</span></h1>

            <form action="RegisterServlet" method="POST">
                
                <div class="form-group">
                    <label for="userId">ID</label>
                    <input type="text" id="userId" name="userId" placeholder="IDを入力" autocomplete="off">
                </div>
                
                <div class="form-group">
                    <label for="password">パスワード</label>
                    <div class="password-wrapper">
                        <input type="password" id="password" name="password" placeholder="パスワードを入力" autocomplete="new-password">
                        <img src="image/eye-off.png" class="toggle-password" id="togglePassword" alt="表示切替">
                    </div>
                </div>
                
                <div class="form-group" style="margin-bottom: 0;">
                    <button type="submit" class="submit-btn">登録する</button>
                    
                    <% if("empty".equals(request.getParameter("error"))) { %>
                        <span class="error-message" style="margin-top: 14px;">すべての項目を入力してください。</span>
                    <% } else if("invalid_char".equals(request.getParameter("error"))) { %>
                        <span class="error-message" style="margin-top: 14px;">全角文字は使用できません。</span>
                    <% } else if("exists".equals(request.getParameter("error"))) { %>
                        <span class="error-message" style="margin-top: 14px;">このIDは既に使われています。</span>
                    <% } else if("db_error".equals(request.getParameter("error"))) { %>
                        <span class="error-message" style="margin-top: 14px;">登録に失敗しました。もう一度お試しください。</span>
                    <% } %>
                </div>

                <div class="register-link-box">
                    <a href="index.jsp" class="back-link">ログイン画面に戻る</a>
                </div>
                
            </form>
        </div>

        <!-- 右側：お好み焼き画像背景 -->
        <div class="image-section"></div>
    </div>

    <!-- JavaScript: 初期クリア ＆ アイコン画像切り替え -->
    <script>
        window.addEventListener('DOMContentLoaded', () => {
            const userIdInput = document.getElementById('userId');
            const passwordInput = document.getElementById('password');
            const togglePassword = document.getElementById('togglePassword');

            // 1. 画面表示時は初期値を空にする
            setTimeout(() => {
                userIdInput.value = '';
                passwordInput.value = '';
            }, 50);

            // 2. パスワード表示／非表示の画像切り替え
            togglePassword.addEventListener('click', () => {
                const isPassword = passwordInput.getAttribute('type') === 'password';
                passwordInput.setAttribute('type', isPassword ? 'text' : 'password');
                // 非表示時は eye-off.png、表示時は eye.png に切り替え
                togglePassword.src = isPassword ? 'image/eye.png' : 'image/eye-off.png';
            });
        });
    </script>

</body>
</html>