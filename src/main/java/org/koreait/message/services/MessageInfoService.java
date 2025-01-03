package org.koreait.message.services;

import lombok.RequiredArgsConstructor;
import org.koreait.global.paging.ListData;
import org.koreait.message.controllers.MessageSearch;
import org.koreait.message.entities.Message;
import org.koreait.message.repositories.MessageRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Lazy
@Service
@RequiredArgsConstructor
public class MessageInfoService {

    private final MessageRepository messageRepository;

    /**
     * 쪽지 하나 조회
     * 
     * @param seq
     * @return
     */
    public Message get(Long seq) {

        Message item = messageRepository.findById(seq).orElseThrow();

        return null;
    }

    /**
     * 쪽지 목록 조회 
     * 
     * @param search
     * @return
     */
    public ListData<Message> getList(MessageSearch search) {

        return null;
    }

    /**
     * 추가 정보 처리
     *
     * @param item
     */
    private void addInfo(Message item) {

    }
}
