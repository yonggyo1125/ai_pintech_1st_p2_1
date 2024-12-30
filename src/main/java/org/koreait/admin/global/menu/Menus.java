package org.koreait.admin.global.menu;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Menus {
    private static Map<String, List<MenuDetail>> submenus;

    static {
        submenus = new HashMap<>();

        // 기본 설정
        submenus.put("basic", List.of(
           new MenuDetail("siteConfig", "사이트 기본정보", "/admin/basic/siteConfig"),
                new MenuDetail("terms", "약관 관리", "/admin/basic/terms")
        ));

        // 회원관리
        submenus.put("member", List.of(
                new MenuDetail("list", "회원목록", "/admin/member/list"),
                new MenuDetail("message", "쪽지관리", "/admin/member/message")
        ));

        // 게시판관리
        submenus.put("board", List.of(
           new MenuDetail("list", "게시판목록", "/admin/board/list"),
           new MenuDetail("add", "게시판등록", "/admin/board/add"),
           new MenuDetail("posts", "게시글관리", "/admin/board/posts")
        ));

    }

    public static List<MenuDetail> getMenus(String menuCode) {
        return submenus.get(menuCode);
    }
}
