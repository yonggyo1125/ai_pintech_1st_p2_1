package org.koreait.admin.board.validators;

import lombok.RequiredArgsConstructor;
import org.koreait.admin.board.controllers.RequestBoard;
import org.koreait.board.repositories.BoardRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Lazy
@Component
@RequiredArgsConstructor
public class BoardValidator implements Validator {

    private final BoardRepository boardRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(RequestBoard.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        if (errors.hasErrors()) {
            return;
        }

        RequestBoard form = (RequestBoard)target;
        String bid = form.getBid();
        if (boardRepository.exists(bid)) {
            errors.rejectValue("bid", "Duplicated");
        }
    }
}
