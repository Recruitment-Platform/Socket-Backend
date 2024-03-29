package com.project.socket.userchatroom.repository;

import static com.project.socket.chatroom.model.QChatRoom.chatRoom;
import static com.project.socket.userchatroom.model.QUserChatRoom.userChatRoom;

import com.project.socket.userchatroom.model.UserChatRoom;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
public class UserChatRoomRepositoryImpl implements UserChatRoomRepositoryCustom {

  private final JPAQueryFactory jpaQueryFactory;

  @Transactional
  @Override
  public UserChatRoom findByUserIdAndPostId(Long userId, Long postId) {
    return jpaQueryFactory.selectFrom(userChatRoom)
                          .innerJoin(userChatRoom.chatRoom, chatRoom)
                          .where(userChatRoom.user.userId.eq(userId).and(
                              chatRoom.post.id.eq(postId)))
                          .fetchOne();
  }

  @Transactional
  @Override
  public void updateUnreadCount(Long userId, Long chatRoomId) {
    jpaQueryFactory.update(userChatRoom)
        .set(userChatRoom.unreadCount, userChatRoom.unreadCount.add(1))
        .where(eqUserId(userId).and(eqChatRoomId(chatRoomId)))
        .execute();
  }

  private BooleanExpression eqChatRoomId(Long chatRoomId) {
    return chatRoomId != null ? userChatRoom.chatRoom.chatRoomId.eq(chatRoomId) : null;
  }

  private BooleanExpression eqUserId(Long userId) {
    return userId != null ? userChatRoom.user.userId.eq(userId) : null;
  }
}
