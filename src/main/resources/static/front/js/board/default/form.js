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

    for (const {seq, location, fileName, fileUrl} of files) {

        if (location === 'editor') {
           imageUrls.push(fileUrl);

        } else {

        }
    } // endfor

    if (imageUrls.length > 0) { // 에디터에 추가할 이미지
        const { insertEditorImage } = commonLib;
        insertEditorImage(imageUrls);
    }
}