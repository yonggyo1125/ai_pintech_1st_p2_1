package org.koreait.global.entities;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class Terms {

    @NotBlank
    private String code; // 약관 코드 terms_{code}

    @NotBlank
    private String subject; // 약관 제목

    @NotBlank
    private String content; // 약관 내용
}
