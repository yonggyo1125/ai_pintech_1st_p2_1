package org.koreait.message.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles({"default", "test"})
public class MessageInfoServiceTest {

    @Autowired
    private MessageInfoService infoService;
}
