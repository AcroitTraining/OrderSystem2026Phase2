<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="ja">
<head>
<meta charset="UTF-8">
<title>カテゴリ${categoryEditInfo.categoryId == 0 ? '作成' : '編集'}</title>
<link rel="stylesheet" href="./css/categoryEdit.css">
</head>
<body>

    <div class="home-container">
        <a href="HomeServlet" class="home-btn">
            <img src="./images/home_icon.png" alt="ホーム" class="home-icon">
        </a>
    </div>

    <div class="page-title">
        カテゴリ${categoryEditInfo.categoryId == 0 ? '新規登録' : '編集'}
    </div>

    <div class="main-card">
        <form id="categoryForm" action="CategoryEditServlet" method="post">
            <input type="hidden" name="categoryId" value="${categoryEditInfo.categoryId}">

            <div class="form-group">
                <label class="label-title">
                    カテゴリ名 <span class="label-sub">（18文字以内）</span>
                </label>
                <input type="text" id="categoryName" name="categoryName" class="input-category-name" 
                       value="${categoryEditInfo.categoryName}" autocomplete="off">
            </div>

            <div class="footer-actions">
                <a href="CategoryListServlet" class="btn-back">戻る</a>
                <button type="button" id="openModalBtn" class="btn-submit">
                    ${categoryEditInfo.categoryId == 0 ? '作成' : '変更'}
                </button>
            </div>
        </form>
    </div>

    <!-- 確認モーダル -->
    <div id="modalOverlay" class="modal-overlay" style="display: none;">
        <div class="modal-content">
            <h3>以下の内容で${categoryEditInfo.categoryId == 0 ? '作成' : '変更'}しますか？</h3>
            <div class="modal-body">
                <div class="modal-row">
                    <span class="modal-label">カテゴリ名</span>
                    <span id="modalCategoryName" class="modal-val"></span>
                </div>
            </div>
            <div class="modal-actions">
                <button type="button" id="modalNoBtn" class="btn-modal-no">いいえ</button>
                <button type="button" id="modalYesBtn" class="btn-modal-yes">はい</button>
            </div>
        </div>
    </div>

    <script src="./js/categoryEdit.js" defer></script>
</body>
</html>