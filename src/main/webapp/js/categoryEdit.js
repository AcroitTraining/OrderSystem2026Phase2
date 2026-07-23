document.addEventListener("DOMContentLoaded", function () {

    /* =========================================
       要素取得
    ========================================= */

    const form =
        document.getElementById("categoryForm");

    const categoryInput =
        document.getElementById("categoryName");

    const openModalBtn =
        document.getElementById("openModalBtn");

    const modalOverlay =
        document.getElementById("modalOverlay");

    const modalNoBtn =
        document.getElementById("modalNoBtn");

    const modalYesBtn =
        document.getElementById("modalYesBtn");

    const modalCategoryName =
        document.getElementById("modalCategoryName");

    const systemStatus =
        document.querySelector(".system-status");

    const coreOrb =
        document.querySelector(".core-orb");

    const battleOverlay =
        document.getElementById("battleOverlay");

    const battleProgressBar =
        document.getElementById("battleProgressBar");

    const battleMessage =
        document.getElementById("battleMessage");


    /* =========================================
       要素チェック
    ========================================= */

    if (
        !form ||
        !categoryInput ||
        !openModalBtn ||
        !modalOverlay ||
        !modalNoBtn ||
        !modalYesBtn
    ) {

        console.error(
            "categoryEdit.js: 必要な要素が見つかりません。"
        );

        return;
    }


    /* =========================================
       エラー表示
    ========================================= */

    function createErrorElement(inputElement) {

        let errorSpan =
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


    const nameError =
        createErrorElement(categoryInput);


    /* =========================================
       バリデーション
    ========================================= */

    function validateCategoryName() {

        if (!categoryInput.value.trim()) {

            nameError.textContent =
                "※入力してください";

            return false;
        }

        if (categoryInput.value.length > 18) {

            nameError.textContent =
                "※18文字以内で入力してください";

            return false;
        }

        nameError.textContent = "";

        return true;
    }


    categoryInput.addEventListener(
        "input",
        validateCategoryName
    );


    /* =========================================
       カテゴリ入力中
    ========================================= */

    categoryInput.addEventListener(
        "focus",
        function () {

            if (systemStatus) {

                systemStatus.innerHTML =
                    '<span class="status-dot"></span>' +
                    'EDITING CATEGORY CORE';
            }
        }
    );


    categoryInput.addEventListener(
        "blur",
        function () {

            if (systemStatus) {

                systemStatus.innerHTML =
                    '<span class="status-dot"></span>' +
                    'SYSTEM ONLINE';
            }
        }
    );


    categoryInput.addEventListener(
        "input",
        function () {

            if (coreOrb) {

                coreOrb.classList.add(
                    "core-editing"
                );
            }
        }
    );


    /* =========================================
       CREATE / UPDATE
       モーダル表示
    ========================================= */

    openModalBtn.addEventListener(
        "click",
        function () {

            // バリデーション
            if (!validateCategoryName()) {

                return;
            }


            // モーダルに入力値を表示
            modalCategoryName.textContent =
                categoryInput.value;


            // モーダル表示
            modalOverlay.style.display =
                "flex";

            modalOverlay.classList.remove(
                "closing"
            );
        }
    );


    /* =========================================
       モーダル閉じる
    ========================================= */

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


    /* =========================================
       CANCEL
    ========================================= */

    modalNoBtn.addEventListener(
        "click",
        function () {

            closeModalWithAnimation();
        }
    );


    /* =========================================
       APPLY UPDATE
    ========================================= */

    modalYesBtn.addEventListener(
        "click",
        function () {

            startFinalBattle();
        }
    );


    /* =========================================
       FINAL BATTLE
    ========================================= */

    function startFinalBattle() {


        // モーダルを閉じる
        modalOverlay.style.display =
            "none";


        // 最終決戦画面を表示
        battleOverlay.classList.add(
            "active"
        );


        const messages = [

            "INITIALIZING SYSTEM...",

            "VALIDATING CATEGORY DATA...",

            "LOCKING DATABASE CONNECTION...",

            "SYNCHRONIZING CATEGORY CORE...",

            "UPDATING SYSTEM DATA...",

            "FINALIZING OPERATION...",

            "CATEGORY CORE UPDATE COMPLETE"
        ];


        let messageIndex = 0;

        let progress = 0;


        const interval =
            setInterval(
                function () {


                    progress += 1;


                    battleProgressBar.style.width =
                        progress + "%";


                    if (

                        progress % 15 === 0 &&

                        messageIndex <
                        messages.length

                    ) {

                        battleMessage.textContent =
                            messages[
                                messageIndex
                            ];

                        messageIndex++;
                    }


                    if (progress >= 100) {


                        clearInterval(
                            interval
                        );


                        battleMessage.textContent =
                            "SYSTEM UPDATE COMPLETE";


                        setTimeout(
                            function () {

                                form.submit();

                            },
                            700
                        );
                    }

                },
                30
            );
    }

});

/* =========================================
   カーソル追従キラキラエフェクト
========================================= */

const sparkleTypes = ["✦", "✧", "✦", "✧", "·"];

document.addEventListener("mousemove", function (event) {

    for (let i = 0; i < 5; i++) {

        const sparkle = document.createElement("span");

        sparkle.className = "cursor-sparkle";

        sparkle.textContent =
            sparkleTypes[
                Math.floor(Math.random() * sparkleTypes.length)
            ];

        // カーソル周辺にランダム配置
        const offsetX = (Math.random() - 0.5) * 35;
        const offsetY = (Math.random() - 0.5) * 35;

        sparkle.style.left =
            event.clientX + offsetX + "px";

        sparkle.style.top =
            event.clientY + offsetY + "px";

        // 大きさもランダム
        sparkle.style.fontSize =
            (8 + Math.random() * 10) + "px";

        document.body.appendChild(sparkle);

        setTimeout(() => {

            sparkle.remove();

        }, 700);

    }

});

/* =========================================
   クリック時：星の爆発エフェクト
========================================= */

document.addEventListener("click", function (event) {

    const clickSparkles = [
        "✦", "✧", "✦", "✧",
        "✦", "✧", "✦", "✧",
        "✦", "✧", "✦", "✧"
    ];

    clickSparkles.forEach((symbol, index) => {

        const sparkle = document.createElement("span");

        sparkle.className = "click-sparkle";

        sparkle.textContent = symbol;

        sparkle.style.left =
            event.clientX + "px";

        sparkle.style.top =
            event.clientY + "px";

        // 360度に均等に飛ばす
        const angle =
            (index / clickSparkles.length) * Math.PI * 2;

        const distance =
            70 + Math.random() * 60;

        sparkle.style.setProperty(
            "--move-x",
            Math.cos(angle) * distance + "px"
        );

        sparkle.style.setProperty(
            "--move-y",
            Math.sin(angle) * distance + "px"
        );

        document.body.appendChild(sparkle);

        setTimeout(() => {

            sparkle.remove();

        }, 900);

    });

});

/* =========================================
   SYSTEM SHUTDOWN
========================================= */

const returnBtn =
    document.getElementById("returnBtn");

const shutdownOverlay =
    document.getElementById("shutdownOverlay");

const shutdownProgressBar =
    document.getElementById("shutdownProgressBar");

const shutdownMessage =
    document.getElementById("shutdownMessage");


returnBtn.addEventListener("click", function (event) {

    event.preventDefault();

    shutdownOverlay.classList.add("active");

    const messages = [

        "DISCONNECTING DATABASE...",
        "CLOSING CATEGORY CORE...",
        "SAVING SYSTEM STATE...",
        "TERMINATING CONTROL INTERFACE...",
        "SYSTEM SHUTDOWN COMPLETE"

    ];

    let progress = 0;
    let messageIndex = 0;

    const interval = setInterval(() => {

        progress += 2;

        shutdownProgressBar.style.width =
            progress + "%";

        if (
            progress % 20 === 0 &&
            messageIndex < messages.length
        ) {

            shutdownMessage.textContent =
                messages[messageIndex];

            messageIndex++;

        }

        if (progress >= 100) {

            clearInterval(interval);

            setTimeout(() => {

                window.location.href =
                    returnBtn.href;

            }, 700);

        }

    }, 30);

});