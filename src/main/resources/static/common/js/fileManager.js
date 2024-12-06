var commonLib = commonLib ?? {};
commonLib.fileManager = {
    /**
    * 파일 업로드 처리
    *
    */
    upload(files, gid, location, single, imageOnly) {

    }

};

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
        const files = e.currentTarget.files;
        const {gid, location, single, imageOnly} = fileEl;

        const { fileManager } = commonLib;
        fileManager.upload(files, gid, location, single, imageOnly);
    });
});