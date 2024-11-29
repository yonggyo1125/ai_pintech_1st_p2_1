package org.koreait.member.entities;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import org.koreait.member.constants.Authority;

@EqualsAndHashCode
@AllArgsConstructor
public class AuthoritiesId {
    private Member member;
    private Authority authority;
}
