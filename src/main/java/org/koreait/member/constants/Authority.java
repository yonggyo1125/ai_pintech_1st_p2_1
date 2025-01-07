package org.koreait.member.constants;

/**
 * 회원 권한
 *
 */
public enum Authority {
    ALL, // 모든 사용자(일반회원 + 부관리자 + 최고 관리자 + 비회원)
    USER, // 일반 회원
    MANAGER, // 부관리자
    ADMIN // 최고 관리자
}
