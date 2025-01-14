package org.koreait.board.entities;

import jakarta.persistence.*;
import lombok.Data;
import org.koreait.file.entities.FileInfo;
import org.koreait.global.entities.BaseEntity;
import org.koreait.member.entities.Member;

import java.io.Serializable;
import java.util.List;

@Data
@Entity
@Table(indexes = {
        @Index(name="idx_bd_created_at", columnList = "createdAt DESC"),
        @Index(name="idx_bd_notice_created_at", columnList = "notice DESC, createdAt DESC")
})
public class BoardData extends BaseEntity implements Serializable {
    @Id @GeneratedValue
    private Long seq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="bid")
    private Board board;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @Column(length=45, nullable = false)
    private String gid;

    @Column(length=45, nullable = false)
    private String poster; // 작성자명

    @Column(length=65)
    private String guestPw; // 글 수정, 삭제 비밀번호

    private boolean notice; // 공지글 여부

    @Column(nullable = false)
    private String subject; // 글 제목

    @Lob
    private String content;

    private long viewCount; // 조회수

    private long commentCount; // 댓글수

    @Column(length=20)
    private String ipAddr; // ip 주소

    private String userAgent; // 브라우저 정보

    @Column(length=150)
    private String externalLink; // 외부 링크 -> 게시글 링크를 외부 링크로 변경

    @Column(length=60)
    private String youtubeUrl; // Youtube 영상 링크

    @Column(length=60)
    private String category; // 게시글 분류

    @Transient
    private BoardData prev; // 이전 게시글

    @Transient
    private BoardData next; // 다음 게시글

    @Transient
    private List<FileInfo> editorImages; // 에디터 첨부 이미지

    @Transient
    private List<FileInfo> attachFiles; // 다운로드용 첨부 파일

    @Transient
    private boolean listable; // 목록 버튼 노출 여부

    @Transient
    private boolean editable; // 수정, 삭제 버튼 노출 여부

    @Transient
    private boolean writable; // 글쓰기 버튼 노출 여부

    @Transient
    private boolean mine; // 내가 작성한 게시글 여부
}
