package org.koreait.message.services;

import lombok.RequiredArgsConstructor;
import org.koreait.file.services.FileDeleteService;
import org.koreait.global.exceptions.UnAuthorizedException;
import org.koreait.member.libs.MemberUtil;
import org.koreait.message.entities.Message;
import org.koreait.message.repositories.MessageRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Lazy
@Service
@RequiredArgsConstructor
public class MessageDeleteService {
    private final MessageInfoService infoService;
    private final MessageRepository repository;
    private final FileDeleteService fileDeleteService;
    private final MemberUtil memberUtil;

    /**
     * 삭제 처리
     * 0. 공지인 경우는 관리자인 경우만 삭제
     * 1. sender 쪽에서 삭제하는 경우 / mode - send
     *      deletedBySender 값을 true
     * 2. receiver 쪽에서 삭제 하는 경우 / mode - receive
     *      deletedByReceiver 값을 true
     * 3. deletedBySender와 deletedByReceiver가 모두 true인 경우 실제 DB에서도 삭제(Message 쪽 삭제, 파일 데이터 함께 삭제)
     *
     *
     * @param seq
     */
    public void process(Long seq, String mode) {
        mode = StringUtils.hasText(mode) ? mode : "receive";

        boolean isProceedDelete = false;
        Message item = infoService.get(seq);
        if (item.isNotice()) {
            if (memberUtil.isAdmin()) { // 삭제 처리
                isProceedDelete = true;
            } else { // 공지이지만 관리자가 아닌 경우 - 권한 없음
                throw new UnAuthorizedException();
            }
        } // endif

        if (mode.equals("send")) { // 보낸 쪽 
            item.setDeletedBySender(true);
        } else { // 받는 쪽
            item.setDeletedByReceiver(true);
        }

        if (item.isDeletedBySender() && item.isDeletedByReceiver()) {
            isProceedDelete = true; // 보낸쪽, 받는쪽 모두 삭제 한 경우 -> DB에서 삭제
        }

        // 삭제 진행이 필요한 경우 처리
        if (isProceedDelete) {
            String gid = item.getGid();

            // DB에서 삭제
            repository.delete(item);
            repository.flush();

            // 파일 삭제
            fileDeleteService.deletes(gid);
        } else { // 보내는쪽 또는 받는 쪽 한군데서만 삭제 처리를 한 경우
            repository.saveAndFlush(item);
        }
    }
}
