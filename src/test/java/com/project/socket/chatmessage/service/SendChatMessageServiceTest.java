package com.project.socket.chatmessage.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import com.project.socket.chatmessage.model.ChatMessage;
import com.project.socket.chatmessage.repository.ChatMessageRepository;
import com.project.socket.chatmessage.service.usecase.SendChatMessageCommand;
import com.project.socket.chatroom.exception.ChatRoomNotFoundException;
import com.project.socket.chatroom.model.ChatRoom;
import com.project.socket.chatroom.repository.ChatRoomRepository;
import com.project.socket.user.exception.UserNotFoundException;
import com.project.socket.user.model.User;
import com.project.socket.user.repository.UserJpaRepository;
import java.util.Optional;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(ReplaceUnderscores.class)
class SendChatMessageServiceTest {

  @Mock
  UserJpaRepository userJpaRepository;

  @Mock
  ChatRoomRepository chatRoomRepository;

  @Mock
  ChatMessageRepository chatMessageRepository;

  @InjectMocks
  SendChatMessageService sendChatMessageService;

  SendChatMessageCommand command = new SendChatMessageCommand(1L, 1L, 2L, "HI");

  @Test
  void id에_해당하는_chatroom이_없으면_ChatRoomNotFoundException_예외가_발생한다() {
    when(chatRoomRepository.findById(anyLong())).thenReturn(Optional.empty());
    assertThatThrownBy(() -> sendChatMessageService.sendChatMessage(command))
        .isInstanceOf(ChatRoomNotFoundException.class);
  }

  @Test
  void id에_해당하는_sender가_없으면_UserNotFoundException_예외가_발생한다() {
    ChatRoom chatRoom = ChatRoom.builder().chatRoomId(1L).build();
    when(chatRoomRepository.findById(anyLong())).thenReturn(Optional.of(chatRoom));
    when(userJpaRepository.findById(anyLong())).thenReturn(Optional.empty());
    assertThatThrownBy(() -> sendChatMessageService.sendChatMessage(command))
        .isInstanceOf(UserNotFoundException.class);
  }

  @Test
  void 성공적으로_ChatMessage를_저장하고_반환한다() {
    ChatRoom chatRoom = ChatRoom.builder().chatRoomId(1L).build();
    User sender = User.builder().userId(1L).build();
    ChatMessage chatMessage = ChatMessage.builder().chatMessageId(1L).chatRoom(chatRoom)
                                         .sender(sender)
                                         .build();
    when(chatRoomRepository.findById(anyLong())).thenReturn(Optional.of(chatRoom));
    when(userJpaRepository.findById(anyLong())).thenReturn(Optional.of(sender));
    when(chatMessageRepository.save(any())).thenReturn(chatMessage);

    ChatMessage savedChatMessage = sendChatMessageService.sendChatMessage(command);

    assertThat(savedChatMessage.getChatMessageId()).isEqualTo(chatMessage.getChatMessageId());
  }
}