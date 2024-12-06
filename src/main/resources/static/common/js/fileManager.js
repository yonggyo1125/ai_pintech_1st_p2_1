
window.addEventListener("DOMContentLoaded", function() {
    const fileUploads = document.getElementsByClassName("file-upload");
    const fileEl = document.createElement("input");
    fileEl.type = 'file';

    for (const el of fileUploads) {
        el.addEventListener("click", function() {
            const {gid, location, single, imageOnly} = this.dataset;

            fileEl.gid = gid;
            fileEl.location = location;
            fileEl.imageOnly = imageOnly === 'true';
            fileEl.single = single === 'true';
            fileEl.multiple = !fileEl.single;  // false - 단일 파일 선택, true - 여러파일 선택 가능

            fileEl.click();
        });
    }

    // 파일 선택시 - change 이벤트 발생
    fileEl.addEventListener("change", function(e) {
        // e.currentTarget, e.target
        console.dir(e.currentTarget.files);
    });
});