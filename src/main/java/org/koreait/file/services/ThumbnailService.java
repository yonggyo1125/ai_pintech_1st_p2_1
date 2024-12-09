package org.koreait.file.services;

import lombok.RequiredArgsConstructor;
import org.koreait.file.controllers.RequestThumb;
import org.koreait.file.entities.FileInfo;
import org.koreait.global.configs.FileProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.util.Objects;

@Lazy
@Service
@RequiredArgsConstructor
@EnableConfigurationProperties(FileProperties.class)
public class ThumbnailService {

    private final FileProperties properties;
    private final FileInfoService infoService;
    private final RestTemplate restTemplate;


    public String create(RequestThumb form) {

        return null;
    }

    /**
     * Thumbnail 경로
     * thumbs/폴더번호/seq_너비_높이.확장자
     * thumbs/urls/정수해시코드_너비_높이.확장자
     */
    public String getThumbPath(long seq, String url, int width, int height) {
        String thumbPath = properties.getPath() + "thumbs/";
        if (seq > 0L) { // 직접 서버에 올린 파일
            FileInfo item = infoService.get(seq);

            thumbPath = thumbPath + String.format("%d/%d_%d_%d%s", seq % 10L, seq, width, height, item.getExtension());
        } else if (StringUtils.hasText(url)){ // 원격 URL 이미지인 경우
            String extension = url.lastIndexOf(".") == -1 ? "": url.substring(url.lastIndexOf("."));
            if (StringUtils.hasText(extension)) {
                extension = extension.split("[?#]")[0];
            }
            thumbPath = thumbPath + String.format("urls/%d_%d_%d%s", Objects.hash(url), width, height, extension);
        }

        File file = new File(thumbPath);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }

        return thumbPath;
    }
}
