package org.koreait.message.entities;

import jakarta.persistence.*;
import lombok.Data;
import org.koreait.file.entities.FileInfo;
import org.koreait.global.entities.BaseEntity;
import org.koreait.member.entities.Member;
import org.koreait.message.constants.MessageStatus;

import java.util.List;

@Data
@Entity
public class Message extends BaseEntity {
    @Id @GeneratedValue
    private Long seq;

    private boolean notice; // 공지

    @Column(length=45, nullable = false)
    private String gid;

    @Enumerated(EnumType.STRING)
    @Column(length=10, nullable = false)
    private MessageStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="sender")
    private Member sender; // 보내는 사람

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="receiver")
    private Member receiver; // 받는 사람

    @Column(length=150, nullable = false)
    private String subject; // 제목

    @Lob
    @Column(nullable = false)
    private String content; // 내용

    @Transient
    private List<FileInfo> editorImages;

    @Transient
    private List<FileInfo> attachFiles;
}
