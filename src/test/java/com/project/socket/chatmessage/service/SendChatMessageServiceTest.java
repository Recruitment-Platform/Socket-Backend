package com.project.socket.chatmessage.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.project.socket.chatmessage.model.ChatMessage;
import com.project.socket.chatmessage.repository.ChatMessageRepository;
import com.project.socket.chatmessage.service.usecase.SendChatMessageCommand;
import com.project.socket.chatroom.exception.ChatRoomNotFoundException;
import com.project.socket.chatroom.model.ChatRoom;
import com.project.socket.chatroom.repository.ChatRoomRepository;
import com.project.socket.chatuser.repository.ChatUserRepository;
import com.project.socket.user.exception.UserNotFoundException;
import com.project.socket.user.model.User;
import com.project.socket.user.repository.UserJpaRepository;
import java.util.Optional;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
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

  @Mock
  ChatUserRepository chatUserRepository;

  @InjectMocks
  SendChatMessageService sendChatMessageService;

  @Captor
  ArgumentCaptor<ChatMessage> chatMessageCaptor;

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
  void 상대방이_접속중이면_ChatMessage를_읽음처리_저장하고_반환한다() {
    ChatRoom chatRoom = ChatRoom.builder().chatRoomId(1L).build();
    User sender = User.builder().userId(1L).build();
    ChatMessage chatMessage = ChatMessage.builder().chatMessageId(1L).chatRoom(chatRoom)
                                         .sender(sender)
                                         .build();

    String receiverDestination = "/sub/rooms/1";
    when(chatRoomRepository.findById(anyLong())).thenReturn(Optional.of(chatRoom));
    when(userJpaRepository.findById(anyLong())).thenReturn(Optional.of(sender));
    when(chatUserRepository.findDestinationById(anyLong())).thenReturn(receiverDestination);
    when(chatMessageRepository.save(any())).thenReturn(chatMessage);

    ChatMessage savedChatMessage = sendChatMessageService.sendChatMessage(command);

    verify(chatMessageRepository).save(chatMessageCaptor.capture());
    ChatMessage captureChatMessage = chatMessageCaptor.getValue();

    assertAll(
        () -> assertThat(savedChatMessage.getChatMessageId()).isEqualTo(
            chatMessage.getChatMessageId()),
        () -> assertThat(captureChatMessage.getReadCount()).isZero()
    );
  }

  @Test
  void 상대방이_다른_채팅방에_접속중이면_ChatMessage를_안읽음처리_저장하고_반환한다() {
    ChatRoom chatRoom = ChatRoom.builder().chatRoomId(1L).build();
    User sender = User.builder().userId(1L).build();
    ChatMessage chatMessage = ChatMessage.builder().chatMessageId(1L).chatRoom(chatRoom)
                                         .sender(sender)
                                         .build();

    String receiverDestination = "/sub/rooms/2";
    when(chatRoomRepository.findById(anyLong())).thenReturn(Optional.of(chatRoom));
    when(userJpaRepository.findById(anyLong())).thenReturn(Optional.of(sender));
    when(chatUserRepository.findDestinationById(anyLong())).thenReturn(receiverDestination);
    when(chatMessageRepository.save(any())).thenReturn(chatMessage);

    ChatMessage savedChatMessage = sendChatMessageService.sendChatMessage(command);

    verify(chatMessageRepository).save(chatMessageCaptor.capture());
    ChatMessage captureChatMessage = chatMessageCaptor.getValue();

    assertAll(
        () -> assertThat(savedChatMessage.getChatMessageId()).isEqualTo(
            chatMessage.getChatMessageId()),
        () -> assertThat(captureChatMessage.getReadCount()).isEqualTo(1)
    );
  }

  @Test
  void 상대방이_접속중이_아니면_ChatMessage를_안읽음처리_저장하고_반환한다() {
    ChatRoom chatRoom = ChatRoom.builder().chatRoomId(1L).build();
    User sender = User.builder().userId(1L).build();
    ChatMessage chatMessage = ChatMessage.builder().chatMessageId(1L).chatRoom(chatRoom)
                                         .sender(sender)
                                         .build();

    when(chatRoomRepository.findById(anyLong())).thenReturn(Optional.of(chatRoom));
    when(userJpaRepository.findById(anyLong())).thenReturn(Optional.of(sender));
    when(chatUserRepository.findDestinationById(anyLong())).thenReturn(null);
    when(chatMessageRepository.save(any())).thenReturn(chatMessage);

    ChatMessage savedChatMessage = sendChatMessageService.sendChatMessage(command);

    verify(chatMessageRepository).save(chatMessageCaptor.capture());
    ChatMessage captureChatMessage = chatMessageCaptor.getValue();

    assertAll(
        () -> assertThat(savedChatMessage.getChatMessageId()).isEqualTo(
            chatMessage.getChatMessageId()),
        () -> assertThat(captureChatMessage.getReadCount()).isEqualTo(1)
    );
  }
}