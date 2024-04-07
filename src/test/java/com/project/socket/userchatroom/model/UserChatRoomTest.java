package com.project.socket.userchatroom.model;

import static org.assertj.core.api.Assertions.assertThat;

import com.project.socket.user.model.User;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(ReplaceUnderscores.class)
class UserChatRoomTest {

  @Test
  void 엔티티_빌더_테스트() {
    UserChatRoom userChatroom = UserChatRoom.builder().userChatRoomId(1L).build();
    assertThat(userChatroom.getUserChatRoomId()).isEqualTo(1L);
  }

  @Test
  void userId가_같으면_true를_반환한다() {
    UserChatRoom userChatroom = UserChatRoom.builder()
                                            .userChatRoomId(1L)
                                            .user(User.builder().userId(1L).build())
                                            .build();

    boolean result = userChatroom.isSameUser(1L);

    assertThat(result).isTrue();
  }

  @Test
  void userId가_다르면_false를_반환한다() {
    UserChatRoom userChatroom = UserChatRoom.builder()
                                            .userChatRoomId(1L)
                                            .user(User.builder().userId(1L).build())
                                            .build();

    boolean result = userChatroom.isSameUser(2L);

    assertThat(result).isFalse();
  }
}