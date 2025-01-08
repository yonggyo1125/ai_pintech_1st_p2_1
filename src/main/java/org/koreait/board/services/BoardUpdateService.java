package org.koreait.board.services;

import lombok.RequiredArgsConstructor;
import org.koreait.board.controllers.RequestBoard;
import org.koreait.board.entities.BoardData;
import org.koreait.board.exceptions.BoardDataNotFoundException;
import org.koreait.board.repositories.BoardDataRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Lazy
@Service
@RequiredArgsConstructor
public class BoardUpdateService {

    private final BoardDataRepository boardDataRepository;

    public BoardData process(RequestBoard form) {

        Long seq = Objects.requireNonNullElse(form.getSeq(), 0L);
        String mode = Objects.requireNonNullElse(form.getMode(), "write");

        BoardData item = mode.equals("edit") ? boardDataRepository.findById(seq).orElseThrow(BoardDataNotFoundException::new) : new BoardData();



        return null;
    }
}
