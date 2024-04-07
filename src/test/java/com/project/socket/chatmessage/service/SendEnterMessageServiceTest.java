package com.project.socket.chatmessage.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import com.project.socket.chatmessage.repository.ChatMessageRepository;
import com.project.socket.chatmessage.service.usecase.SendEnterMessageCommand;
import com.project.socket.chatroom.model.ChatRoom;
import com.project.socket.user.model.User;
import com.project.socket.userchatroom.exception.UserChatRoomNotFoundException;
import com.project.socket.userchatroom.exception.UserChatRoomNotSameUserException;
import com.project.socket.userchatroom.model.UserChatRoom;
import com.project.socket.userchatroom.model.UserChatRoomStatus;
import com.project.socket.userchatroom.repository.UserChatRoomRepository;
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
class SendEnterMessageServiceTest {

  @Mock
  UserChatRoomRepository userChatRoomRepository;

  @Mock
  ChatMessageRepository chatMessageRepository;

  @InjectMocks
  SendEnterMessageService sendEnterMessageService;

  SendEnterMessageCommand command = new SendEnterMessageCommand(1L, 1L);

  @Test
  void userChatRoom이_존재하지_않으면_UserChatRoomNotFoundException_예외가_발생한다() {
    when(userChatRoomRepository.findById(anyLong())).thenReturn(Optional.empty());

    assertThatThrownBy(() -> sendEnterMessageService.apply(command))
        .isInstanceOf(UserChatRoomNotFoundException.class);
  }

  @Test
  void 요청차_ID와_일치하지_않으면_UserChatRoomNotSameUserException_예외가_발생한다() {
    UserChatRoom userChatRoom = UserChatRoom.builder()
                                            .userChatRoomId(1L)
                                            .user(User.builder().userId(2L).build())
                                            .build();
    when(userChatRoomRepository.findById(anyLong())).thenReturn(Optional.of(userChatRoom));

    assertThatThrownBy(() -> sendEnterMessageService.apply(command))
        .isInstanceOf(UserChatRoomNotSameUserException.class);
  }

  @Test
  void UserChatRoom의_안읽음_개수를_0으로_변경한다() {
    UserChatRoom userChatRoom = UserChatRoom.builder()
                                            .userChatRoomId(1L)
                                            .user(User.builder().userId(1L).build())
                                            .chatRoom(ChatRoom.builder().chatRoomId(1L).build())
                                            .unreadCount((short) 10)
                                            .userChatRoomStatus(UserChatRoomStatus.ENTER)
                                            .build();

    when(userChatRoomRepository.findById(anyLong())).thenReturn(Optional.of(userChatRoom));
    when(chatMessageRepository.updateAllUnreadMessages(anyLong(), anyLong())).thenReturn(1L);

    Long chatRoomId = sendEnterMessageService.apply(command);

    assertAll(
        () -> assertThat(userChatRoom.getUnreadCount()).isEqualTo((short) 0),
        () -> assertThat(chatRoomId).isEqualTo(userChatRoom.getChatRoom().getChatRoomId())
    );
  }
}