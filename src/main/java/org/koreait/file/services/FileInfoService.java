package org.koreait.file.services;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.koreait.file.entities.FileInfo;
import org.koreait.file.exceptions.FileNotFoundException;
import org.koreait.file.repositories.FileInfoRepository;
import org.koreait.global.configs.FileProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
@EnableConfigurationProperties(FileProperties.class)
public class FileInfoService  {
    private final FileInfoRepository infoRepository;

    private final FileProperties properties;

    private final HttpServletRequest request;

    public FileInfo get(Long seq) {
        FileInfo item = infoRepository.findById(seq).orElseThrow(FileNotFoundException::new);

        addInfo(item); // 추가 정보 처리

        return item;
    }

    /**
     * 추가 정보 처리
     *
     * @param item
     */
    public void addInfo(FileInfo item) {
        // filePath - 서버에 올라간 실제 경로(다운로드, 삭제시 활용...)
        item.setFilePath(getFilePath(item));

        // fileUrl - 접근할 수 있는 주소(브라우저)
        item.setFileUrl(getFileUrl(item));
    }

    public String getFilePath(FileInfo item) {
        Long seq = item.getSeq();
        String extension = Objects.requireNonNullElse(item.getExtension(), "");
        return String.format("%s%s/%s", properties.getPath(), getFolder(seq), seq + extension);
    }

    public String getFilePath(Long seq) {
        FileInfo item = infoRepository.findById(seq).orElseThrow(FileNotFoundException::new);
        return getFilePath(item);
    }

    public String getFileUrl(FileInfo item) {
        Long seq = item.getSeq();
        String extension = Objects.requireNonNullElse(item.getExtension(), "");
        return String.format("%s%s%s/%s", request.getContextPath(), properties.getUrl(), getFolder(seq), seq + extension);
    }

    public String getFileUrl(Long seq) {
        FileInfo item = infoRepository.findById(seq).orElseThrow(FileNotFoundException::new);
        return getFileUrl(item);
    }

    private long getFolder(long seq) {
        return seq % 10L;
    }

}
