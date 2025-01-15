package org.koreait.board.services.comment;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.koreait.board.controllers.RequestComment;
import org.koreait.board.entities.BoardData;
import org.koreait.board.entities.CommentData;
import org.koreait.board.exceptions.CommentNotFoundException;
import org.koreait.board.repositories.CommentDataRepository;
import org.koreait.board.services.BoardInfoService;
import org.koreait.member.libs.MemberUtil;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Objects;

@Lazy
@Service
@RequiredArgsConstructor
public class CommentUpdateService {

    private final CommentDataRepository commentDataRepository;
    private final BoardInfoService boardInfoService;
    private final MemberUtil memberUtil;
    private final HttpServletRequest request;
    private final PasswordEncoder passwordEncoder;

    /**
     * 댓글 등록, 수정
     * @param form
     * @return
     */
    public CommentData save(RequestComment form) {
        String mode = Objects.requireNonNullElse(form.getMode(), "write");
        Long seq = form.getSeq();
        Long boardDataSeq = form.getBoardDataSeq(); // 게시글 번호


        CommentData item = null;
        if (mode.equals("edit") && seq != null && seq > 0L) { // 댓글 수정 
            item = commentDataRepository.findById(seq).orElseThrow(CommentNotFoundException::new);

        } else { // 댓글 등록
            BoardData data = boardInfoService.get(boardDataSeq); // 게시글 데이터
            item = new CommentData();
            item.setMember(memberUtil.getMember());
            item.setData(data);
            item.setIpAddr(request.getRemoteAddr());
            item.setUserAgent(request.getHeader("User-Agent"));
        }

        item.setCommenter(form.getCommenter());
        item.setContent(form.getContent());

        String guestPw = form.getGuestPw();
        if (StringUtils.hasText(guestPw)) {
            item.setGuestPw(passwordEncoder.encode(guestPw));
        }
    }
}
