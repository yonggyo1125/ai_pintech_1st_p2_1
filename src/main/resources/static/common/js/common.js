var commonLib = commonLib ?? {};

/**
* 메타 태그 정보 조회
*   mode - rootUrl : <meta name="rootUrl" ... />
*/
commonLib.getMeta = function(mode) {
    if (!mode) return;

    const el = document.querySelector(`meta[name='${mode}']`);

    return el?.content;
};

/**
* Ajax 요청 처리
*
* @params url : 요청 주소
* @params method 요청방식 - GET, POST, DELETE, PATCH ...
* @params callback 응답 완료 후 후속 처리 콜백 함수
* @params data : 요청 데이터
* @params headers : 추가 요청 헤더
*/
commonLib.ajaxLoad = function(url, callback, method, data, headers) {

};

window.addEventListener("DOMContentLoaded", function() {
    // 체크박스 전체 토글 기능 S
    const checkAlls = document.getElementsByClassName("check-all");
    for (const el of checkAlls) {
        el.addEventListener("click", function() {
            const { targetClass } = this.dataset;
            if (!targetClass) { // 토클할 체크박스의 클래스가 설정되지 않은 경우는 진행 X
                return;
            }

            const chks = document.getElementsByClassName(targetClass);
            for (const chk of chks) {
                chk.checked = this.checked;
            }
        });
    }
    // 체크박스 전체 토글 기능 E
});
