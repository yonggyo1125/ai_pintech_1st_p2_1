package org.koreait.mypage.validators;

import org.koreait.global.validators.PasswordValidator;
import org.koreait.member.libs.MemberUtil;
import org.koreait.mypage.controllers.RequestProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Lazy
@Component
public class ProfileValidator implements Validator, PasswordValidator {
    @Autowired
    private MemberUtil memberUtil;

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(RequestProfile.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        RequestProfile form = (RequestProfile)target;
        String password = form.getPassword();
        String confirmPassword = form.getConfirmPassword();
        String email = form.getEmail();
        String mode = form.getMode();

        if (StringUtils.hasText(mode) && mode.equals("admin") && !StringUtils.hasText(email)) {
            errors.rejectValue("email", "NotBlank");
        }

        if (!StringUtils.hasText(password)) {
            return;
        }

        if (password.length() < 8) {
            errors.rejectValue("password", "Size");
        }

        if (!StringUtils.hasText(confirmPassword)) {
            errors.rejectValue("confirmPassword", "NotBlank");
            return;
        }

        // 비밀번호 복잡성 S
        if (!alphaCheck(password, false) || !numberCheck(password) || !specialCharsCheck(password)) {
            errors.rejectValue("password", "Complexity");
        }
        // 비밀번호 복잡성 E

        // 비밀번호, 비밀번호 확인 일치 여부 S
        if (!password.equals(confirmPassword)) {
            errors.rejectValue("confirmPassword", "Mismatch");
        }
        // 비밀번호, 비밀번호 확인 일치 여부 E

    }
}
