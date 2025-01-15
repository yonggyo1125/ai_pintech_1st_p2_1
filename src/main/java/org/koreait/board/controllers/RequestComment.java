package org.koreait.board.controllers;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RequestComment {
    private String mode;
    private Long seq;

    @NotNull
    private Long boardDataSeq;

    @NotBlank
    private String commenter;

    @Size(min=4)
    private String guestPw;

    @NotBlank
    private String content;

    private String target;
}
