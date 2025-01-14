package org.koreait.board.services;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.StringExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.koreait.board.controllers.BoardSearch;
import org.koreait.board.controllers.RequestBoard;
import org.koreait.board.entities.Board;
import org.koreait.board.entities.BoardData;
import org.koreait.board.entities.QBoardData;
import org.koreait.board.exceptions.BoardDataNotFoundException;
import org.koreait.board.repositories.BoardDataRepository;
import org.koreait.board.services.configs.BoardConfigInfoService;
import org.koreait.file.services.FileInfoService;
import org.koreait.global.libs.Utils;
import org.koreait.global.paging.ListData;
import org.koreait.global.paging.Pagination;
import org.koreait.member.entities.Member;
import org.koreait.member.libs.MemberUtil;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Lazy
@Service
@RequiredArgsConstructor
public class BoardInfoService {

    private final BoardConfigInfoService configInfoService;
    private final BoardDataRepository boardDataRepository;
    private final FileInfoService fileInfoService;
    private final JPAQueryFactory queryFactory;
    private final HttpServletRequest request;
    private final MemberUtil memberUtil;
    private final ModelMapper modelMapper;
    private final Utils utils;

    /**
     * 게시글 한개 조회
     *
     * @param seq
     * @return
     */
    public BoardData get(Long seq) {

        BoardData item = boardDataRepository.findById(seq).orElseThrow(BoardDataNotFoundException::new);

        addInfo(item, true); // 추가 정보 처리

        return item;
    }

    public RequestBoard getForm(Long seq) {
        return getForm(get(seq));
    }

    /**
     * 수정 처리시 커맨드 객체 RequestBoard로 변환
     *
     * @param item
     * @return
     */
    public RequestBoard getForm(BoardData item) {
        RequestBoard form = modelMapper.map(item, RequestBoard.class);
        form.setMode("edit");
        form.setBid(item.getBoard().getBid());

        return form;
    }

    /**
     * 게시글 목록
     *
     * @param search
     * @return
     */
    public ListData<BoardData> getList(BoardSearch search) {
        int page = Math.max(search.getPage(), 1);
        Board board = null;
        int rowsPerPage = 0;
        List<String> bids = search.getBid();
        if (bids != null && !bids.isEmpty()) {
            board = configInfoService.get(bids.get(0));
            rowsPerPage = board.getRowsPerPage();
        }
        int limit = search.getLimit() > 0 ? search.getLimit() : rowsPerPage;
        int offset = (page - 1) * limit;

        /* 검색 처리 S */
        BooleanBuilder andBuilder = new BooleanBuilder();
        QBoardData boardData = QBoardData.boardData;

        // 게시판 아이디
        if (bids != null && !bids.isEmpty()) {
            andBuilder.and(boardData.board.bid.in(bids));
        }

        // 분류 검색
        List<String> categories = search.getCategory();
        if (categories != null && !categories.isEmpty()) {
            andBuilder.and(boardData.category.in(categories));
        }

        /**
         * 키워드 검색
         *  sopt
         *      - ALL - 제목 + 내용 + 작성자(작성자 + 이메일 + 회원명)
         *      - SUBJECT - 제목
         *      - CONTENT - 내용
         *      - SUBJECT_CONTENT - 제목 + 내용
         *      - POSTER - 작성자 + 이메일 + 회원명
         */
        String sopt = search.getSopt();
        String skey = search.getSkey();
        sopt = StringUtils.hasText(sopt) ? sopt : "ALL";
        if (StringUtils.hasText(skey)) {
            skey = skey.trim();

            StringExpression subject = boardData.subject;
            StringExpression content = boardData.content;
            StringExpression poster = boardData.poster.concat(boardData.member.name)
                    .concat(boardData.member.email);

            StringExpression condition = null;
            if (sopt.equals("SUBJECT")) { // 제목 검색
                condition = subject;
            } else if (sopt.equals("CONTENT")) { // 내용 검색
                condition = content;
            } else if (sopt.equals("SUBJECT_CONTENT")) { // 제목 + 내용
                condition = subject.concat(content);
            } else if (sopt.equals("POSTER")){
                condition = poster;
            } else { // 통합 검색
                condition = subject.concat(content).concat(poster);
            }

            andBuilder.and(condition.contains(skey));
        }

        // 회원 이메일
        List<String> emails = search.getEmail();
        if (emails != null && !emails.isEmpty()) {
            andBuilder.and(boardData.member.email.in(emails));
        }

        /* 검색 처리 E */

        JPAQuery<BoardData> query = queryFactory.selectFrom(boardData)
                .leftJoin(boardData.board)
                .fetchJoin()
                .leftJoin(boardData.member)
                .fetchJoin()
                .where(andBuilder)
                .offset(offset)
                .limit(limit);

        /* 정렬 조건 처리 S */
        String sort = search.getSort();
        if (StringUtils.hasText(sort)) {
            String[] _sort = sort.split("_");
            String field = _sort[0];
            String direction = _sort[1];
            if (field.equals("viewCount")) {
                query.orderBy(direction.equalsIgnoreCase("DESC") ? boardData.viewCount.desc() : boardData.viewCount.asc());
            } else if (field.equals("commentCount")) {
                query.orderBy(direction.equalsIgnoreCase("DESC") ? boardData.commentCount.desc() : boardData.commentCount.asc());
            } else {
                query.orderBy(boardData.notice.desc(), boardData.createdAt.desc());
            }

        } else { // 기본 정렬 조건 - notice DESC, createdAt DESC
            query.orderBy(boardData.notice.desc(), boardData.createdAt.desc());
        }

        /* 정렬 조건 처리 E */

        List<BoardData> items = query.fetch();

        long total = boardDataRepository.count(andBuilder);

        items.forEach(this::addInfo); // 추가 정보 처리

        int ranges = utils.isMobile() ? 5 : 10;
        if (board != null) { // 게시판별 설정이 있는 경우
            ranges = utils.isMobile() ? board.getPageRangesMobile() : board.getPageRanges();
        }

        Pagination pagination = new Pagination(page, (int)total, ranges, limit, request);

        return new ListData<>(items, pagination);
    }

