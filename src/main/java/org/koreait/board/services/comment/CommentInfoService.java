package org.koreait.board.services.comment;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.koreait.board.controllers.RequestComment;
import org.koreait.board.entities.BoardData;
import org.koreait.board.entities.CommentData;
import org.koreait.board.entities.QCommentData;
import org.koreait.board.exceptions.CommentNotFoundException;
import org.koreait.board.repositories.CommentDataRepository;
import org.koreait.member.entities.Member;
import org.koreait.member.libs.MemberUtil;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;

@Lazy
@Service
@RequiredArgsConstructor
public class CommentInfoService {
    private final CommentDataRepository commentDataRepository;
    private final ModelMapper modelMapper;
    private final JPAQueryFactory queryFactory;
    private final MemberUtil memberUtil;

    /**
     * 댓글 한개 조회
     *
     * @param seq
     * @return
     */
    public CommentData get(Long seq) {
        CommentData item = commentDataRepository.findById(seq).orElseThrow(CommentNotFoundException::new);

        addInfo(item); // 추가 데이터 처리

        return item;
    }

    public RequestComment getForm(Long seq) {
        CommentData item = get(seq);
        BoardData data = item.getData();
        RequestComment form = modelMapper.map(item, RequestComment.class);
        form.setMode("edit");
        form.setBoardDataSeq(data.getSeq());

        return form;
    }

    /**
     * 게시글 번호로 작성된 댓글 목록 조회
     *
     * @param seq
     * @return
     */
    public List<CommentData> getList(Long seq) {

        QCommentData commentData = QCommentData.commentData;

        List<CommentData> items = queryFactory.selectFrom(commentData)
                .leftJoin(commentData.member)
                .fetchJoin()
                .where(commentData.data.seq.eq(seq))
                .orderBy(commentData.createdAt.asc())
                .fetch();

        items.forEach(this::addInfo); // 추가 데이터 처리

        return items;
    }

    // 추가 데이터 처리
    private void addInfo(CommentData item) {
        Member member = memberUtil.getMember();
        Member commentMember = item.getMember();
        boolean editable = memberUtil.isAdmin() || item.getMember() == null || (commentMember != null && memberUtil.isLogin() && member.getEmail().equals(commentMember.getEmail()));

        item.setEditable(editable); // 댓글 수정, 삭제 가능, 다만 비회원은 비밀번호 검증 페이지로 넘어간다.
    }
}
