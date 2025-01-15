package org.koreait.main.controllers;

import lombok.RequiredArgsConstructor;
import org.koreait.global.libs.Utils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class MainController {

    private final Utils utils;

    @ModelAttribute("addCss")
    public List<String> addCss() {
        return List.of("board/gallery/style", "main/style");
    }

    @ModelAttribute("addScript")
    public List<String> addScript() {
        return List.of("main/common");
    }

    @GetMapping
    public String index(Model model) {


        return utils.tpl("main/index");
    }
}
