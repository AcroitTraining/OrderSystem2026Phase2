<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="ja">
<head>
<meta charset="UTF-8">
<title>カテゴリ${categoryEditInfo.categoryId == 0 ? '作成' : '編集'}</title>
<link rel="stylesheet" href="./css/categoryEdit.css">
</head>
<body>
	<div id="bootScreen" class="boot-screen">

		<div class="boot-core">

			<div class="boot-symbol">◈</div>

			<div class="boot-title">CATEGORY CONTROL SYSTEM</div>

			<div class="boot-loading">INITIALIZING CORE SYSTEM</div>

			<div class="boot-progress">
				<span></span>
			</div>

			<div class="boot-status">
				<span id="bootText"> SYSTEM INITIALIZATION... </span>
			</div>

		</div>

	</div>

	<div class="system-shell">

		<!-- =========================
             システムヘッダー
        ========================== -->

		<header class="system-header">

			<div class="system-logo">

				<span class="logo-symbol">◈</span>

				<div>
					<div class="logo-main">CATEGORY CONTROL</div>

					<div class="logo-sub">MANAGEMENT SYSTEM / CORE INTERFACE</div>
				</div>

			</div>


			<div class="header-actions">

				<a href="HomeServlet" class="home-system-btn"> ⌂ HOME </a>

				<div class="system-status-header">

					<span class="header-status-dot"></span> <span>ONLINE</span>

				</div>

			</div>

		</header>


		<!-- =========================
             メインシステム
        ========================== -->

		<main class="system-main">


			<!-- 左側ステータスパネル -->

			<aside class="side-panel">

				<div class="panel-title">SYSTEM STATUS</div>


				<div class="status-item">

					<span class="status-light"></span>

					<div>
						<div class="status-label">CORE SYSTEM</div>

						<div class="status-value">ONLINE</div>
					</div>

				</div>


				<div class="status-item">

					<span class="status-light"></span>

					<div>
						<div class="status-label">DATABASE</div>

						<div class="status-value">CONNECTED</div>
					</div>

				</div>


				<div class="status-item">

					<span class="status-light"></span>

					<div>
						<div class="status-label">EDIT MODE</div>

						<div class="status-value">READY</div>
					</div>

				</div>


				<div class="side-decoration">◇ ◇ ◇</div>

			</aside>


			<!-- 中央エリア -->

			<section class="control-area">


				<div class="page-title">

					<span class="title-main"> CATEGORY CONTROL </span> <span
						class="title-sub"> カテゴリ構造を編集 </span>

				</div>


				<div class="main-card">


					<div class="card-corner corner-tl">✦</div>
					<div class="card-corner corner-tr">✦</div>
					<div class="card-corner corner-bl">✦</div>
					<div class="card-corner corner-br">✦</div>


					<div class="system-status">

						<span class="status-dot"></span> SYSTEM ONLINE

					</div>


					<form id="categoryForm" action="CategoryEditServlet" method="post">


						<input type="hidden" name="categoryId"
							value="${categoryEditInfo.categoryId}">


						<div class="form-group">

							<label class="label-title"> カテゴリ名 <span class="label-sub">
									（18文字以内） </span>

							</label> <input type="text" id="categoryName" name="categoryName"
								class="input-category-name"
								value="${categoryEditInfo.categoryName}" autocomplete="off">

						</div>


						<div class="footer-actions">

							<a href="CategoryListServlet" class="btn-back" id="returnBtn">
								RETURN </a>


							<button type="button" id="openModalBtn" class="btn-submit">

								${categoryEditInfo.categoryId == 0
                                    ? 'CREATE'
                                    : 'UPDATE'}

							</button>

						</div>


					</form>

				</div>


			</section>


			<!-- 右側情報パネル -->

			<aside class="right-panel">

				<div class="panel-title">DATA CORE</div>


				<div class="core-orb">

					<div class="core-ring ring-one"></div>
					<div class="core-ring ring-two"></div>
					<div class="core-ring ring-three"></div>

					<div class="core-inner">CORE</div>

				</div>


				<div class="core-status">STABLE</div>


				<div class="data-line">
					ID <span> #${categoryEditInfo.categoryId} </span>
				</div>


				<div class="data-line">
					MODE <span> ${categoryEditInfo.categoryId == 0 ? 'CREATE' : 'EDIT'}
					</span>
				</div>


			</aside>


		</main>


		<!-- =========================
             システムフッター
        ========================== -->

		<footer class="system-footer">

			<span> CATEGORY CONTROL SYSTEM </span> <span> DATABASE
				CONNECTION : ACTIVE </span> <span> v2.026.07 </span>

		</footer>


	</div>


	<!-- カテゴリ更新確認モーダル -->
	<div id="modalOverlay" class="modal-overlay" style="display: none;">

		<div class="modal-content">

			<!-- モーダル上部 -->
			<div class="modal-header">

				<div class="modal-system-label">
					<span class="modal-status-dot"></span> CATEGORY CONTROL SYSTEM
				</div>

				<div class="modal-warning">⚠</div>

				<h3>CATEGORY CORE UPDATE</h3>

				<p class="modal-subtitle">以下のデータを更新しますか？</p>

			</div>


			<!-- 更新対象 -->
			<div class="modal-body">

				<div class="data-panel">

					<div class="data-panel-header">
						<span>UPDATE TARGET</span> <span class="data-status">READY</span>
					</div>

					<div class="modal-row">

						<span class="modal-label"> CATEGORY NAME </span> <span
							class="modal-val" id="modalCategoryName"> </span>

					</div>

				</div>

			</div>


			<!-- モーダル下部 -->
			<div class="modal-footer">

				<div class="execute-warning">⚠ この操作はデータベースに反映されます</div>

				<div class="modal-actions">

					<button type="button" id="modalNoBtn" class="btn-modal-no">

						CANCEL</button>

					<button type="button" id="modalYesBtn" class="btn-modal-yes">

						APPLY UPDATE</button>

				</div>

			</div>

		</div>

	</div>

	<!-- FINAL BATTLE MODE -->
	<div id="battleOverlay" class="battle-overlay">

		<div class="battle-container">

			<div class="battle-warning">⚠ SYSTEM OVERRIDE</div>

			<div class="battle-title">CATEGORY CORE UPDATE</div>

			<div class="battle-subtitle">FINAL EXECUTION PROTOCOL</div>

			<div class="battle-core">

				<div class="battle-ring ring-a"></div>
				<div class="battle-ring ring-b"></div>
				<div class="battle-ring ring-c"></div>

				<div class="battle-core-symbol">◈</div>

			</div>

			<div class="battle-progress">

				<div id="battleProgressBar"></div>

			</div>

			<div id="battleMessage" class="battle-message">INITIALIZING
				SYSTEM...</div>

		</div>

	</div>
	
	<!-- SYSTEM SHUTDOWN -->
<div id="shutdownOverlay" class="shutdown-overlay">

    <div class="shutdown-container">

        <div class="shutdown-warning">
            SYSTEM DISCONNECT
        </div>

        <div class="shutdown-title">
            SYSTEM SHUTDOWN
        </div>

        <div class="shutdown-subtitle">
            CLOSING CATEGORY CONTROL SYSTEM
        </div>

        <div class="shutdown-core">

            <div class="shutdown-ring ring-one"></div>
            <div class="shutdown-ring ring-two"></div>
            <div class="shutdown-ring ring-three"></div>

            <div class="shutdown-symbol">
                ◈
            </div>

        </div>

        <div class="shutdown-progress">

            <div id="shutdownProgressBar"></div>

        </div>

        <div id="shutdownMessage" class="shutdown-message">
            DISCONNECTING SYSTEM...
        </div>

    </div>

</div>


<div class="lightning-field"></div>

<div id="lightningFlash"></div>

	<script src="./js/categoryEdit.js" defer></script>
</body>
</html>