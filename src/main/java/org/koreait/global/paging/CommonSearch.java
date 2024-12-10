package org.koreait.global.paging;

import lombok.Data;

/**
 * 공통 검색 항목
 *
 */
@Data
public class CommonSearch {
    private int page = 1; // 페이지 번호
    private int limit = 20; // 페이지당 출력 갯수

    private String sopt; // 검색 옵션
    private String skey; // 검색 키워드
}
