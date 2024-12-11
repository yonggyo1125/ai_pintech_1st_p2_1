package org.koreait.file.entities;

import jakarta.persistence.*;
import lombok.Data;
import org.koreait.global.entities.BaseMemberEntity;
import org.springframework.util.StringUtils;

import java.io.Serializable;

@Data
@Entity
@Table(indexes = {
        @Index(name="idx_gid", columnList = "gid, createdAt"),
        @Index(name="idx_gid_location", columnList = "gid, location, createdAt")
})
public class FileInfo extends BaseMemberEntity implements Serializable {
    @Id @GeneratedValue
    private Long seq; // 파일 등록 번호

    @Column(length=45, nullable = false)
    private String gid; // 파일 그룹

    @Column(length=45)
    private String location;  // 그룹 내에서 위치

    @Column(length=100, nullable = false)
    private String fileName; // 업로드시 원 파일명

    @Column(length=30)
    private String extension; // 확장자

    @Column(length=65)
    private String contentType; // 파일 형식  image/png   application/..

    @Transient
    private String fileUrl; // URL로 파일 접근할 수 있는 주소 - 2차 가공

    @Transient
    private String filePath; // 파일이 서버에 있는 경로

    @Transient
    private String thumbUrl; // 썸네일 기본 URL

    private boolean done; // 파일과 연관된 작업이 완료되었는지 여부

    // 이미지 형식 여부
    public boolean isImage() {

        return StringUtils.hasText(contentType) && contentType.contains("image/");
    }
}
