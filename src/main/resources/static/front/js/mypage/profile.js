
/**
* 파일 업로드 후속 처리
*
*/
function callbackFileUpload(files) {
    if (!files || files.length === 0) {
        return;
    }

    const el = document.querySelector(".profile-image");
    if (el) {
        const file = files[0];
        el.innerHTML = `<img src='${file.fileUrl}'>`;
    }
}