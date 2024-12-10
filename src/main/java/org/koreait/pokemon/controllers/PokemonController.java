package org.koreait.pokemon.controllers;

import lombok.RequiredArgsConstructor;
import org.koreait.global.annotations.ApplyErrorPage;
import org.koreait.global.libs.Utils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@ApplyErrorPage
@RequestMapping("/pokemon")
@RequiredArgsConstructor
public class PokemonController {

    private final Utils utils;

    @GetMapping("/list")
    public String list(Model model) {
        commonProcess("list", model);

        return utils.tpl("pokemon/list");
    }

    @GetMapping("/view/{seq}")
    public String view(@PathVariable("seq") Long seq, Model model) {
        commonProcess("view", model);

        return utils.tpl("pokemon/view");
    }

    private void commonProcess(String mode, Model model) {
        mode = StringUtils.hasText(mode) ? mode : "list";
        String pageTitle = utils.getMessage("포켓몬_도감");

        List<String> addCss = new ArrayList<>();
        addCss.add("pokemon/style"); // 포켓몬 도감 페이지 공통 스타일(목록, 상세)

        if (mode.equals("list")) {
            addCss.add("pokemon/list"); // 목록쪽에만 적용되는 스타일
        } else if (mode.equals("view")) {
            addCss.add("pokemon/view"); // 상세쪽에만 적용되는 스타일
        }

        model.addAttribute("pageTitle", pageTitle);
        model.addAttribute("addCss", addCss);
    }
}
