package org.koreait.file.controllers;

import org.koreait.global.annotations.RestExceptionHandling;
import org.koreait.global.exceptions.BadRequestException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/file")
@RestExceptionHandling
public class ApiFileController {
    @GetMapping
    public void test() {
        boolean result = true;
        if (result) {
            throw new BadRequestException("그냥 테스트...");
        }
    }
}
