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

}