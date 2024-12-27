package org.koreait.admin.member.controllers;

import lombok.Data;
import org.koreait.global.paging.CommonSearch;
import org.koreait.member.constants.Authority;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

@Data
public class MemberSearch extends CommonSearch {
    private List<String> email;
    private List<Authority> authority;
    private String dateType;

    @DateTimeFormat(pattern="yyyy-MM-dd")
    private LocalDate sDate;

    @DateTimeFormat(pattern="yyyy-MM-dd")
    private LocalDate eDate;
}
