package org.koreait.board.controllers;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.koreait.board.entities.Board;
import org.koreait.board.services.configs.BoardConfigInfoService;
import org.koreait.global.annotations.ApplyErrorPage;
import org.koreait.global.libs.Utils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@ApplyErrorPage
@RequestMapping("/board")
@RequiredArgsConstructor
@SessionAttributes({"commonValue"})
public class BoardController {

    private final Utils utils;
    private final BoardConfigInfoService configInfoService;

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
    public String list(@PathVariable("bid") String bid, Model model) {
        commonProcess(bid, "list", model);


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
    public String view(@PathVariable("seq") Long seq, Model model) {
        commonProcess(seq, "view", model);

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
    public String write(@PathVariable("bid") String bid, Model model) {
        commonProcess(bid, "add", model);

        return utils.tpl("board/write");
    }

    /**
     * 게시글 수정
     *
     * @param seq
     * @param model
     * @return
     */
    public String edit(@PathVariable("seq") Long seq, Model model) {
        commonProcess(seq, "edit", model);

        return utils.tpl("board/edit");
    }

    /**
     * 게시글 삭제
     * @param seq
     * @param model
     * @return
     */
    @GetMapping("/delete/{seq}")
    public String delete(@PathVariable("seq") Long seq, Model model) {
        commonProcess(seq, "delete", model);

        return "redirect:/board/list/...";
    }


    // 공통 처리
    private void commonProcess(String bid, String mode, Model model) {
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


        CommonValue commonValue = commonValue();
        commonValue.setBoard(board);

        model.addAttribute("board", board);
        model.addAttribute("commonValue", commonValue);
        model.addAttribute("addCommonScript", addCommonScript);
        model.addAttribute("addScript", addScript);
        model.addAttribute("addCss", addCss);
        model.addAttribute("pageTitle", pageTitle);
    }

    private void commonProcess(Long seq, String mode, Model model) {
        String bid = null;

        commonProcess(bid, mode, model);
    }

    @Data
    static class CommonValue {
        private Board board;
    }
}
