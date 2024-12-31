package org.koreait.message.controllers;

import lombok.RequiredArgsConstructor;
import org.koreait.global.annotations.ApplyErrorPage;
import org.koreait.global.libs.Utils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@ApplyErrorPage
@RequestMapping("/message")
@RequiredArgsConstructor
public class MessageController {

    private final Utils utils;

    /**
     * 쪽지 작성 양식
     *
     * @return
     */
    @GetMapping
    public String form() {

        return utils.tpl("message/form");
    }

    /**
     * 쪽지 작성
     *
     * @return
     */
    @PostMapping
    public String process() {

        return "redirect:/message/list";
    }

    /**
     * 보낸거나 받은 쪽지 목록
     *
     * @return
     */
    @GetMapping("/list")
    public String list() {

        return utils.tpl("message/list");
    }

    @GetMapping("/view/{seq}")
    public String view(@PathVariable("seq") Long seq) {

        return utils.tpl("message/view");
    }

    @DeleteMapping
    public String delete(@RequestParam(name="seq", required = false) List<String> seq) {

        return "redirect:/message/list";
    }
}
