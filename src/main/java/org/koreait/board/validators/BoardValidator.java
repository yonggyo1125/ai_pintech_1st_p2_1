package org.koreait.board.validators;

import lombok.RequiredArgsConstructor;
import org.koreait.board.controllers.RequestBoard;
import org.koreait.member.libs.MemberUtil;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Lazy
@Component
@RequiredArgsConstructor
public class BoardValidator implements Validator {

    private final MemberUtil memberUtil;

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(RequestBoard.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        if (errors.hasErrors()) {
            return;
        }

        RequestBoard form = (RequestBoard) target;
        if (!memberUtil.isLogin()) {
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "guestPw", "NotBlank");
        }
    }
}
