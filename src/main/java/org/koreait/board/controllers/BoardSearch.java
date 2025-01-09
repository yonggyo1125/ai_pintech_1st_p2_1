package org.koreait.board.controllers;

import lombok.Data;
import org.koreait.global.paging.CommonSearch;

import java.util.List;

@Data
public class BoardSearch extends CommonSearch {
    private List<String> bid;
    private String sort; // 필드명_정렬방향  예) viewCount_DESC
    private List<String> email; // 회원 이메일
    private List<String> category; // 분류 조회
}
