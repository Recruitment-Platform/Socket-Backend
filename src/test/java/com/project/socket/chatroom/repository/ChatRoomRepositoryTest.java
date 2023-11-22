package com.project.socket.chatroom.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.project.socket.chatroom.model.ChatRoom;
import com.project.socket.common.annotation.CustomDataJpaTest;
import com.project.socket.post.model.Post;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

@CustomDataJpaTest
class ChatRoomRepositoryTest {

  @Autowired
  ChatRoomRepository chatRoomRepository;

  @Test
  @Sql("saveChatRoom.sql")
  void ChatRoom_엔티티를_저장하고_반환한다(){
    ChatRoom chatRoom = ChatRoom.builder().post(Post.builder().id(1L).build()).build();
    ChatRoom savedChatRoom = chatRoomRepository.save(chatRoom);

    assertThat(savedChatRoom.getChatRoomId()).isEqualTo(1L);
  }
}