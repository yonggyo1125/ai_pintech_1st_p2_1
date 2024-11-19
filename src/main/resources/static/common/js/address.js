/**
* 주소 검색
*/
var commonLib = commonLib ?? {}
commonLib.address = {
    /**
    * 카카오 주소 API 동적 로드
    *
    */
    init() {
        // 이미 동적 로드가 된 상태라면 추가 X
        if (document.getElementById("address-api")) {
            return;
        }

        const script = document.createElement("script");
        script.id = "address-api";
        script.src = "//t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js";
        document.head.prepend(script);
    }
};

// DOM 구성이 완료 되면 동적 로드
window.addEventListener("DOMContentLoaded", function() {
    const { address } = commonLib;
    address.init();

});