package org.koreait.board.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.koreait.board.entities.Board;
import org.koreait.board.entities.BoardData;
import org.koreait.board.entities.CommentData;
import org.koreait.board.exceptions.GuestPasswordCheckException;
import org.koreait.board.services.*;
import org.koreait.board.services.comment.CommentDeleteService;
import org.koreait.board.services.comment.CommentInfoService;
import org.koreait.board.services.comment.CommentUpdateService;
import org.koreait.board.services.configs.BoardConfigInfoService;
import org.koreait.board.validators.BoardValidator;
import org.koreait.board.validators.CommentValidator;
import org.koreait.file.constants.FileStatus;
import org.koreait.file.services.FileInfoService;
import org.koreait.global.annotations.ApplyErrorPage;
import org.koreait.global.entities.SiteConfig;
import org.koreait.global.exceptions.scripts.AlertException;
import org.koreait.global.libs.Utils;
import org.koreait.global.paging.ListData;
import org.koreait.global.services.CodeValueService;
import org.koreait.member.libs.MemberUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Controller
@ApplyErrorPage
@RequestMapping("/board")
@RequiredArgsConstructor
@SessionAttributes({"commonValue"})
public class BoardController {

    private final Utils utils;
    private final MemberUtil memberUtil;
    private final BoardConfigInfoService configInfoService;
    private final FileInfoService fileInfoService;
    private final BoardValidator boardValidator;
    private final BoardUpdateService boardUpdateService;
    private final BoardInfoService boardInfoService;
    private final BoardViewUpdateService boardViewUpdateService;
    private final BoardDeleteService boardDeleteService;
    private final BoardAuthService boardAuthService;
    private final CodeValueService codeValueService;
    private final CommentValidator commentValidator;
    private final CommentUpdateService commentUpdateService;
    private final CommentInfoService commentInfoService;
    private final CommentDeleteService commentDeleteService;
    private final HttpServletRequest request;


    /**
     * 사용자별 공통 데이터
     *
     * @return
     */
    @ModelAttribute("commonValue")
    public CommonValue commonValue() {
        return new CommonValue();
    }

    /**
     * 게시판 목록
     *
     * @param bid
     * @param model
     * @return
     */
    @GetMapping("/list/{bid}")
    public String list(@PathVariable("bid") String bid, BoardSearch search, Model model) {
        commonProcess(bid, "list", model);

        ListData<BoardData> data = boardInfoService.getList(bid, search);
        model.addAttribute("items", data.getItems());
        model.addAttribute("pagination", data.getPagination());

        return utils.tpl("board/list");
    }

    /**
     * 게시글 보기
     *
     * @param seq
     * @param model
     * @return
     */
    @GetMapping("/view/{seq}")
        public String view(@PathVariable("seq") Long seq, Model model, @ModelAttribute RequestComment form) {
        commonProcess(seq, "view", model);

        long viewCount = boardViewUpdateService.process(seq); // 조회수 업데이트
        BoardData data = (BoardData)model.getAttribute("boardData");
        data.setViewCount(viewCount);

        Board board = data.getBoard();
        if (board.isListUnderView()) { // 보기페이지 하단에 게시글 목록 출력

            BoardSearch search = new BoardSearch();
            search.setPage(boardInfoService.getPage(board.getBid(), seq, board.getRowsPerPage()));

            ListData<BoardData> listData = boardInfoService.getList(board.getBid(), search);
            model.addAttribute("items", listData.getItems());
            model.addAttribute("pagination", listData.getPagination());
        }

        // 댓글을 사용하는 경우
        if (board.isUseComment()) {
            if (memberUtil.isLogin()) {
                form.setCommenter(memberUtil.getMember().getName());
            }

            form.setMode("write");
            form.setTarget("ifrmProcess");
            form.setBoardDataSeq(seq);

            List<CommentData> comments = commentInfoService.getList(seq); // 댓글 목록
            model.addAttribute("comments", comments);
        }



        return utils.tpl("board/view");
    }

    /**
     * 글쓰기
     *
     * @param bid
     * @param model
     * @return
     */
    @GetMapping("/write/{bid}")
    public String write(@PathVariable("bid") String bid, @ModelAttribute RequestBoard form, Model model) {
        commonProcess(bid, "write", model);

        form.setBid(bid);
        form.setGid(UUID.randomUUID().toString());

        if (memberUtil.isLogin()) {
            form.setPoster(memberUtil.getMember().getName());
        }

        return utils.tpl("board/write");
    }

