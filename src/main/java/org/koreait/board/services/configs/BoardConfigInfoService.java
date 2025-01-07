package org.koreait.board.services.configs;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.StringExpression;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.koreait.admin.board.controllers.BoardConfigSearch;
import org.koreait.admin.board.controllers.RequestBoard;
import org.koreait.board.entities.Board;
import org.koreait.board.entities.QBoard;
import org.koreait.board.exceptions.BoardNotFoundException;
import org.koreait.board.repositories.BoardRepository;
import org.koreait.global.paging.ListData;
import org.koreait.global.paging.Pagination;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;

import static org.springframework.data.domain.Sort.Order.desc;

@Lazy
@Service
@RequiredArgsConstructor
public class BoardConfigInfoService {

    private final BoardRepository boardRepository;
    private final HttpServletRequest request;
    private final ModelMapper modelMapper;

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

    public RequestBoard getForm(String bid) {
        Board item = get(bid);

        RequestBoard form = modelMapper.map(item, RequestBoard.class);
        form.setMode("edit");

        return form;
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

        /* 검색 처리 S */
        String sopt = search.getSopt();
        String skey = search.getSkey();
        sopt = StringUtils.hasText(sopt) ? sopt : "ALL";
        if (StringUtils.hasText(skey)) {
            StringExpression condition;
            if (sopt.equals("BID")) { // 게시판 아이디
                condition = board.bid;
            } else if (sopt.equals("NAME")) { // 게시판명
                condition = board.name;
            } else { // 통합 검색 - 게시판 아이디 + 게시판명
                condition = board.bid.concat(board.name);
            }

            andBuilder.and(condition.contains(skey.trim()));
        }

        List<String> bids = search.getBid();
        if (bids != null && !bids.isEmpty()) {
            andBuilder.and(board.bid.in(bids));
        }
        /* 검색 처리 E */

        Pageable pageable = PageRequest.of(page - 1, limit, Sort.by(desc("createdAt")));
        Page<Board> data = boardRepository.findAll(andBuilder, pageable);

        List<Board> items = data.getContent();
        items.forEach(this::addInfo);

        Pagination pagination = new Pagination(page, (int)data.getTotalElements(), 10, limit, request);

        return new ListData<>(items, pagination);
    }

    // 추가 정보처리
    private void addInfo(Board item) {
        String category = item.getCategory();
        if (StringUtils.hasText(category)) {
            List<String> categories = Arrays.stream(category.split("\\n"))
                    .map(s -> s.replaceAll("\\r", ""))
                    .filter(s -> !s.isBlank())
                    .map(String::trim)
                    .toList();

            item.setCategories(categories);
        }
    }
}
