window.addEventListener("DOMContentLoaded", function() {

    const sendButton = document.getElementById("send-auth-code");
    const authCodeEl = document.getElementById("auth-code");
    const verifyButton = document.getElementById("verify-auth-code");

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
         * 1. 전송버튼의 문구를 인증코드 재전송으로 변경
         * 2. 타이머에 남은 시간을 출력
         */

        emailAuth.sendCode(email, updateTimer, function() {
             // 1. 전송버튼의 문구를 인증코드 재전송으로 변경,
             //    이메일을 변경하지 못하게 처리
             //    인증코드 입력 가능하게 처리
             //    인증하기 버튼 노출
             const { text } = sendButton.dataset;
             sendButton.innerText = text;

             frmJoin.email.setAttribute("readonly", true);
             authCodeEl.disabled = false;

             verifyButton.classList.remove("dn");
        });
    });

    /**
    * 타이머 출력 갱신
    *
    */
    function updateTimer(seconds) {
        let timerStr = "00:00";
        if (seconds > 0) {
            const min = Math.floor(seconds / 60);
            const sec = seconds - min * 60;
            timeStr = `${('' + min).padStart(2, '0')}:${('' + sec).padStart(2, '0')}`;
        } else { // 타이머가 0이 되면 다시 이메일 변경 가능하게 처리, 인증 코드 입력 불가 처리, 인증하기 버튼 감추기
            frmJoin.email.removeAttribute("readonly");
            authCodeEl.value = "";
            authCodeEl.disabled = true;
            verifyButton.classList.remove("dn");
            verifyButton.classList.add("dn");
        }

        const timerEl = document.querySelector(".auth-box .timer");
        if (timerEl) {
            timerEl.innerHTML = timeStr;
        }
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