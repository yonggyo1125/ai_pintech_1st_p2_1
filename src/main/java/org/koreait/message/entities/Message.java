package org.koreait.message.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.koreait.file.entities.FileInfo;
import org.koreait.global.entities.BaseEntity;
import org.koreait.member.entities.Member;
import org.koreait.message.constants.MessageStatus;

import java.util.List;

@Data
@Entity
@Builder
@NoArgsConstructor @AllArgsConstructor
@Table(indexes = @Index(name="idx_notice_created_at", columnList = "notice DESC, createdAt DESC"))
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

    @Transient
    private boolean received;

    @Transient
    private boolean deletable; // 삭제 가능 여부

    private boolean deletedBySender; // 보내는 쪽에서 쪽지를 삭제한 경우

    private boolean deletedByReceiver; // 받는 쪽에서 쪽지를 삭제한 경우
}
