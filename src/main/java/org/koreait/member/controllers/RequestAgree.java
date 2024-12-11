package org.koreait.member.controllers;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class RequestAgree implements Serializable {
    //@AssertTrue
    private boolean requiredTerms1; // 필수 약관 동의 여부

    //@AssertTrue
    private boolean requiredTerms2;

    //@AssertTrue
    private boolean requiredTerms3;

    private List<String> optionalTerms; // 선택 약관 동의 여부 - 선택약관은 어떤 약관인지를 구분할 수 있어야 함
}
