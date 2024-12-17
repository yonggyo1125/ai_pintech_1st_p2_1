var commonLib = commonLib : {};

/**
* 이메일 인증 코드 관련
*
*/
commonLib.emailAuth = {
    /**
    * 인증 코드 전송
    *
    */
    sendCode(email) {
        const { ajaxLoad } = commonLib;
        ajaxLoad(`/api/email/auth/${email}`)
    },
    /**
    * 인증 코드 검증
    *
    */
    verify(authCode) {

    },
};