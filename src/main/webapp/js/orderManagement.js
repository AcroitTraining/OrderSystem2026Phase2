


/*
 * =========================
 * テーブル1行分のデータ
 * =========================
 */

function createOrderRow(item) {

    const toppingHtml =
        createToppingHtml(item.toppings);

    return `
        <tr class="order-row" data-category="${item.categoryName}">
            <td>No.${item.orderId}</td>
            <td>${item.orderTime}</td>
            <td>${item.orderQuantity}個</td>
            <td>${item.productName}</td>
            <td>${toppingHtml}</td>

            <td>
                <button type="button"
                    class="edit-img-btn"
                    data-order-id="${item.orderId}">
                    <img src="./image/edit_icon.png"
                        alt="注文編集"
                        class="edit-icon-img">
                </button>
            </td>

            <td>
                <button type="button"
                    class="served-btn ${item.timeColorClass}"
                    data-order-id="${item.orderId}">
                    ${item.tableId}卓<br>提供
                </button>
            </td>
        </tr>
    `;
}


/*
 * =========================
 * 現在のフィルター取得
 * =========================
 */

function getCurrentFilters() {

    return {

        category:
            document
                .querySelector(".category-filter .active")
                .value,

        table:
            document
                .querySelector(".table-filter .active")
                .value

    };

}


/*
 * =========================
 * トッピングの出力
 * =========================
 */

function createToppingHtml(toppings) {

    if (!toppings || toppings.length === 0) {

        return "";

    }


    let html = "";


    toppings.forEach(t => {

        html +=
            `・${t.name}×${t.quantity}<br>`;

    });


    return html;

}


/*
 * =========================
 * テーブル更新
 * =========================
 */

function updateTable(orders) {

    let html = "";


    orders.forEach(item => {

        html += createOrderRow(item);

    });


    document
        .getElementById("orderTableBody")
        .innerHTML = html;

}


/*
 * =========================
 * サーバー通信
 * =========================
 */

async function fetchOrders() {
    try {
        const filters = getCurrentFilters();

        const url = "OrderManagementAjaxServlet"
            + "?categoryFilter=" + encodeURIComponent(filters.category)
            + "&tableFilter=" + encodeURIComponent(filters.table);

        const response = await fetch(url);
        const data = await response.json();

        updateTable(data.orders);

    } catch (error) {
        console.error("注文データの取得に失敗しました", error);
    }
}


/*
 * =========================
 * 初回通信
 * =========================
 */

fetchOrders();


/*
 * =========================
 * 5秒ごとに更新
 * =========================
 */

setInterval(

    fetchOrders,

    5000

);


/*
 * =========================
 * ボタン操作
 * =========================
 */

document
    .getElementById("orderTableBody")
    .addEventListener(
        "click",
        event => {

            /*
             * =========================
             * 編集ボタン
             * =========================
             */

            const editButton =
                event.target.closest(
                    ".edit-img-btn"
                );


            if (editButton) {

                const orderId =
                    editButton.dataset.orderId;


                const url =

                    "EditOrderServlet"

                    + "?action="

                    + encodeURIComponent(
                        "注文編集"
                    )

                    + "&oid="

                    + encodeURIComponent(
                        orderId
                    )

                    + "&from="

                    + encodeURIComponent(
                        "orderManagement"
                    );


                window.location.href =
                    url;

            }


            /*
             * =========================
             * 提供ボタン
             * =========================
             */

            const servedButton =
                event.target.closest(
                    ".served-btn"
                );


            if (servedButton) {

                const orderId =
                    servedButton.dataset.orderId;


                const form =
                    document.createElement(
                        "form"
                    );


                form.method =
                    "POST";


                form.action =
                    "OrderManagementServlet";


                const oidInput =
                    document.createElement(
                        "input"
                    );


                oidInput.type =
                    "hidden";


                oidInput.name =
                    "oid";


                oidInput.value =
                    orderId;


                const actionInput =
                    document.createElement(
                        "input"
                    );


                actionInput.type =
                    "hidden";


                actionInput.name =
                    "action";


                actionInput.value =
                    "提供";


                form.appendChild(
                    oidInput
                );


                form.appendChild(
                    actionInput
                );


                document.body.appendChild(
                    form
                );


                form.submit();

            }

        }
    );