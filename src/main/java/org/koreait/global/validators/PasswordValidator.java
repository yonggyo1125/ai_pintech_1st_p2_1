package org.koreait.global.validators;

public interface PasswordValidator {
    /**
     * 알파벳 복잡성 체크
     *  - 1) 대소문자 각각 1개 이상 있는 경우
     *  - 2) 대소문자 구분없이 알파벳 1자 이상
     * @param  caseInsensitive : true 2), false - 1)
     * @return
     */
    default boolean alphaCheck(String password, boolean caseInsensitive) {
        if (caseInsensitive) { // 대소문자 구분없이 알파벳 1자 이상
            // .* : 0이상 아무 문자  [a-zA-Z]+ : 알파벳 대소문자 상관없이 1자 이상
            return password.matches(".*[a-zA-Z]+.*");
        }

        // 대문자 1개 이상, 소문자 1개 이상
        return password.matches(".*[a-z]+.*") && password.matches(".*[A-Z]+.*");
    }

    /**
     * 숫자 복잡성 체크
     *
     * [0-9]  -> \d
     *
     * @param password
     * @return
     */
    default boolean numberCheck(String password) {
        return password.matches(".*\\d.*");
    }

    /**
     * 특수문자 복잡성 체크
     * [^문자..]   문자는 제외
     * [^\d] -> 숫자를 제외한 문자
     *
     * @param password
     * @return
     */
    default boolean specialCharsCheck(String password) {
        String pattern = ".*[^0-9a-zA-Zㄱ-ㅎ가-힣].*";  // 숫자, 알파벳, 한글을 제외한 모든 문자(특수문자)

        return password.matches(pattern);
    }
}
