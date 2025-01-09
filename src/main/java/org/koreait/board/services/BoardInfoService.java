package org.koreait.board.services;

import lombok.RequiredArgsConstructor;
import org.koreait.board.controllers.BoardSearch;
import org.koreait.board.entities.BoardData;
import org.koreait.global.paging.ListData;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;

@Lazy
@Service
@RequiredArgsConstructor
public class BoardInfoService {

    /**
     * 게시글 한개 조회
     *
     * @param seq
     * @return
     */
    public BoardData get(Long seq) {

        return null;
    }

    /**
     * 게시글 목록
     *
     * @param search
     * @return
     */
    public ListData<BoardData> getList(BoardSearch search) {

        return null;
    }

    public ListData<BoardData> getList(String bid, BoardSearch search) {
        search.setBid(List.of(bid));

        return getList(search);
    }

    /**
     * 추가 정보 처리
     *
     * @param item
     */
    private void addInfo(BoardData item) {

    }
}
