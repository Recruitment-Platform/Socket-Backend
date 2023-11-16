package com.project.socket.chatroom.model;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(ReplaceUnderscores.class)
class ChatRoomTest {

  @Test
  void 엔티티_빌더_테스트(){
    ChatRoom chatRoom = ChatRoom.builder().chatRoomId(1L).build();
    assertThat(chatRoom.getChatRoomId()).isEqualTo(1L);
  }
}