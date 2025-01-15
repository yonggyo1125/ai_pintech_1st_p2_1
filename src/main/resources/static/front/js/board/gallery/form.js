window.addEventListener("DOMContentLoaded", function() {
    const { loadEditor } = commonLib;

    loadEditor("content", 350)
        .then(editor => window.editor = editor);

        // 이미지 본문 추가 이벤트 처리
        const insertEditors = document.querySelectorAll(".insert-editor")
        insertEditors.forEach(el => {
            el.addEventListener("click", e => commonLib.insertEditorImage(e.currentTarget.dataset.url));
        });

        // 목록 노출 이미지 선택 처리
        const selectImages = document.querySelectorAll(".select-image");
        selectImages.forEach(el => {
            el.addEventListener("click", e => commonLib.selectImage(e.currentTarget.dataset.seq));
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
* 파일 업로드 후 후속 처리
*
*/
function callbackFileUpload(files) {
    if (!files || files.length === 0) {
        return;
    }

    const imageUrls = [];
    const tpl = document.getElementById("tpl-file-item").innerHTML;
    const tpl2 = document.getElementById("tpl-image-item").innerHTML;

    const targetEditor = document.getElementById("editor-files");
    const targetAttach = document.getElementById("attach-files");

    const domParser = new DOMParser();
    const { insertEditorImage } = commonLib;

    for (const {seq, location, fileName, fileUrl, selected, thumbUrl} of files) {

        const addClass = selected ? " on":"";

        let html = location === 'editor' ? tpl2 : tpl;
        html = html.replace(/\[seq\]/g, seq)
                   .replace(/\[fileName\]/g, fileName)
                    .replace(/\[fileUrl\]/g, fileUrl)
                    .replace(/\[thumbUrl\]/g, `${thumbUrl}&width=100&height=50`)
                    .replace(/\[addClass\]/g, addClass);

        const dom = domParser.parseFromString(html, "text/html");
        const el = dom.querySelector(location === 'editor' ? ".image-item" : ".file-item");
        const insertEditor = el.querySelector(".insert-editor");
        const selectImageEl = el.querySelector(".select-image");
        const removeEl = el.querySelector(".remove");
        removeEl.addEventListener("click", () => {
            if (!confirm('정말 삭제하겠습니까?')) {
                return;
            }

            const { fileManager } = commonLib;
            fileManager.delete(seq, () => {
                el.parentElement.removeChild(el);
            });
        });


        if (location === 'editor') {
           imageUrls.push(fileUrl);

            insertEditor.addEventListener("click", () => insertEditorImage(fileUrl));
            if (selectImageEl) { // 리스트 노출 메인 이미지 선택 처리
                selectImageEl.addEventListener("click", () => commonLib.selectImage(seq));
            }

            targetEditor.append(el);
        } else {
            // 파일 첨부에서는 에디터에 추가하는것이 아니므로 제거
            insertEditor.parentElement.removeChild(insertEditor);

            targetAttach.append(el);
        }
    } // endfor

    if (imageUrls.length > 0) { // 에디터에 추가할 이미지
        insertEditorImage(imageUrls);
    }
}
