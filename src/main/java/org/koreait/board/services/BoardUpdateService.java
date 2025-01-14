package org.koreait.board.services;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.koreait.board.controllers.RequestBoard;
import org.koreait.board.entities.Board;
import org.koreait.board.entities.BoardData;
import org.koreait.board.exceptions.BoardDataNotFoundException;
import org.koreait.board.repositories.BoardDataRepository;
import org.koreait.board.services.configs.BoardConfigInfoService;
import org.koreait.file.services.FileDoneService;
import org.koreait.member.libs.MemberUtil;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Objects;

@Lazy
@Service
@Transactional
@RequiredArgsConstructor
public class BoardUpdateService {

    private final BoardConfigInfoService configInfoService;
    private final BoardDataRepository boardDataRepository;
    private final MemberUtil memberUtil;
    private final PasswordEncoder passwordEncoder;
    private final HttpServletRequest request;
    private final FileDoneService fileDoneService;

    public BoardData process(RequestBoard form) {

        Long seq = Objects.requireNonNullElse(form.getSeq(), 0L);
        String mode = Objects.requireNonNullElse(form.getMode(), "write");

        BoardData data = null;
        if (mode.equals("edit")) { // 수정
            data = boardDataRepository.findById(seq).orElseThrow(BoardDataNotFoundException::new);
        } else { // 추가
            /**
             * 등록될때만 최초 한번 기록되는 데이터
             * - 게시판 설정, 회원
             * - gid
             * - 아이피, UserAgent
             */
            Board board = configInfoService.get(form.getBid());
            data = new BoardData();
            data.setBoard(board);
            data.setMember(memberUtil.getMember());
            data.setGid(form.getGid());
            data.setIpAddr(request.getRemoteAddr());
            data.setUserAgent(request.getHeader("User-Agent"));
        }

        // 글등록, 글 수정 공통 반영 사항
        String guestPw = form.getGuestPw();
        if (StringUtils.hasText(guestPw)) { // 비회원 비밀번호
            data.setGuestPw(passwordEncoder.encode(guestPw));
        }

        data.setPoster(form.getPoster());

        // 공지글 여부는 관리자만 반영 가능
        if (memberUtil.isAdmin()) {
            data.setNotice(form.isNotice());
        }

        data.setSubject(form.getSubject());
        data.setContent(form.getContent());
        data.setExternalLink(form.getExternalLink());
        data.setYoutubeUrl(form.getYoutubeUrl());
        data.setCategory(form.getCategory());

        boardDataRepository.saveAndFlush(data);
        fileDoneService.process(form.getGid());

        // 비회원 게시글 인증 정보 삭제
        request.getSession().removeAttribute("board_" + seq);

        return data;
    }
}
