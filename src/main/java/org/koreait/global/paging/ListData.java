package org.koreait.global.paging;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ListData<T> {
    private List<T> items; // 목록 데이터
    private Pagination pagination; // 페이징 기초 데이터
}
