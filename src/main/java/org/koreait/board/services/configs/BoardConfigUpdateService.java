package org.koreait.board.services.configs;

import lombok.RequiredArgsConstructor;
import org.koreait.admin.board.controllers.RequestBoard;
import org.koreait.board.entities.Board;
import org.koreait.board.repositories.BoardRepository;
import org.koreait.member.constants.Authority;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Objects;

@Lazy
@Service
@RequiredArgsConstructor
public class BoardConfigUpdateService {
    private final BoardRepository boardRepository;

    public void process(RequestBoard form) {

        String bid = form.getBid();

        Board board = boardRepository.findById(bid).orElseGet(Board::new);

        board.setBid(bid);
        board.setName(form.getName());
        board.setOpen(form.isOpen());
        board.setCategory(form.getCategory());
        board.setRowsPerPage(form.getRowsPerPage() < 1 ? 20 : form.getRowsPerPage());
        board.setPageRanges(form.getPageRanges() < 1 ? 10 : form.getPageRanges());
        board.setPageRangesMobile(form.getPageRangesMobile() < 1 ? 5 : form.getPageRangesMobile());
        board.setUseEditor(form.isUseEditor());
        board.setUseEditorImage(form.isUseEditorImage());
        board.setUseAttachFile(form.isUseAttachFile());
        board.setUseComment(form.isUseComment());
        board.setSkin(StringUtils.hasText(form.getSkin()) ? form.getSkin() : "default");
        board.setListAuthority(Objects.requireNonNullElse(form.getListAuthority(), Authority.ALL));
        board.setViewAuthority(Objects.requireNonNullElse(form.getViewAuthority(), Authority.ALL));
        board.setWriteAuthority(Objects.requireNonNullElse(form.getWriteAuthority(), Authority.ALL));
        board.setCommentAuthority(Objects.requireNonNullElse(form.getCommentAuthority(), Authority.ALL));

        boardRepository.saveAndFlush(board);
    }
}
