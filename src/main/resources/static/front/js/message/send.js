window.addEventListener("DOMContentLoaded", function() {
    const { loadEditor } = commonLib;

    loadEditor("content", 350)
        .then((editor) => {
            window.editor = editor; // 전역 변수로 등록, then 구간 외부에서도 접근 가능하게 처리
        });

    // 이미지 본문 추가 이벤트 처리
    const insertEditors = document.querySelectorAll(".insert-editor")
    insertEditors.forEach(el => {
        el.addEventListener("click", e => commonLib.insertEditorImage(e.currentTarget.dataset.url));
    });

    // 파일 삭제 버튼 이벤트 처리
    const removeEls = document.querySelectorAll(".file-item .remove");
    const { fileManager } = commonLib;
    removeEls.forEach(el => {
        el.addEventListener("click", e => {
            if (confirm('정말 삭제하겠습니까?')) {
                const seq = e.currentTarget.dataset.seq;
                fileManager.delete(seq, () => {
                    const el = document.getElementById(`file-${seq}`);
                    el.parentElement.removeChild(el);
                });
            }
        });
    });
});


/**
* 파일 업로드 완료 후 성공 후속처리
*
*/
function callbackFileUpload(files) {
    if (!files || files.length === 0) {
        return;
    }

    const imageUrls = [];

    const targetEditor = document.getElementById("editor-files");
    const targetAttach = document.getElementById("attach-files");
    const tpl = document.getElementById("tpl-file-item").innerHTML;

    const domParser = new DOMParser();

    const { fileManager } = commonLib;

    for (const {seq, fileUrl, fileName, location} of files) {
        let html = tpl;
        html = html.replace(/\[seq\]/g, seq)
                    .replace(/\[fileName\]/g, fileName)
                    .replace(/\[fileUrl\]/g, fileUrl);

        const dom = domParser.parseFromString(html, "text/html");
        const fileItem = dom.querySelector(".file-item");
        const el = fileItem.querySelector(".insert-editor");
        const removeEl = fileItem.querySelector(".remove");

        if (location === 'editor') { // 에디터에 추가될 이미지
            imageUrls.push(fileUrl);

            targetEditor.append(fileItem);
            el.addEventListener("click", function() {
                const { url } = this.dataset;
                commonLib.insertEditorImage(url);
            });

        } else { // 다운로드를 위한 첨부 파일
            el.parentElement.removeChild(el);

            targetAttach.append(fileItem);
        }

        removeEl.addEventListener("click", function() {
            if (!confirm('정말 삭제하겠습니까?')) {
                return;
            }

            fileManager.delete(seq, f => {
                const el = document.getElementById(`file-${f.seq}`);
                if (el) el.parentElement.removeChild(el);
            });
        });
    }

    if (imageUrls.length > 0) commonLib.insertEditorImage(imageUrls);
}

