package org.koreait.board.controllers;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RequestComment {
    private String mode;
    private Long seq;

    @NotBlank
    private String commenter;

    private String guestPw;

    @NotBlank
    private String content;
}
