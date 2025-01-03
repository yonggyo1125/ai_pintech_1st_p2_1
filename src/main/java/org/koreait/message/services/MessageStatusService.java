package org.koreait.message.services;

import lombok.RequiredArgsConstructor;
import org.koreait.message.constants.MessageStatus;
import org.koreait.message.entities.Message;
import org.koreait.message.repositories.MessageRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Lazy
@Service
@RequiredArgsConstructor
public class MessageStatusService {

    private final MessageInfoService infoService;
    private final MessageRepository repository;

    public void change(Long seq) {
        Message item = infoService.get(seq);
        if (item.isReceived()) { // 수신한 메세지만 열람 상태로 변경
            item.setStatus(MessageStatus.READ);
        }

        repository.saveAndFlush(item);
    }
}
