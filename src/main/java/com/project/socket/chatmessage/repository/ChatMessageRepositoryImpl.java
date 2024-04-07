package com.project.socket.chatmessage.repository;

import static com.project.socket.chatmessage.model.QChatMessage.chatMessage;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
public class ChatMessageRepositoryImpl implements ChatMessageRepositoryCustom {

  private final JPAQueryFactory jpaQueryFactory;

  @Override
  public long updateAllUnreadMessages(Long chatRoomId, Long userId) {
    return jpaQueryFactory.update(chatMessage)
                          .set(chatMessage.readCount, 0)
                          .where(chatMessage.chatRoom.chatRoomId.eq(chatRoomId).and(
                              chatMessage.sender.userId.ne(userId)))
                          .execute();
  }
}