    /**
     * 게시글 수정
     *
     * @param seq
     * @param model
     * @return
     */
    @GetMapping("/edit/{seq}")
    public String edit(@PathVariable("seq") Long seq, Model model, @SessionAttribute("commonValue") CommonValue commonValue) {
        commonProcess(seq, "edit", model);

        RequestBoard form = boardInfoService.getForm(commonValue.getData());
        model.addAttribute("requestBoard", form);

        return utils.tpl("board/edit");
    }

    /**
     * 글등록, 수정 처리
     *
     * @return
     */
    @PostMapping("/save")
    public String save(@Valid RequestBoard form, Errors errors, @SessionAttribute("commonValue") CommonValue commonValue, Model model) {
        String mode = form.getMode();
        mode = StringUtils.hasText(mode) ? mode : "write";

        if (mode.equals("edit")) commonProcess(form.getSeq(), mode, model);
        else commonProcess(form.getBid(), mode, model);

        boardValidator.validate(form, errors);

        if (errors.hasErrors()) {
            String gid = form.getGid();
            form.setEditorImages(fileInfoService.getList(gid, "editor", FileStatus.ALL));
            form.setAttachFiles(fileInfoService.getList(gid, "attach", FileStatus.ALL));

            return utils.tpl("board/" + mode);
        }

        BoardData data = boardUpdateService.process(form);

        Board board = commonValue.getBoard();
        // 글작성, 수정 성공시 글보기 또는 글목록으로 이동
        String redirectUrl = String.format("/board/%s", board.getLocationAfterWriting().equals("view") ? "view/" + data.getSeq() : "list/" + board.getBid());
        return "redirect:" + redirectUrl;
    }

    /**
     * 게시글 삭제
     * @param seq
     * @param model
     * @return
     */
    @GetMapping("/delete/{seq}")
    public String delete(@PathVariable("seq") Long seq, Model model, @SessionAttribute("commonValue") CommonValue commonValue) {
        commonProcess(seq, "delete", model);
        Board board = commonValue.getBoard();

        boardValidator.checkDelete(seq); // 게시글에 댓글이 있으면 삭제 불가

        boardDeleteService.delete(seq);

        return "redirect:/board/list/" + board.getBid();
    }

    /**
     * 댓글 등록/수정
     *
     * @param form
     * @param errors
     * @param model
     * @return
     */
    @PostMapping("/comment")
    public String comment(@Valid RequestComment form, Errors errors, Model model) {
        String mode = form.getMode();
        mode = StringUtils.hasText(mode) ? mode : "write";
        if (mode.equals("edit")) {
            commonProcess(form.getSeq(), "comment", model);
        }

        commentValidator.validate(form, errors);

        if (errors.hasErrors()) {
            if (!mode.equals("edit")) { // 댓글 등록시에는 alert 메세지로 검증 실패를 알린다.
                FieldError err = errors.getFieldErrors().get(0);
                String code = err.getCode();
                String field = err.getField();
                throw new AlertException(utils.getMessage(code + ".requestComment." + field));
            }

            return utils.tpl("board/comment"); // 수정
        }

        // 댓글 등록/수정 서비스
        CommentData item = commentUpdateService.save(form);

        String redirectUrl = String.format("/board/view/%d#comment-%d", form.getBoardDataSeq(), item.getSeq());
        if (mode.equals("edit")) {
            return "redirect:" + redirectUrl;
        } else {

            redirectUrl = request.getContextPath() + redirectUrl;

            String script = String.format("parent.location.reload(); parent.addEventListener('DOMContentLoaded', function() { parent.location.replace('%s'); });", redirectUrl);
            model.addAttribute("script", script);
            return "common/_execute_script";
        }
    }

    /**
     * 댓글 수정
     *
     * @param seq
     * @param model
     * @return
     */
    @GetMapping("/comment/edit/{seq}")
    public String commentEdit(@PathVariable("seq") Long seq, Model model) {
        commonProcess(seq, "comment", model);

        RequestComment form = commentInfoService.getForm(seq);

        model.addAttribute("requestComment", form);

        return utils.tpl("board/comment");
    }

    /**
     * 댓글 삭제 처리
     *
     * @param seq
     * @param model
     * @return
     */
    @GetMapping("/comment/delete/{seq}")
    public String commentDelete(@PathVariable("seq") Long seq, Model model) {
        commonProcess(seq, "comment", model);

        BoardData item = commentDeleteService.delete(seq);

        return "redirect:/board/view/" + item.getSeq();
    }

    /**
     * 비회원 비밀번호 처리
     *
     * @return
     */
    @ExceptionHandler(GuestPasswordCheckException.class)
    public String guestPassword(Model model) {

        SiteConfig config = Objects.requireNonNullElseGet(codeValueService.get("siteConfig", SiteConfig.class), SiteConfig::new);
        model.addAttribute("siteConfig", config);

        return utils.tpl("board/password");
    }

