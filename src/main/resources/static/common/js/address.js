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
    },
    /**
    * 주소 검색 팝업
    *
    */
    search() {
        new daum.Postcode({
            oncomplete: function(data) {
               /**
               * 주소 검색 후 데이터를 활용하는 방법은 다양!
               * 처리에 대한 위임, 특정 함수를 정하고 그 함수가 정의되어 있으면 그 함수를 데이터와 함께 호출 - 콜백 방식
               */
               if (typeof callbackAddressSearch === 'function') {
                    const { zonecode, roadAddress } = data;
                    callbackAddressSearch({zipCode: zonecode, address: roadAddress});
               }
            }
        }).open();
    }
};

// DOM 구성이 완료 되면 동적 로드
window.addEventListener("DOMContentLoaded", function() {
    const { address } = commonLib;
    address.init();

    /* 주소 찾기 버튼 클릭 처리 S */
    const els = document.getElementsByClassName("search-address");
    for (const el of els) {
        el.addEventListener("click", address.search);
    }
    /* 주소 찾기 버튼 클릭 처리 E */

});