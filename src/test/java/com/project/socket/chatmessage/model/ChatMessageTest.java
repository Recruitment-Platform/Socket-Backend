package com.project.socket.chatmessage.model;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(ReplaceUnderscores.class)
class ChatMessageTest {

  @Test
  void 엔티티_빌더_테스트(){
    ChatMessage chatMessage = ChatMessage.builder().chatMessageId(1L).build();
    assertThat(chatMessage.getChatMessageId()).isEqualTo(1L);
  }
}