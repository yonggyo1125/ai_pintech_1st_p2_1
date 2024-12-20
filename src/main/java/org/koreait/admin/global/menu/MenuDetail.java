package org.koreait.admin.global.menu;

public record MenuDetail(
   String code, // 서브 메뉴 코드
   String name, // 서브 메뉴 이름
   String url // 서브 메뉴 이동 URL
) {}
