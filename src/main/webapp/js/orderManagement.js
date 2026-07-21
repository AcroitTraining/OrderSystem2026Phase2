document.addEventListener("DOMContentLoaded", () => {

    /*
     * =========================
     * モーダル
     * =========================
     */

    const servedForms =
        document.querySelectorAll(".served-form");

    const modal =
        document.getElementById("customModal");

    const yesBtn =
        document.getElementById("modalYesBtn");

    const noBtn =
        document.getElementById("modalNoBtn");

    let currentForm = null;

    servedForms.forEach(form => {

        form.addEventListener("submit", event => {

            event.preventDefault();

            currentForm = form;

            modal.style.display = "flex";

        });

    });

    yesBtn.addEventListener("click", () => {

        if (currentForm) {

            currentForm.submit();

        }

    });

    noBtn.addEventListener("click", () => {

        modal.style.display = "none";

        currentForm = null;

    });

    modal.addEventListener("click", event => {

        if (event.target === modal) {

            modal.style.display = "none";

            currentForm = null;

        }

    });


    /*
     * =========================
     * タブスクロール
     * =========================
     */

    const categoryTabs =
        document.getElementById("categoryTabs");

    const rightBtn =
        document.querySelector(".scroll-btn.right");

    const leftBtn =
        document.querySelector(".scroll-btn.left");


    rightBtn.addEventListener("click", () => {

        categoryTabs.scrollBy({
            left: 200,
            behavior: "smooth"
        });

    });


    leftBtn.addEventListener("click", () => {

        categoryTabs.scrollBy({
            left: -200,
            behavior: "smooth"
        });

    });


    /*
     * =========================
     * ドラッグスクロール
     * =========================
     */

    let isDown = false;

    let startX;

    let scrollLeft;


    categoryTabs.addEventListener("mousedown", event => {

        isDown = true;

        categoryTabs.classList.add("dragging");

        startX =
            event.pageX - categoryTabs.offsetLeft;

        scrollLeft =
            categoryTabs.scrollLeft;

    });


    categoryTabs.addEventListener("mouseleave", () => {

        isDown = false;

        categoryTabs.classList.remove("dragging");

    });


    categoryTabs.addEventListener("mouseup", () => {

        isDown = false;

        categoryTabs.classList.remove("dragging");

    });


    categoryTabs.addEventListener("mousemove", event => {

        if (!isDown) {
            return;
        }

        event.preventDefault();

        const x =
            event.pageX - categoryTabs.offsetLeft;

        const walk =
            (x - startX) * 1.5;

        categoryTabs.scrollLeft =
            scrollLeft - walk;

    });

});

    
//テーブル1行分のデータ
// テーブル1行分のデータ
function createOrderRow(item) {

    const toppingHtml =
        createToppingHtml(item.toppings);

    return "<tr class=\"order-row\" data-category=\""
        + item.categoryName
        + "\">"
        + "<td>No."
        + item.orderId
        + "</td>"
        + "<td>"
        + item.orderTime
        + "</td>"
        + "<td>"
        + item.orderQuantity
        + "個</td>"
        + "<td>"
        + item.productName
        + "</td>"
        + "<td>"
        + toppingHtml
        + "</td>"
        + "</tr>";

}

function getCurrentFilters() {
    return {
        category: document.querySelector(".category-filter .active").value,
        table: document.querySelector(".table-filter .active").value
    };
}


//トッピングの出力
function createToppingHtml(toppings) {
	
	if (!toppings || toppings.length === 0) {
        return "";
    }

    let html = "";

    toppings.forEach(t => {
        html += `・${t.name}×${t.quantity}<br>`;
    });

    return html;
}

//更新処理
function updateTable(orders) {

    let html = "";

    orders.forEach(item => {
        html += createOrderRow(item);
    });

    document.getElementById("orderTableBody").innerHTML = html;
}



async function fetchOrders() {
    try {
        const filters = getCurrentFilters();

        const url =
            "OrderManagementAjaxServlet"
            + "?categoryFilter="
            + encodeURIComponent(filters.category)
            + "&tableFilter="
            + encodeURIComponent(filters.table);

        const response = await fetch(url);

        const data = await response.json();

        updateTable(data.orders);

    } catch (error) {
        console.error(
            "注文データの取得に失敗しました",
            error
        );
    }
}

//通信する関数を呼び出す
fetchOrders();

//5秒ごとに呼び出す
setInterval(fetchOrders, 5000);
