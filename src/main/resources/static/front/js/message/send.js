window.addEventListener("DOMContentLoaded", function() {
    const { loadEditor } = commonLib;

    loadEditor("content", 450)
        .then((editor) => {
            window.editor = editor; // 전역 변수로 등록, then 구간 외부에서도 접근 가능하게 처리
        });
});