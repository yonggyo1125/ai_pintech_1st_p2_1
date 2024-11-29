package org.koreait.member.entities;

import jakarta.persistence.*;
import lombok.Data;
import org.koreait.member.constants.Authority;

@Data
@Entity
@IdClass(AuthoritiesId.class)
public class Authorities {
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @Id
    @Enumerated(EnumType.STRING)
    @Column(length=15)
    private Authority authority;
}
