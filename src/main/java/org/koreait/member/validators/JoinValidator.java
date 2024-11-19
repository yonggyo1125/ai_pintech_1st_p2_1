package org.koreait.member.validators;

import org.koreait.member.controllers.RequestAgree;
import org.koreait.member.controllers.RequestJoin;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class JoinValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(RequestAgree.class) || clazz.isAssignableFrom(RequestJoin.class);
    }

    @Override
    public void validate(Object target, Errors errors) {

        if (target instanceof RequestAgree requestAgree) {
            validateAgree(requestAgree, errors);
        } else {
            validateJoin((RequestJoin)target, errors);
        }
    }

    /**
     * 약관 동의 검증
     *
     * @param form
     * @param errors
     */
    private void validateAgree(RequestAgree form, Errors errors) {

    }

    /**
     * 회원 가입 검증
     *
     * @param form
     * @param errors
     */
    private void validateJoin(RequestJoin form, Errors errors) {

    }
}

