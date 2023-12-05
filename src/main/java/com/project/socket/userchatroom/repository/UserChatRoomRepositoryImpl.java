package com.project.socket.userchatroom.repository;

import static com.project.socket.chatroom.model.QChatRoom.chatRoom;
import static com.project.socket.userchatroom.model.QUserChatRoom.userChatRoom;

import com.project.socket.userchatroom.model.UserChatRoom;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UserChatRoomRepositoryImpl implements UserChatRoomRepositoryCustom {

  private final JPAQueryFactory jpaQueryFactory;

  @Override
  public UserChatRoom findByUserIdAndPostId(Long userId, Long postId) {
    return jpaQueryFactory.selectFrom(userChatRoom)
                          .innerJoin(userChatRoom.chatRoom, chatRoom)
                          .where(userChatRoom.user.userId.eq(userId).and(
                              chatRoom.post.id.eq(postId)))
                          .fetchOne();
  }
}
