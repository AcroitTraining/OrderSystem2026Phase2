<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" href="./css/orderManagement.css">
<link rel="stylesheet" href="./css/common.css">
<title>注文管理画面</title>
</head>
<body>
	<form action="HomeServlet" method="get">
		<input type="submit" name="action" value="home">
	</form>

	<category-area>
	<form action="OrderManagementServlet" method="get"
		style="display: inline;">
		<!-- カテゴリタブ部分：初期値の0をEL式に変更 -->
		<div class="category-tabs">
			<button type="submit" name="tableFilter" value="全て" class="tab-btn"
				data-category="全て">
				全て
				<c:if test="${countAll > 0}">
					<span class="badge">${countAll}</span>
				</c:if>
			</button>
			<button type="submit" name="tableFilter" value="お好み焼き"
				class="tab-btn" data-category="お好み焼き">
				お好み焼き
				<c:if test="${countOkonomi > 0}">
					<span class="badge">${countOkonomi}</span>
				</c:if>
			</button>
			<button type="submit" name="tableFilter" value="もんじゃ焼き"
				class="tab-btn" data-category="もんじゃ焼き">
				もんじゃ焼き
				<c:if test="${countMonja > 0}">
					<span class="badge">${countMonja}</span>
				</c:if>
			</button>
			<button type="submit" name="tableFilter" value="鉄板焼き" class="tab-btn"
				data-category="鉄板焼き">
				鉄板焼き
				<c:if test="${countTeppan > 0}">
					<span class="badge">${countTeppan}</span>
				</c:if>
			</button>
			<button type="submit" name="tableFilter" value="サイドメニュー"
				class="tab-btn" data-category="サイドメニュー">
				サイドメニュー
				<c:if test="${countSide > 0}">
					<span class="badge">${countSide}</span>
				</c:if>
			</button>
			<button type="submit" name="tableFilter" value="ソフトドリンク"
				class="tab-btn" data-category="ソフトドリンク">
				ソフトドリンク
				<c:if test="${countSoft > 0}">
					<span class="badge">${countSoft}</span>
				</c:if>
			</button>
			<button type="submit" name="tableFilter" value="お酒" class="tab-btn"
				data-category="お酒">
				お酒
				<c:if test="${countSake > 0}">
					<span class="badge">${countSake}</span>
				</c:if>
			</button>
			<button type="submit" name="tableFilter" value="ボトル" class="tab-btn"
				data-category="ボトル">
				ボトル
				<c:if test="${countBottle > 0}">
					<span class="badge">${countBottle}</span>
				</c:if>
			</button>
		</div>
	</form>
	</category-area>


	<h1>注文管理画面</h1>
	<table class="order-table">
		<tr class="order-row" data-category="${item.categoryName}">
			<th>No.</th>
			<th>注文時間</th>
			<th>個数</th>
			<th>商品</th>
			<th>トッピング</th>
			<th>編集</th>
			<th>卓番/提供</th>
		</tr>
		<c:forEach var="item" items="${omList}">
			<tr class="order-row" data-category="${item.categoryName}">
				<td>${item.orderId}</td>
				<td>${item.orderTime}</td>
				<td>${item.orderQuantity}</td>
				<td>${item.productName}</td>
				<td><c:if test="${!empty item.toppings}">
						<c:forEach var="t" items="${item.toppings}">
                    ・${t.name}✕${t.quantity}<br>
						</c:forEach>
					</c:if></td>
				<td>
					<form action="EditOrderServlet" method="get">
						<input type="submit" name="action" value="注文編集"> <input
							type="hidden" name="oid" value="${item.orderId}"> <input
							type="hidden" name="from" value="orderManagement">
					</form>
				</td>
				<td>
					<form action="OrderManagementServlet" method="post">
						<input type="submit" name="action" value="提供"
							class="${item.timeColorClass}"> <input type="hidden"
							name="oid" value="${item.orderId}">
					</form>
				</td>
			</tr>
		</c:forEach>
	</table>

	<table class="footer-table">
		<tr>
			<td class="bg-green"
				style="display: table-cell; border: solid 1px #330000;">～5分</td>
			<td class="bg-yellow"
				style="display: table-cell; border: solid 1px #330000; color: #333333;">5分～10分</td>
			<td class="bg-red"
				style="display: table-cell; border: solid 1px #330000;">10分～
				<form action="ServedHistoryServlet" method="get">
					<input type="submit" name="action" value="履歴">
				</form>
			</td>
		</tr>
	</table>
	
</body>
</html>
