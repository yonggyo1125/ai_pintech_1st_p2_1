package org.koreait.pokemon.controllers;

import lombok.RequiredArgsConstructor;
import org.koreait.global.annotations.ApplyErrorPage;
import org.koreait.global.libs.Utils;
import org.koreait.global.paging.ListData;
import org.koreait.pokemon.entities.Pokemon;
import org.koreait.pokemon.services.PokemonInfoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
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
    private final PokemonInfoService infoService;

    @GetMapping("/list")
    public String list(@ModelAttribute PokemonSearch search, Model model) {
        commonProcess("list", model);

        ListData<Pokemon> data = infoService.getList(search);
        model.addAttribute("items", data.getItems());
        model.addAttribute("pagination", data.getPagination());

        return utils.tpl("pokemon/list");
    }

    @GetMapping("/view/{seq}")
    public String view(@PathVariable("seq") Long seq, Model model) {
        Pokemon item = infoService.get(seq);
        model.addAttribute("item", item);

        commonProcess("view", model);
        return utils.tpl("pokemon/view");
    }

    private void commonProcess(String mode, Model model) {
        mode = StringUtils.hasText(mode) ? mode : "list";
        String pageTitle = utils.getMessage("포켓몬_도감");

        List<String> addCss = new ArrayList<>();
        List<String> addCommonScript = new ArrayList<>();

        addCss.add("pokemon/style"); // 포켓몬 도감 페이지 공통 스타일(목록, 상세)
        addCommonScript.add("wish"); // 찜하기

        if (mode.equals("list")) {
            addCss.add("pokemon/list"); // 목록쪽에만 적용되는 스타일
        } else if (mode.equals("view")) {
            addCss.add("pokemon/view"); // 상세쪽에만 적용되는 스타일

            // 상세 보기에서는 포켓몬 이름으로 제목을 완성
            Pokemon item = (Pokemon) model.getAttribute("item");
            if (item != null) {
                pageTitle = String.format("%s - %s", item.getName(), pageTitle);
            }
        }

        model.addAttribute("pageTitle", pageTitle);
        model.addAttribute("addCss", addCss);
        model.addAttribute("addCommonScript", addCommonScript);
    }
}
