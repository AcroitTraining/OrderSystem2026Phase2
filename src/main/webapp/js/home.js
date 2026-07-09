document.addEventListener('DOMContentLoaded', () => {
    const logoutTrigger = document.getElementById('logoutTrigger');
    const logoutModal = document.getElementById('logoutModal');
    const modalCancel = document.getElementById('modalCancel');
    const modalConfirm = document.getElementById('modalConfirm');
    const logoutForm = document.getElementById('logoutForm');

    // 1. ログアウトボタンを押したらポップアップを表示（スクロール禁止）
    logoutTrigger.addEventListener('click', () => {
        logoutModal.classList.add('is-active');
        document.body.style.overflow = 'hidden';
    });

    // 2. 「いいえ」を押したらポップアップを閉じる（スクロール解除）
    modalCancel.addEventListener('click', () => {
        logoutModal.classList.remove('is-active');
        document.body.style.overflow = 'auto';
    });

    // 3. 「はい」を押したらフォームを送信してログアウト
    modalConfirm.addEventListener('click', () => {
        logoutForm.submit();
    });

    // 背景の暗い部分をクリックしても閉じる（スクロール解除）
    logoutModal.addEventListener('click', (e) => {
        if (e.target === logoutModal) {
            logoutModal.classList.remove('is-active');
            document.body.style.overflow = 'auto';
        }
    });
});