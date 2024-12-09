var commonLib = commonLib ?? {};
commonLib.fileManager = {
    /**
    * 파일 업로드 처리
    *
    */
    upload(files, gid, location, single, imageOnly) {
        try {
            /* 유효성 검사 S */
            if (!files || files.length === 0) {
                throw new Error("파일을 선택하세요.");
            }

            if (imageOnly) { // 이미지만 업로드 하는 경우
                for (const file of files) {
                    if (file.type.indexOf("image/") === -1) { // 이미지가 아닌 파일인 경우
                        throw new Error("이미지 형식이 아닙니다.");
                    }
                }
            }

            if (!gid || !('' + gid).trim()) {
                throw new Error("잘못된 접근입니다.");
            }
            /* 유효성 검사 E */

            /* 전송 양식 만들기 S */
            const formData = new FormData();
            formData.append("gid", gid);
            formData.append("single", single);
            formData.append("imageOnly", imageOnly);
            if (location) {
                formData.append("location", location);
            }

            for (const file of files) {
                formData.append("file", file);
            }

            /* 전송 양식 만들기 E */

        } catch (err) {
            alert(err.message);
            console.error(err);
        }
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