    public ListData<BoardData> getList(String bid, BoardSearch search) {
        search.setBid(List.of(bid));

        return getList(search);
    }

    /**
     * 게시판별 최신 게시글
     *
     * @param bid
     * @param limit
     * @return
     */
    public List<BoardData> getLatest(String bid, int limit) {
        BoardSearch search = new BoardSearch();
        search.setLimit(limit);
        search.setBid(List.of(bid));

        ListData<BoardData> data = getList(search);

        return data.getItems();
    }

    public List<BoardData> getLatest(String bid) {
        return getLatest(bid, 5);
    }

    /**
     * 로그인한 회원이 작성한 게시글 목록
     *
     * @param search
     * @return
     */
    public ListData<BoardData> getMyList(BoardSearch search) {
        if (!memberUtil.isLogin()) {
            return new ListData<>(List.of(), null);
        }

        Member member = memberUtil.getMember();
        String email = member.getEmail();
        search.setEmail(List.of(email));

        return getList(search);
    }

    /**
     * 추가 정보 처리
     *
     * @param item
     */
    private void addInfo(BoardData item, boolean isView) {
        // 게시판 파일 정보 S
        String gid = item.getGid();
        item.setEditorImages(fileInfoService.getList(gid, "editor"));
        item.setAttachFiles(fileInfoService.getList(gid, "attach"));
        // 게시판 파일 정보 E

        // 이전, 다음 게시글
        if (isView) { // 보기 페이지 데이터를 조회하는 경우만 이전, 다음 게시글을 조회
            QBoardData boardData = QBoardData.boardData;
            Long seq = item.getSeq();

            BoardData prev = queryFactory.selectFrom(boardData)
                    .where(boardData.seq.lt(seq))
                    .orderBy(boardData.seq.desc())
                    .fetchFirst();

            BoardData next = queryFactory.selectFrom(boardData)
                            .where(boardData.seq.gt(seq))
                            .orderBy(boardData.seq.asc())
                            .fetchFirst();

            item.setPrev(prev);
            item.setNext(next);
        }

        /* listable, writable, editable, mine 처리 S */

        Board board = item.getBoard();
        configInfoService.addInfo(board);

        boolean listable = board.isListable();

        boolean writable = board.isWritable();

        Member member = item.getMember();
        Member loggedMember = memberUtil.getMember();

        boolean editable = member == null || (memberUtil.isLogin() && loggedMember.getEmail().equals(member.getEmail())); // 비회원게시글은 비밀번호 확인이 필요하므로 버튼 노출, 회원게시글 로그인한 회원과 일치하면 버튼 노출

        boolean mine = request.getSession().getAttribute("board_" + item.getSeq()) != null
                        || (member != null && memberUtil.isLogin() && loggedMember.getEmail().equals(member.getEmail()));

        item.setListable(listable);
        item.setWritable(writable);
        item.setEditable(editable);
        item.setMine(mine);

        /* listable, writable, editable, mine 처리 E */
    }

    private void addInfo(BoardData item) {
        addInfo(item, false);
    }
}
