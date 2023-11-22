package com.project.socket.userchatroom.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.project.socket.chatroom.model.ChatRoom;
import com.project.socket.common.annotation.CustomDataJpaTest;
import com.project.socket.user.model.User;
import com.project.socket.userchatroom.model.UserChatRoom;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

@CustomDataJpaTest
class UserChatRoomRepositoryTest {

  @Autowired
  UserChatRoomRepository userChatRoomRepository;

  @Test
  @Sql("saveUserChatRoom.sql")
  void UserChatRoom_엔티티를_저장하고_반환한다() {
    UserChatRoom userChatRoom = UserChatRoom.builder()
                                            .user(User.builder().userId(1L).build())
                                            .chatRoom(ChatRoom.builder().chatRoomId(1L).build())
                                            .build();
    UserChatRoom savedUserChatRoom = userChatRoomRepository.save(userChatRoom);

    assertThat(savedUserChatRoom.getUserChatRoomId()).isEqualTo(1L);
  }
}