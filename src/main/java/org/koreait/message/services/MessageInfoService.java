package org.koreait.message.services;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.koreait.global.paging.ListData;
import org.koreait.message.controllers.MessageSearch;
import org.koreait.message.entities.Message;
import org.koreait.message.entities.QMessage;
import org.koreait.message.exceptions.MessageNotFoundException;
import org.koreait.message.repositories.MessageRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;

@Lazy
@Service
@RequiredArgsConstructor
public class MessageInfoService {

    private final MessageRepository messageRepository;
    private final JPAQueryFactory queryFactory;

    /**
     * 쪽지 하나 조회
     * 
     * @param seq
     * @return
     */
    public Message get(Long seq) {

        Message item = messageRepository.findById(seq).orElseThrow(MessageNotFoundException::new);

        addInfo(item); // 추가 정보 처리

        return item;
    }

    /**
     * 쪽지 목록 조회 
     * 
     * @param search
     * @return
     */
    public ListData<Message> getList(MessageSearch search) {
        int page = Math.max(search.getPage(), 1);
        int limit = search.getLimit();
        limit = limit < 1 ? 20 : limit;
        int offset = (page - 1) * limit;

        BooleanBuilder andBuilder = new BooleanBuilder();
        QMessage message = QMessage.message;

        List<Message> items = queryFactory.selectFrom(message)
                .leftJoin(message.receiver)
                .fetchJoin()
                .where(andBuilder)
                .limit(limit)
                .offset(offset)
                .orderBy(message.createdAt.desc())
                .fetch();

        items.forEach(this::addInfo); // 추가 정보 처리

        long total = messageRepository.count(andBuilder);



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
