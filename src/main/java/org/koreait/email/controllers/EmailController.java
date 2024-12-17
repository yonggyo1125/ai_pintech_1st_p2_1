package org.koreait.email.controllers;

import lombok.RequiredArgsConstructor;
import org.koreait.email.services.EmailAuthService;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Profile("email")
@RestController
@RequiredArgsConstructor
@RequestMapping("/email")
public class EmailController {
    private final EmailAuthService authService;

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @GetMapping("/auth/{to}")
    public void authCode(@PathVariable("to") String to) {
        if (!authService.sendCode(to)) {

        }
    }
}
