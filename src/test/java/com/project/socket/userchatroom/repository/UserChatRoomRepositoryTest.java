package com.project.socket.userchatroom.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.project.socket.chatroom.model.ChatRoom;
import com.project.socket.common.annotation.CustomDataJpaTest;
import com.project.socket.user.model.User;
import com.project.socket.userchatroom.model.UserChatRoom;
import com.project.socket.userchatroom.model.UserChatRoomStatus;
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
                                            .userChatRoomStatus(UserChatRoomStatus.ENTER)
                                            .build();
    UserChatRoom savedUserChatRoom = userChatRoomRepository.save(userChatRoom);

    assertThat(savedUserChatRoom.getUserChatRoomId()).isNotNull();
  }

  @Test
  void 조건에_해당하는_데이터가_없으면_null을_반환한다() {
    UserChatRoom userChatRoom = userChatRoomRepository.findByUserIdAndPostId(1L, 1L);
    assertThat(userChatRoom).isNull();
  }

  @Test
  @Sql("findByUserIdAndPostId.sql")
  void 조건에_해당하는_데이터를_반환한다() {
    UserChatRoom userChatRoom = userChatRoomRepository.findByUserIdAndPostId(1L, 1L);
    assertThat(userChatRoom).isNotNull();
  }

  @Test
  @Sql("updateUnreadCount.sql")
  void 안읽음_개수를_증가시킨다 () {
    userChatRoomRepository.updateUnreadCount(1L, 1L);

    UserChatRoom userChatRoom = userChatRoomRepository.findById(1L).get();
    assertThat(userChatRoom.getUnreadCount()).isEqualTo(1);
  }
}