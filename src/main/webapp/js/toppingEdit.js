document.addEventListener("DOMContentLoaded", function () {

    var form = document.getElementById("toppingForm");

    var toppingNameInput =
        document.getElementById("toppingName");

    var toppingPriceInput =
        document.getElementById("toppingPrice");

    var toppingStockInput =
        document.getElementById("toppingStock");

    var openModalBtn =
        document.getElementById("openModalBtn");


    var modalOverlay =
        document.getElementById("modalOverlay");

    var modalNoBtn =
        document.getElementById("modalNoBtn");

    var modalYesBtn =
        document.getElementById("modalYesBtn");


    var modalToppingName =
        document.getElementById("modalToppingName");

    var modalToppingPrice =
        document.getElementById("modalToppingPrice");

    var modalToppingStock =
        document.getElementById("modalToppingStock");


    if (!form || !openModalBtn || !modalOverlay) {

        console.error(
            "toppingEdit.js: 必要な要素が見つかりません。",
            {
                form: form,
                openModalBtn: openModalBtn,
                modalOverlay: modalOverlay
            }
        );

        return;
    }


    // エラー表示用スパンの自動生成
    function createErrorElement(inputElement) {

        var errorSpan =
            inputElement.parentNode
                .querySelector(".error-message");


        if (!errorSpan) {

            errorSpan =
                document.createElement("span");

            errorSpan.className =
                "error-message";

            inputElement.parentNode
                .appendChild(errorSpan);
        }


        return errorSpan;
    }


    var nameError =
        createErrorElement(toppingNameInput);

    var priceError =
        createErrorElement(toppingPriceInput);


    // --- 金額バリデーション ---
    function validatePriceValue(val) {

        var trimmed =
            val.trim();


        if (trimmed === "") {

            return "";
        }


        var num =
            Number(trimmed);


        if (num === 0
                || trimmed.match(/^0+$/)) {

            return "※1以上の正しい金額を入力してください";
        }


        if (trimmed.indexOf("-") !== -1
                || isNaN(num)
                || num < 0) {

            return "※半角数字で入力してください";
        }


        if (trimmed.length > 5) {

            return "※5桁以内で入力してください";
        }


        return "";
    }


    // --- リアルタイムバリデーション ---

    toppingNameInput.addEventListener(
        "input",
        function () {

            if (!toppingNameInput.value.trim()) {

                nameError.textContent =
                    "※入力してください";

            } else if (
                toppingNameInput.value.length > 18
            ) {

                nameError.textContent =
                    "※18文字以内で入力してください";

            } else {

                nameError.textContent =
                    "";
            }
        }
    );


    toppingPriceInput.addEventListener(
        "input",
        function () {

            priceError.textContent =
                validatePriceValue(
                    toppingPriceInput.value
                );
        }
    );


    // --- 「作成/変更」ボタン押下時の最終チェック ---

    openModalBtn.addEventListener(
        "click",
        function () {

            var hasError =
                false;


            // トッピング名チェック
            if (!toppingNameInput.value.trim()) {

                nameError.textContent =
                    "※入力してください";

                hasError =
                    true;

            } else if (
                toppingNameInput.value.length > 18
            ) {

                nameError.textContent =
                    "※18文字以内で入力してください";

                hasError =
                    true;
            }


            // 金額チェック
            var priceErr =
                validatePriceValue(
                    toppingPriceInput.value
                );


            if (!toppingPriceInput.value.trim()) {

                priceError.textContent =
                    "※入力してください";

                hasError =
                    true;

            } else if (priceErr !== "") {

                priceError.textContent =
                    priceErr;

                hasError =
                    true;
            }


            // エラーがあればモーダルを表示しない
            if (hasError) {

                return;
            }


            // モーダルに入力内容を反映

            modalToppingName.textContent =
                toppingNameInput.value;


            modalToppingPrice.textContent =
                Number(
                    toppingPriceInput.value
                ) + " 円";


            modalToppingStock.textContent =
                Number(
                    toppingStockInput.value
                ) + " 個";


            modalOverlay.style.display =
                "flex";


            modalOverlay.classList.remove(
                "closing"
            );
        }
    );


    // --- モーダルを閉じる（アニメーション付き） ---

    function closeModalWithAnimation(
        afterClose
    ) {

        modalOverlay.classList.add(
            "closing"
        );


        modalOverlay.addEventListener(
            "animationend",
            function handler() {

                modalOverlay.removeEventListener(
                    "animationend",
                    handler
                );


                modalOverlay.style.display =
                    "none";


                modalOverlay.classList.remove(
                    "closing"
                );


                if (
                    typeof afterClose ===
                    "function"
                ) {

                    afterClose();
                }
            }
        );
    }


    // 「いいえ」
    modalNoBtn.addEventListener(
        "click",
        function () {

            closeModalWithAnimation();
        }
    );


    // 「変更する / 作成する」
    modalYesBtn.addEventListener(
        "click",
        function () {

            closeModalWithAnimation(
                function () {

                    form.submit();
                }
            );
        }
    );

});