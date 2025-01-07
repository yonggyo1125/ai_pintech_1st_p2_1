package org.koreait.board.services.configs;

import lombok.RequiredArgsConstructor;
import org.koreait.admin.board.controllers.BoardConfigSearch;
import org.koreait.board.entities.Board;
import org.koreait.board.repositories.BoardRepository;
import org.koreait.global.paging.ListData;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Lazy
@Service
@RequiredArgsConstructor
public class BoardConfigInfoService {
    private final BoardRepository boardRepository;

    /**
     * 게시판 설정 하나 조회
     *
     * @param bid
     * @return
     */
    public Board get(String bid) {

        return null;
    }

    public ListData<Board> getList(BoardConfigSearch search) {

        return null;
    }

    private void addInfo(Board item) {

    }
}
