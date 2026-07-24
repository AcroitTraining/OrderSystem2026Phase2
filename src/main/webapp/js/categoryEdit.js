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
   LIGHTNING FLASH
========================================= */

const lightningFlash =
    document.getElementById(
        "lightningFlash"
    );


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
   FINAL BATTLE
========================================= */

modalYesBtn.addEventListener("click", () => {
    startFinalBattle();
});

function startFinalBattle() {
    document.getElementById("modalOverlay").style.display = "none";
    battleOverlay.classList.add("active");

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

    const interval = setInterval(() => {
        progress += 1;
        battleProgressBar.style.width = progress + "%";

        if (progress % 15 === 0 && messageIndex < messages.length) {
            battleMessage.textContent = messages[messageIndex];
            messageIndex++;
        }

        if (progress >= 100) {
            clearInterval(interval);

            battleMessage.textContent = "SYSTEM UPDATE COMPLETE";

            setTimeout(() => {
                document.querySelector(".battle-title").textContent = "SUCCESS";
                document.querySelector(".battle-subtitle").textContent = "CATEGORY CORE UPDATE COMPLETE";
                battleOverlay.classList.add("success");

                setTimeout(() => {
                    form.submit();
                }, 1800);

            }, 700);
        }

    }, 30);
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

/* =========================================
   OUTER ELECTRIC STORM
========================================= */

function createLightningStorm(side) {

    const svg =
        document.createElementNS(
            "http://www.w3.org/2000/svg",
            "svg"
        );

    svg.classList.add(
        "lightning-storm"
    );

    svg.setAttribute(
        "viewBox",
        "0 0 400 1000"
    );


    /* =========================================
       巨大雷判定
    ========================================= */

    const isGiant =
        Math.random() < 0.08;


    /* =========================================
       雷の長さ
    ========================================= */

    const segments =
        isGiant
            ? 24 + Math.floor(
                Math.random() * 10
            )
            : 14 + Math.floor(
                Math.random() * 10
            );


    /* =========================================
       雷の座標生成
    ========================================= */

    const mainPoints = [];


    const startX =
        side === "left"
            ? 0
            : 400;


    let x =
        startX;


    let y =
        -100;


    mainPoints.push(
        `${x},${y}`
    );


    for (
        let i = 0;
        i < segments;
        i++
    ) {

        y +=
            55 +
            Math.random() * 80;


        const direction =
            side === "left"
                ? 1
                : -1;


        x +=
            direction *
            (
                10 +
                Math.random() * 55
            );


        x +=
            (
                Math.random() - 0.5
            ) * 35;


        mainPoints.push(
            `${x},${y}`
        );
    }


    /* =========================================
       メイン雷
    ========================================= */

    const main =
        document.createElementNS(
            "http://www.w3.org/2000/svg",
            "polyline"
        );


    main.setAttribute(
        "points",
        mainPoints.join(" ")
    );


    /* =========================================
       巨大雷 / 通常雷
    ========================================= */

    if (
        isGiant
    ) {

        main.classList.add(
            "lightning-main",
            "lightning-cyan",
            "lightning-giant"
        );

    } else {

        main.classList.add(
            "lightning-main"
        );


        /* =========================================
           紫 / 水色をランダム決定
        ========================================= */

        if (
            Math.random() < 0.5
        ) {

            main.classList.add(
                "lightning-cyan"
            );
        }
    }


    /* =========================================
       メイン雷をSVGへ追加
    ========================================= */

    svg.appendChild(
        main
    );


    /* =========================================
       枝分かれ
    ========================================= */

    for (
        let i = 2;
        i < mainPoints.length - 2;
        i += 2
    ) {

        if (
            Math.random() > 0.75
        ) {

            continue;
        }


        const [
            branchX,
            branchY
        ] =
            mainPoints[i]
                .split(",")
                .map(Number);


        const branchPoints = [

            `${branchX},${branchY}`,


            `${branchX +
                (
                    side === "left"
                        ? 30
                        : -30
                ) +
                (
                    Math.random() - 0.5
                ) * 80
            },${branchY + 40}`,


            `${branchX +
                (
                    side === "left"
                        ? 80
                        : -80
                ) +
                (
                    Math.random() - 0.5
                ) * 100
            },${branchY + 90}`,


            `${branchX +
                (
                    side === "left"
                        ? 120
                        : -120
                ) +
                (
                    Math.random() - 0.5
                ) * 120
            },${branchY + 140}`

        ];


        const branch =
            document.createElementNS(
                "http://www.w3.org/2000/svg",
                "polyline"
            );


        branch.setAttribute(
            "points",
            branchPoints.join(" ")
        );


        branch.classList.add(
            "lightning-branch"
        );


        svg.appendChild(
            branch
        );


        /* =========================================
           枝のさらに枝
        ========================================= */

        if (
            Math.random() > 0.45
        ) {

            const subBranch =
                document.createElementNS(
                    "http://www.w3.org/2000/svg",
                    "polyline"
                );


            const subX =
                branchX +
                (
                    side === "left"
                        ? 80
                        : -80
                );


            subBranch.setAttribute(
                "points",


                `${subX},${branchY + 90} ` +


                `${subX +
                    (
                        side === "left"
                            ? 40
                            : -40
                    )
                },${branchY + 130} ` +


                `${subX +
                    (
                        side === "left"
                            ? 80
                            : -80
                    )
                },${branchY + 190}`

            );


            subBranch.classList.add(
                "lightning-subbranch"
            );


            svg.appendChild(
                subBranch
            );
        }
    }


    /* =========================================
       雷のサイズ・角度
    ========================================= */

    const scale =
        isGiant
            ? 1.2 +
              Math.random() * 0.8
            : 0.7 +
              Math.random() * 1.3;


    const rotate =
        side === "left"
            ? -18 +
              Math.random() * 36
            : -36 +
              Math.random() * 36;


    svg.style.transform =
        `scale(${scale}) rotate(${rotate}deg)`;


    /* =========================================
       左右端に配置
    ========================================= */

    svg.style.left =
        side === "left"
            ? Math.random() * 100 - 50 + "px"
            : window.innerWidth -
              350 +
              Math.random() * 100 +
              "px";


    svg.style.top =
        Math.random() * -300 +
        "px";


    /* =========================================
       画面へ追加
    ========================================= */

    document.body.appendChild(
        svg
    );


    /* =========================================
       LIGHTNING FLASH
    ========================================= */

    if (
        Math.random() < 0.075 &&
        lightningFlash
    ) {

        lightningFlash.classList.remove(
            "active"
        );


        void lightningFlash.offsetWidth;


        lightningFlash.classList.add(
            "active"
        );
    }


    /* =========================================
       雷を削除
    ========================================= */

    setTimeout(
        () => {

            svg.remove();

        },
        1800
    );
}


/* =========================================
   雷を大量発生
========================================= */

setInterval(
    () => {

        createLightningStorm(
            Math.random() < 0.5
                ? "left"
                : "right"
        );

    },
    160
);