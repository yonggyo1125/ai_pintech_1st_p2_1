package org.koreait.global.paging;

import lombok.Data;

@Data
public class ListData<T> {
    private T items; // 목록 데이터
    private Pagination pagination; // 페이징 기초 데이터
}
