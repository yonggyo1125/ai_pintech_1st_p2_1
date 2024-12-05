package org.koreait.file.controllers;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class RequestUpload {
    @NotBlank
    private String gid;
    private String location;

    public MultipartFile[] files;
}
