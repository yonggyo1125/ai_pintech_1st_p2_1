window.addEventListener("DOMContentLoaded", function() {
    const { loadEditor } = commonLib;

    loadEditor("content", 350)
        .then(editor => window.editor = editor);
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

    const targetEditor = document.getElementById("editor-files");
    const targetAttach = document.getElementById("attach-files");

    const domParser = new DOMParser();
    const { insertEditorImage } = commonLib;

    for (const {seq, location, fileName, fileUrl} of files) {

        let html = tpl;
        html = html.replace(/\[seq\]/g, seq)
                   .replace(/\[fileName\]/g, fileName)
                    .replace(/\[fileUrl\]/g, fileUrl);

        const dom = domParser.parseFromString(html, "text/html");
        const el = dom.querySelector(".file-item");
        const insertEditor = el.querySelector(".insert-editor");
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