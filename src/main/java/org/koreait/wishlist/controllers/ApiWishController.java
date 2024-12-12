package org.koreait.wishlist.controllers;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.koreait.wishlist.constants.WishType;
import org.koreait.wishlist.services.WishService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/wish")
public class ApiWishController {

    private final HttpServletRequest request;
    private final WishService service;

    @GetMapping({"/add", "/remove"})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void process(@RequestParam("seq") Long seq, @RequestParam("type") WishType type) {
        String mode = request.getRequestURI().contains("/remove") ? "remove" : "add";

        service.process(mode, seq, type);
    }
}
