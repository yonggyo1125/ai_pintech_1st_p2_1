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

    });
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