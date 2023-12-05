package com.project.socket.userchatroom.model;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(ReplaceUnderscores.class)
class UserChatRoomTest {

  @Test
  void 엔티티_빌더_테스트(){
    UserChatRoom userChatroom = UserChatRoom.builder().userChatRoomId(1L).build();
    assertThat(userChatroom.getUserChatRoomId()).isEqualTo(1L);
  }
}