package com.project.socket.chatmessage.service;

import com.project.socket.chatmessage.repository.ChatMessageRepository;
import com.project.socket.chatmessage.service.usecase.SendEnterMessageCommand;
import com.project.socket.chatmessage.service.usecase.SendEnterMessageUseCase;
import com.project.socket.userchatroom.exception.UserChatRoomNotFoundException;
import com.project.socket.userchatroom.exception.UserChatRoomNotSameUserException;
import com.project.socket.userchatroom.model.UserChatRoom;
import com.project.socket.userchatroom.repository.UserChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SendEnterMessageService implements SendEnterMessageUseCase {

  private final UserChatRoomRepository userChatRoomRepository;
  private final ChatMessageRepository chatMessageRepository;

  @Transactional
  @Override
  public Long apply(SendEnterMessageCommand command) {
    UserChatRoom userChatRoom = findUserChatRoom(command.userChatRoomId());

    // 잘못된 요청일 경우 예외 발생
    if (!userChatRoom.isSameUser(command.userId())) {
      throw new UserChatRoomNotSameUserException();
    }
    // 채팅방에 입장하면 UserChatRoom, ChatMessage 의 unreadCount 0으로 감소
    userChatRoom.enter();
    Long chatRoomId = userChatRoom.getChatRoom().getChatRoomId();
    chatMessageRepository.updateAllUnreadMessages(chatRoomId, command.userId());

    return chatRoomId;
  }

  private UserChatRoom findUserChatRoom(Long userChatRoomId) {
    return userChatRoomRepository.findById(userChatRoomId)
                                 .orElseThrow(UserChatRoomNotFoundException::new);
  }
}
