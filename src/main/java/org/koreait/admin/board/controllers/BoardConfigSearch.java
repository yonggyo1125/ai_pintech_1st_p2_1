package org.koreait.admin.board.controllers;

import lombok.Data;
import org.koreait.global.paging.CommonSearch;

import java.util.List;

@Data
public class BoardConfigSearch extends CommonSearch {
    private List<String> bid;
}
