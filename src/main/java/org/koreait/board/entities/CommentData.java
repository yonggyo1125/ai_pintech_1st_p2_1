package org.koreait.board.entities;

import jakarta.persistence.*;
import lombok.Data;
import org.koreait.global.entities.BaseEntity;
import org.koreait.member.entities.Member;

import java.io.Serializable;

@Data
@Entity
@Table(indexes = @Index(name = "idx_comment_data_created_at", columnList = "createdAt ASC"))
public class CommentData extends BaseEntity implements Serializable {
    @Id @GeneratedValue
    private Long seq;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    private BoardData data;

    @Column(length=40, nullable = false)
    private String commenter;

    @Column(length=65)
    private String guestPw;

    @Lob
    @Column(nullable = false)
    private String content;

    @Column(length=20)
    private String ipAddr;

    @Column(length=150)
    private String userAgent;

    @Transient
    private boolean editable; // 댓글 수정, 삭제 가능 여부
}
