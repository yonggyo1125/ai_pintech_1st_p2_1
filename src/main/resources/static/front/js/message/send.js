window.addEventListener("DOMContentLoaded", function() {
    const { loadEditor } = commonLib;

    loadEditor("content", 350)
        .then((editor) => {
            window.editor = editor; // 전역 변수로 등록, then 구간 외부에서도 접근 가능하게 처리
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
    for (const {seq, fileUrl, fileName, location} of files) {
        if (location === 'editor') { // 에디터에 추가될 이미지
            imageUrls.push(fileUrl);

        } else { // 다운로드를 위한 첨부 파일

        }
    }

    if (imageUrls.length > 0) insertImage(imageUrls);
}

function insertImage(imageUrls) {
    editor.execute('insertImage', { source: imageUrls });
}