
document.addEventListener("DOMContentLoaded", () => {
    // 要素の取得
    const servedForms = document.querySelectorAll(".served-form");
    const modal = document.getElementById("customModal");
    const yesBtn = document.getElementById("modalYesBtn");
    const noBtn = document.getElementById("modalNoBtn");

    let currentForm = null; // 現在クリックされたフォームを一時保存する変数

    // 全ての「提供フォーム」の送信イベントを監視
    servedForms.forEach(form => {
        form.addEventListener("submit", (event) => {
            // ① 通常のフォーム送信（ページ遷移）を一旦ストップ
            event.preventDefault();

            // ② クリックされたフォームを保存
            currentForm = form;

            // ③ モーダルを表示する (CSSの display: none を flex に上書き)
            modal.style.display = "flex";
        });
    });

    // 「はい」ボタンが押されたとき
    yesBtn.addEventListener("click", () => {
        if (currentForm) {
            // 実際のフォーム送信を実行
            currentForm.submit();
        }
    });

    // 「いいえ」ボタンが押されたとき
    noBtn.addEventListener("click", () => {
        // モーダルを非表示にする
        modal.style.display = "none";
        currentForm = null; // 保持していたフォームをクリア
    });

    // モーダルの背景（グレー部分）をクリックしたときもキャンセルにする場合
    modal.addEventListener("click", (event) => {
        if (event.target === modal) {
            modal.style.display = "none";
            currentForm = null;
        }
    });
});

document.addEventListener("DOMContentLoaded",()=>{

    const categoryTabs = document.getElementById("categoryTabs");
    const rightBtn = document.querySelector(".scroll-btn.right");
    const leftBtn = document.querySelector(".scroll-btn.left");


    rightBtn.addEventListener("click",()=>{
        categoryTabs.scrollBy({
            left:200,
            behavior:"smooth"
        });
    });


    leftBtn.addEventListener("click",()=>{
        categoryTabs.scrollBy({
            left:-200,
            behavior:"smooth"
        });
    });

});

document.addEventListener("DOMContentLoaded",()=>{

    const tabs = document.getElementById("categoryTabs");

    let isDown = false;
    let startX;
    let scrollLeft;


    tabs.addEventListener("mousedown",(e)=>{
        isDown = true;
        tabs.classList.add("dragging");

        startX = e.pageX - tabs.offsetLeft;
        scrollLeft = tabs.scrollLeft;
    });


    tabs.addEventListener("mouseleave",()=>{
        isDown = false;
        tabs.classList.remove("dragging");
    });


    tabs.addEventListener("mouseup",()=>{
        isDown = false;
        tabs.classList.remove("dragging");
    });


    tabs.addEventListener("mousemove",(e)=>{
        if(!isDown) return;

        e.preventDefault();

        const x = e.pageX - tabs.offsetLeft;
        const walk = (x - startX) * 1.5;

        tabs.scrollLeft = scrollLeft - walk;
    });

});
