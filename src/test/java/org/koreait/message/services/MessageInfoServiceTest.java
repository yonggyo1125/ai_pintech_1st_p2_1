package org.koreait.message.services;

import org.koreait.member.entities.Member;
import org.koreait.member.repositories.MemberRepository;
import org.koreait.member.services.MemberUpdateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles({"default", "test"})
public class MessageInfoServiceTest {

    @Autowired
    private MessageInfoService infoService;

    @Autowired
    private MessageSendService sendService;

    @Autowired
    private MemberUpdateService updateService;

    @Autowired
    private MemberRepository memberRepository;

    private Member sender;
    private Member receiver;
}
