package org.koreait.main.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/")
public class MainController {

    @GetMapping
    public String index(Model model) {
        model.addAttribute("addCss", List.of("member/test1", "member/test2"));
        model.addAttribute("addScript", new String[] {"member/test1", "member/test2"});
        return "front/main/index";
    }
}
