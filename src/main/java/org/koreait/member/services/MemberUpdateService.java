package org.koreait.member.services;

import org.koreait.member.controllers.RequestJoin;
import org.springframework.stereotype.Service;

@Service
public class MemberUpdateService {

    /**
     * 커맨드 객체의 타입에 따라서 RequestJoin이면 회원 가입 처리
     *                      RequestProfile이면 회원정보 수정 처리
     * @param form
     */
    public void process(RequestJoin form) {

    }

    /**
     * 회원정보 추가 또는 수정 처리
     *
     */
    private void save() {

    }

}
