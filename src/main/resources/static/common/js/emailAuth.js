var commonLib = commonLib ?? {};

/**
* 이메일 인증 코드 관련
*
*/
commonLib.emailAuth = {
    timer : {
        seconds: 180, // 3분
        intervalId: null,
        // 타이머 초기화
        reset(callback) {
            this.stop();
            this.seconds = 180;

            if (typeof callback === 'function') {
                callback(this.seconds);
            }
        },
        // 타이머 중지
        stop(callback) {
            if (this.intervalId) {
                clearInterval(this.intervalId);
            }

            if (typeof callback === 'function') {
                callback(this.seconds);
            }
        },
        // 타이머 시작
        start(callback) {
            if (this.seconds < 1) return;
            this.stop();

            this.intervalId = setInterval(function() {
                const seconds = --commonLib.emailAuth.timer.seconds;
                if (typeof callback === 'function') {
                    callback(seconds);
                }
            }, 1000);
        },
    },
    /**
    * 인증 코드 전송
    *
    */
    sendCode(email, timerCallback, successCallback) {
        const { ajaxLoad } = commonLib;
        const { timer } = this;
        (async() => {
            try {
                await ajaxLoad(`/api/email/auth/${email}`);
                timer.reset(timerCallback);
                timer.start(timerCallback);

                if (typeof successCallback === 'function') {
                    successCallback();
                }
            } catch (err) { // 인증코드 발급 실패
                alert(err.message);
            }
        })();

    },
    /**
    * 인증 코드 검증
    *
    */
    verify(authCode, successCallback, failureCallback) {
        const { ajaxLoad } = commonLib;
        const { timer } = this;
        (async() => {
            try {
                await ajaxLoad(`/api/email/verify?authCode=${authCode}`);
                timer.stop(successCallback);

            } catch (err) {
                if (typeof failureCallback === 'function') {
                    failureCallback(err);
                }
            }
        })();
    },
};

/**
* ajax 실패시 처리
*
*/
function callbackAjaxFailure(err) {

}