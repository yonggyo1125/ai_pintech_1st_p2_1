package org.koreait.board.services;

import lombok.RequiredArgsConstructor;
import org.koreait.board.entities.Board;
import org.koreait.board.entities.BoardData;
import org.koreait.board.services.configs.BoardConfigInfoService;
import org.koreait.global.exceptions.scripts.AlertBackException;
import org.koreait.global.libs.Utils;
import org.koreait.member.constants.Authority;
import org.koreait.member.libs.MemberUtil;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Lazy
@Service
@RequiredArgsConstructor
public class BoardAuthService {
    private final Utils utils;
    private final BoardConfigInfoService configInfoService;
    private final BoardInfoService infoService;
    private final MemberUtil memberUtil;

    /**
     * 게시판 권한 체크
     *
     * @param mode
     * @param bid
     * @param seq
     */
    public void check(String mode, String bid, Long seq) {
        if (!StringUtils.hasText(mode) || !StringUtils.hasText(bid) || (List.of("edit", "delete").contains(mode) && (seq == null || seq < 1L ))) {
            throw new AlertBackException(utils.getMessage("BadRequest"), HttpStatus.BAD_REQUEST);
        }

        if (memberUtil.isAdmin()) { // 관리자는 모두 허용
            return;
        }

        Board board = configInfoService.get(bid);

        /**
         * mode - write, list  / bid
         *      - edit, view  / seq
         */
        // 글쓰기, 글 목록 권한 체크
        Authority authority = null;
        if (List.of("write", "list").contains(mode)) {
            authority = mode.equals("list") ? board.getListAuthority() : board.getWriteAuthority();
        } else if (mode.equals("view")) {
            authority = board.getViewAuthority();

        } else if (List.of("edit", "delete").contains(mode)) { // 수정, 삭제
            /**
             * 1. 회원 게시글인 경우  / 직접 작성한 회원만 수정 가능
             *
             * 2. 비회원 게시글인 경우 / 비회원 비밀번호 확인이 완료된 경우 삭제 가능
             */
            BoardData item = infoService.get(seq);

        }

        if ((authority == Authority.USER && !memberUtil.isLogin()) || (authority == Authority.ADMIN && !memberUtil.isAdmin())) { // 회원 권한 + 로그인 X, 관리자 권한 + 관리자 X
            throw new AlertBackException(utils.getMessage("UnAuthorized"), HttpStatus.UNAUTHORIZED);
        }
    }
}
