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
           new MenuDetail("list", "회원목록", "/admin/member/list")
        ));
    }

}
