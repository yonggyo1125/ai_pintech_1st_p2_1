package org.koreait.message.services;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.StringExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.koreait.file.services.FileInfoService;
import org.koreait.global.libs.Utils;
import org.koreait.global.paging.ListData;
import org.koreait.global.paging.Pagination;
import org.koreait.member.entities.Member;
import org.koreait.member.libs.MemberUtil;
import org.koreait.message.constants.MessageStatus;
import org.koreait.message.controllers.MessageSearch;
import org.koreait.message.entities.Message;
import org.koreait.message.entities.QMessage;
import org.koreait.message.exceptions.MessageNotFoundException;
import org.koreait.message.repositories.MessageRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Lazy
@Service
@RequiredArgsConstructor
public class MessageInfoService {

    private final FileInfoService fileInfoService;
    private final MessageRepository messageRepository;
    private final JPAQueryFactory queryFactory;
    private final HttpServletRequest request;
    private final MemberUtil memberUtil;
    private final Utils utils;

    /**
     * 쪽지 하나 조회
     * 
     * @param seq
     * @return
     */
    public Message get(Long seq) {
        BooleanBuilder builder = new BooleanBuilder();
        BooleanBuilder orBuilder = new BooleanBuilder();
        QMessage message = QMessage.message;
        builder.and(message.seq.eq(seq));

        if (!memberUtil.isAdmin()) {
            Member member = memberUtil.getMember();
            BooleanBuilder orBuilder2 = new BooleanBuilder();
            BooleanBuilder andBuilder = new BooleanBuilder();

            orBuilder2.or(andBuilder.and(message.notice.eq(true)).and(message.receiver.isNull()))
                            .or(message.receiver.eq(member));

            orBuilder.or(message.sender.eq(member))
                            .or(orBuilder2);


            builder.and(orBuilder);
        }


        Message item = messageRepository.findOne(builder).orElseThrow(MessageNotFoundException::new);

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

        // 검색 조건 처리 S
        BooleanBuilder andBuilder = new BooleanBuilder();
        QMessage message = QMessage.message;
        String mode = search.getMode();
        Member member = memberUtil.getMember();

        mode = StringUtils.hasText(mode) ? mode : "receive";
        // send - 보낸 쪽지 목록, receive - 받은 쪽지 목록
        if (mode.equals("send")) {
            andBuilder.and(message.sender.eq(member));
        } else {
            BooleanBuilder orBuilder = new BooleanBuilder();
            BooleanBuilder andBuilder1 = new BooleanBuilder();

            orBuilder.or(andBuilder1.and(message.notice.eq(true)).and(message.receiver.isNull())) // 공지쪽지
                    .or(message.receiver.eq(member));

            andBuilder.and(orBuilder);
        }


        andBuilder.and(mode.equals("send") ? message.deletedBySender.eq(false) : message.deletedByReceiver.eq(false));

        // 보낸사람 조건 검색
        List<String> sender = search.getSender();
        if (mode.equals("receive") && sender != null && !sender.isEmpty()) {
            andBuilder.and(message.sender.email.in(sender));
        }

        // 키워드 검색 처리
        String sopt = search.getSopt();
        String skey = search.getSkey();
        sopt = StringUtils.hasText(sopt) ? sopt : "ALL";
        if (StringUtils.hasText(skey)) {
            StringExpression condition = sopt.equals("SUBJECT") ? message.subject : message.subject.concat(message.content);

            andBuilder.and(condition.contains(skey.trim()));
        }

        // 검색 조건 처리 E

        List<Message> items = queryFactory.selectFrom(message)
                .leftJoin(message.receiver)
                .fetchJoin()
                .where(andBuilder)
                .limit(limit)
                .offset(offset)
                .orderBy(message.notice.desc(), message.createdAt.desc())
                .fetch();

        items.forEach(this::addInfo); // 추가 정보 처리

        long total = messageRepository.count(andBuilder);
        Pagination pagination = new Pagination(page, (int)total, utils.isMobile() ? 5:10, limit, request);

        return new ListData<>(items, pagination);
    }

    /**
     * 추가 정보 처리
     *
     * @param item
     */
    private void addInfo(Message item) {
        String gid = item.getGid();
        item.setEditorImages(fileInfoService.getList(gid, "editor"));
        item.setAttachFiles(fileInfoService.getList(gid, "attach"));

        Member member = memberUtil.getMember();
        item.setReceived(
                (item.isNotice() && item.getReceiver() == null) ||
                item.getReceiver().getSeq().equals(member.getSeq())
        );

        // 삭제 가능 여부
        boolean deletable = (item.isNotice() && memberUtil.isAdmin())
                || (!item.isNotice() && (item.getSender().getSeq().equals(member.getSeq()) || item.getReceiver().getSeq().equals(member.getSeq())));
        item.setDeletable(deletable);
    }

    /**
     * 미열람 메세지 갯수
     *
     * @return
     */
    public long totalUnRead(String email) {
        BooleanBuilder andBuilder = new BooleanBuilder();
        QMessage message = QMessage.message;
        andBuilder.and(message.receiver.email.eq(email))
                .and(message.status.eq(MessageStatus.UNREAD));

        return messageRepository.count(andBuilder);
    }

    public long totalUnRead() {
        Member member = memberUtil.getMember();

        return totalUnRead(member.getEmail());
    }
}
