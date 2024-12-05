package org.koreait.file.controllers;

import jakarta.validation.Valid;
import org.koreait.member.controllers.RequestLogin;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/file")
public class ApiFileController {
    @GetMapping
    public void test(@Valid RequestLogin form, Errors errors) {
        if (errors.hasErrors()) {
            //errors.
            //System.out.println("검증 실패!");
        }
    }
}
