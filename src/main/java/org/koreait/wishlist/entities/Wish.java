package org.koreait.wishlist.entities;

import jakarta.persistence.*;
import lombok.Data;
import org.koreait.member.entities.Member;
import org.koreait.wishlist.constants.WishType;

@Data
@Entity
@IdClass(WishId.class)
public class Wish {
    @Id
    private Long seq;

    @Id
    @Enumerated(EnumType.STRING)
    @Column(length=15, name="_type")
    private WishType type;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;
}
