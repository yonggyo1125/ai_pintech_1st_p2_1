package org.koreait.file.services;

import lombok.RequiredArgsConstructor;
import org.koreait.file.controllers.RequestUpload;
import org.koreait.file.entities.FileInfo;
import org.koreait.file.repositories.FileInfoRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Lazy
@Service
@RequiredArgsConstructor
public class FileUploadService {
    private final FileInfoRepository fileInfoRepository;

    public List<FileInfo> upload(RequestUpload form) {
        String gid = form.getGid();
        String location = form.getLocation();
        MultipartFile[] files = form.getFiles();

        // 1. 파일 업로드 정보 - DB에 기록 S

        // 1. 파일 업로드 정보 - DB에 기록 E

        return null;
    }
}