    /**
     * 비회원 비밀번호 검증
     *
     * @param password
     * @param session
     * @param model
     * @return
     */
    @PostMapping("/password")
    public String validateGuestPassword(@RequestParam(name="password", required = false) String password, HttpSession session, Model model) {
        if (!StringUtils.hasText(password)) {
            throw new AlertException(utils.getMessage("NotBlank.password"));
        }

        /* 비회원 게시글 비밀번호 검증 S */
        Long seq = (Long)session.getAttribute("seq");

        if (seq != null && seq > 0L) {
            if (!boardValidator.checkGuestPassword(password, seq)) {
                throw new AlertException(utils.getMessage("Mismatch.password"));
            }

            // 비회원 비밀번호 검증 성공시 세션에 board_게시글번호
            session.setAttribute("board_" + seq, true);
        }
        /* 비회원 게시글 비밀번호 검증 E */

        /* 비회원 댓글 비밀번홀 검증 S */
        Long cSeq = (Long)session.getAttribute("cSeq");
        if (cSeq != null && cSeq > 0L) {
            if (!commentValidator.checkGuestPassword(password, cSeq)) {
                throw new AlertException(utils.getMessage("Mismatch.password"));
            }

            // 비회원 댓글 비밀번호 검증 성공 comment_댓글번호
            session.setAttribute("comment_" + cSeq, true);
        }
        /* 비회원 댓글 비밀번홀 검증 E */

        // 비회원 비밀번호 인증 완료된 경우 새로 고침
        model.addAttribute("script", "parent.location.reload();");
        return "common/_execute_script";
    }


    // 공통 처리
    private void commonProcess(String bid, String mode, Model model) {

        // 권한 체크
        if (!List.of("edit", "delete", "comment").contains(mode)) {
            boardAuthService.check(mode, bid);
        }

        Board board = configInfoService.get(bid);
        String pageTitle = board.getName(); // 게시판명 - 목록, 글쓰기
        List<String> addCommonScript = new ArrayList<>();
        List<String> addScript = new ArrayList<>();
        List<String> addCss = new ArrayList<>();

        // 게시판 공통 CSS, JS
        addScript.add("board/common"); // 게시판 공통 스크립트
        addCss.add("board/style"); // 게시판 공통 스타일시트

        // 게시판 스킨별 CSS, JS
        addScript.add(String.format("board/%s/common", board.getSkin()));
        addCss.add(String.format("board/%s/style", board.getSkin()));
        
        if (mode.equals("write") || mode.equals("edit")) { // 글작성, 글수정
            if (board.isUseEditor()) { // 에디터를 사용하는 경우
                addCommonScript.add("ckeditor5/ckeditor");
            } else { // 에디터를 사용하지 않는 경우는 이미지 첨부 불가
                board.setUseEditorImage(false);
            }

            // 파일 업로드가 필요한 설정이라면
            if (board.isUseAttachFile() || board.isUseEditorImage()) {
                addCommonScript.add("fileManager");
            }

            addScript.add(String.format("board/%s/form", board.getSkin()));
        }

        // 게시글 번호가 있는 mode가 view이거나 edit인 경우는 배제
        if (!List.of("view", "edit").contains(mode)) {
            CommonValue commonValue = commonValue();
            commonValue.setBoard(board);
            model.addAttribute("commonValue", commonValue);
            model.addAttribute("pageTitle", pageTitle);
        }

        model.addAttribute("board", board);
        model.addAttribute("categories", board.getCategories());
        model.addAttribute("addCommonScript", addCommonScript);
        model.addAttribute("addScript", addScript);
        model.addAttribute("addCss", addCss);
        model.addAttribute("mode", mode);
    }

    // 게시글 보기, 게시글 수정
    private void commonProcess(Long seq, String mode, Model model) {

        BoardData item = null;
        CommentData comment = null;
        if (mode.equals("comment")) { // 댓글 수정, 삭제
            comment = commentInfoService.get(seq);
            item = comment.getData();
        } else {
            item = boardInfoService.get(seq);
        }

        Board board = item.getBoard();

        // 게시판 권한 체크
        boardAuthService.check(mode, seq);

        String pageTitle = String.format("%s - %s", item.getSubject(), board.getName());

        String bid = board.getBid();
        commonProcess(bid, mode, model);

        CommonValue commonValue = commonValue();
        commonValue.setBoard(board);
        commonValue.setData(item);
        commonValue.setComment(comment);

        model.addAttribute("commonValue", commonValue);
        model.addAttribute("pageTitle", pageTitle);
        model.addAttribute("boardData", item);
    }

    @Data
    static class CommonValue implements Serializable {
        private Board board;
        private BoardData data;
        private CommentData comment;
    }
}
