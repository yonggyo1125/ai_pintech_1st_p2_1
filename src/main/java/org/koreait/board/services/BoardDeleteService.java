package org.koreait.board.services;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.koreait.board.entities.BoardData;
import org.koreait.board.repositories.BoardDataRepository;
import org.koreait.file.services.FileDeleteService;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Lazy
@Service
@RequiredArgsConstructor
public class BoardDeleteService {
    private final BoardInfoService infoService;
    private final BoardDataRepository boardRepository;
    private final FileDeleteService fileDeleteService;
    private final HttpSession session;

    public void delete(Long seq) {

        BoardData item = infoService.get(seq);
        String gid = item.getGid();

        // 파일 삭제
        fileDeleteService.deletes(gid);

        boardRepository.delete(item);
        boardRepository.flush();

        // 비회원 인증 정보 삭제
        session.removeAttribute("board_" + seq);
    }
}
