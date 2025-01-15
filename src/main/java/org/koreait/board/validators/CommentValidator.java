package org.koreait.board.validators;

import lombok.RequiredArgsConstructor;
import org.koreait.board.controllers.RequestComment;
import org.koreait.board.entities.CommentData;
import org.koreait.board.repositories.CommentDataRepository;
import org.koreait.global.validators.PasswordValidator;
import org.koreait.member.libs.MemberUtil;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Lazy
@Component
@RequiredArgsConstructor
public class CommentValidator implements Validator, PasswordValidator {

    private final CommentDataRepository commentDataRepository;
    private final PasswordEncoder passwordEncoder;
    private final MemberUtil memberUtil;

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(RequestComment.class);
    }

    @Override
    public void validate(Object target, Errors errors) {

        if (errors.hasErrors()) {
            return;
        }

        RequestComment form = (RequestComment) target;

        /**
         * 1. 수정모드인 경우 seq 필수
         * 2. 비회원인 경우 guestPw 필수, 비밀번호 복잡성 - 대소문자 구분없는 알파벳 + 숫자
         */

        String mode = form.getMode();
        Long seq = form.getSeq();
        String guestPw = form.getGuestPw();

        // 1. 수정모드인 경우 seq 필수
        if (mode.equals("edit") && (seq == null || seq < 1L)) {
            errors.rejectValue("seq", "NotNull");
        }

        // 2. 비회원인 경우
        if (!memberUtil.isLogin()) {
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "guestPw", "NotBlank");

            if (StringUtils.hasText(guestPw) && (!alphaCheck(guestPw, true) || !numberCheck(guestPw))) {
                errors.rejectValue("guestPw", "Complexity");
            }
        }
    }

    /**
     * 비회원 비밀번호 체크
     *
     * @param password
     * @param seq
     */
    public boolean checkGuestPassword(String password, Long seq) {
        if (seq == null) return false;

        CommentData item = commentDataRepository.findById(seq).orElse(null);

        if (item != null && StringUtils.hasText(item.getGuestPw())) {
            return passwordEncoder.matches(password, item.getGuestPw());
        }

        return false;
    }
}
