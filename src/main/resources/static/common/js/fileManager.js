
window.addEventListener("DOMContentLoaded", function() {
    const fileUploads = document.getElementsByClassName("file-upload");
    const fileEl = document.createElement("input");
    fileEl.type = 'file';

    for (const el of fileUploads) {
        el.addEventListener("click", function() {
            fileEl.click();
        });
    }
});