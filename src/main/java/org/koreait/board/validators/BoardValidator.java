package org.koreait.board.validators;

import lombok.RequiredArgsConstructor;
import org.koreait.board.controllers.RequestBoard;
import org.koreait.board.entities.BoardData;
import org.koreait.board.entities.QCommentData;
import org.koreait.board.repositories.BoardDataRepository;
import org.koreait.board.repositories.CommentDataRepository;
import org.koreait.global.exceptions.scripts.AlertBackException;
import org.koreait.global.libs.Utils;
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
public class BoardValidator implements Validator, PasswordValidator {

    private final MemberUtil memberUtil;
    private final Utils utils;
    private final BoardDataRepository boardDataRepository;
    private final CommentDataRepository commentDataRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(RequestBoard.class);
    }

    @Override
    public void validate(Object target, Errors errors) {

        RequestBoard form = (RequestBoard) target;
        // 비회원 비밀번호 검증
        if (!memberUtil.isLogin()) {
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "guestPw", "NotBlank");

            // 대소문자 구분없은 알파벳 1자 이상, 숫자 1자 이상 포함
            String guestPw = form.getGuestPw();
            if (StringUtils.hasText(guestPw) && (!alphaCheck(guestPw, true) || !numberCheck(guestPw))) {
                errors.rejectValue("guestPw", "Complexity");
            }
        }

        // 수정일때 seq 필수 여부
        String mode = form.getMode();
        Long seq = form.getSeq();
        if (mode != null && mode.equals("edit") && (seq == null || seq < 1L)) {
            errors.rejectValue("seq", "NotNull");
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

        BoardData item = boardDataRepository.findById(seq).orElse(null);

        if (item != null && StringUtils.hasText(item.getGuestPw())) {
            return passwordEncoder.matches(password, item.getGuestPw());
        }

        return false;
    }

    /**
     * 게시글 삭제 가능 여부 체크
     *  - 댓글이 존재하면 삭제 불가
     * @param seq
     */
    public void checkDelete(Long seq) {
        QCommentData commentData = QCommentData.commentData;

        if (commentDataRepository.count(commentData.data.seq.eq(seq)) > 0L) {
            throw new AlertBackException(utils.getMessage("Exist.comment"));
        }
    }
}
