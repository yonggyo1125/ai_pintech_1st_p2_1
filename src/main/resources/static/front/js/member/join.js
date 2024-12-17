window.addEventListener("DOMContentLoaded", function() {

    const sendButton = document.getElementById("send-auth-code");
    const resendButton = document.getElementById("resend-auth-code");
    const { emailAuth } = commonLib;

    frmJoin.email.addEventListener("change", function() {
        if (this.value.trim()) {
            sendButton.disabled = false;
        } else {
            sendButton.disabled = true;
        }
    });

    // 인증코드 전송 버튼 처리
    sendButton.addEventListener("click", function() {
        const email = frmJoin.email.value.trim();
        if (!email) {
            return;
        }

        /**
         * 코드 전송 완료 후 후속 처리
         *
         * 1. 전송 버튼은 감추고, 재전송 버튼은 노출
         * 2. 타이머에 남은 시간을 출력
         */

        emailAuth.sendCode(email, updateTimer, function() {
             // 1. 전송 버튼은 감추고, 재전송 버튼은 노출
             sendButton.classList.remove("dn");
             sendButton.classList.add("dn");

             resendButton.classList.remove("dn");
        });
    });

    /**
    * 타이머 출력 갱신
    *
    */
    function updateTimer(seconds) {
        console.log("seconds", seconds);
    }
});

/**
* 주소 검색 후 후속 처리 (회원 가입 양식)
*
*/
function callbackAddressSearch(data) {
    if (!data) {
        return;
    }

    const { zipCode, address } = data;

    frmJoin.zipCode.value = zipCode;
    frmJoin.address.value = address;
}