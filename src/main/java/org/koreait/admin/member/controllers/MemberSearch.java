package org.koreait.admin.member.controllers;

import lombok.Data;
import org.koreait.global.paging.CommonSearch;
import org.koreait.member.constants.Authority;

import java.time.LocalDate;
import java.util.List;

@Data
public class MemberSearch extends CommonSearch {
    private List<String> email;
    private List<Authority> authority;
    private String dateType;
    private LocalDate sDate;
    private LocalDate eDate;
}
