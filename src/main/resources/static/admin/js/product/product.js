window.addEventListener("DOMContentLoaded", function() {
    commonLib.loadEditor("description", 350)
        .then(editor => window.editor = editor);
});

/**
* 파일 업로드 후 후속 처리
*
*/
function callbackFileUpload(files) {
    if (!files || files.length === 0) return;

    const tplFile = document.getElementById("tpl-file-item").innerHTML;
    const tplImage = document.getElementById("tpl-image-item").innerHTML;

    const targetEditor = document.getElementById("editor-images");
    const targetMain = document.getElementById("main-images");
    const targetList = document.getElementById("list-images");

    const { insertEditorImage, fileManager } = commonLib;
    const imageUrls = [];

    const domParser = new DOMParser();

    for (const {seq, location, fileName, fileUrl, thumbUrl } of files) {
        let html = location === 'editor' ? tplFile : tplImage;
        html = html.replace(/\[seq\]/g, seq)
                    .replace(/\[fileName\]/g, fileName)
                    .replace(/\[fileUrl\]/g, fileUrl)
                    .replace(/\[thumbUrl\]/g, `${thumbUrl}&width=200&height=100`);

        const dom = domParser.parseFromString(html, 'text/html');
        const el = dom.querySelector(".file-item, .image-item");

        const insertEditor = el.querySelector(".insert-editor");
        if (insertEditor) {
            insertEditor.addEventListener("click", () => insertEditorImage(fileUrl));
        }

        const removeEl = el.querySelector(".remove");
        removeEl.addEventListener("click", () => {
            fileManager.delete(seq, () => {
                // 삭제 후속 처리
                const el = document.getElementById(`file-${seq}`);
                el.parentElement.removeChild(el);
            });
        });


        switch (location) {
            case "main":  // 메인 이미지
                targetMain.append(el);
                break;
            case "list": // 목록 이미지
                targetList.append(el);
                break;
            default: // 에디터 이미지
                imageUrls.push(fileUrl);
                targetEditor.append(el);
        }
    }

    if (imageUrls.length > 0) insertEditorImage(imageUrls);
}