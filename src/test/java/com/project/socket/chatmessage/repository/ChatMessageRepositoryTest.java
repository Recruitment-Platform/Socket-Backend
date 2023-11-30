package com.project.socket.chatmessage.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.project.socket.chatmessage.model.ChatMessage;
import com.project.socket.chatroom.model.ChatRoom;
import com.project.socket.common.annotation.CustomDataJpaTest;
import com.project.socket.user.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

@CustomDataJpaTest
class ChatMessageRepositoryTest {

  @Autowired
  ChatMessageRepository chatMessageRepository;

  @Test
  @Sql("chatMessageTest.sql")
  void ChatMessage_엔티티를_저장하고_반환한다(){
    ChatMessage chatMessage = ChatMessage.builder().sender(User.builder().userId(1L).build())
                                .chatRoom(ChatRoom.builder().chatRoomId(1L).build()).content("hi")
                                .build();

    ChatMessage savedChatMessage = chatMessageRepository.save(chatMessage);

    assertThat(savedChatMessage.getChatMessageId()).isNotNull();
  }
}