package org.koreait.board.services.configs;

import com.querydsl.core.BooleanBuilder;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.koreait.admin.board.controllers.BoardConfigSearch;
import org.koreait.board.entities.Board;
import org.koreait.board.entities.QBoard;
import org.koreait.board.exceptions.BoardNotFoundException;
import org.koreait.board.repositories.BoardRepository;
import org.koreait.global.paging.ListData;
import org.koreait.global.paging.Pagination;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.springframework.data.domain.Sort.Order.desc;

@Lazy
@Service
@RequiredArgsConstructor
public class BoardConfigInfoService {

    private final BoardRepository boardRepository;
    private final HttpServletRequest request;

    /**
     * 게시판 설정 하나 조회
     *
     * @param bid
     * @return
     */
    public Board get(String bid) {
        Board item = boardRepository.findById(bid).orElseThrow(BoardNotFoundException::new);

        addInfo(item); // 추가 정보 처리

        return item;
    }

    /**
     * 게시판 설정 목록
     * 
     * @param search
     * @return
     */
    public ListData<Board> getList(BoardConfigSearch search) {
        int page = Math.max(search.getPage(), 1);
        int limit = search.getLimit();
        limit = limit < 1 ? 20 : limit;

        BooleanBuilder andBuilder = new BooleanBuilder();
        QBoard board = QBoard.board;


        Pageable pageable = PageRequest.of(page - 1, limit, Sort.by(desc("createdAt")));
        Page<Board> data = boardRepository.findAll(andBuilder, pageable);

        List<Board> items = data.getContent();
        items.forEach(this::addInfo);

        Pagination pagination = new Pagination(page, (int)data.getTotalElements(), 10, limit, request);

        return new ListData<>(items, pagination);
    }

    private void addInfo(Board item) {

    }
}
