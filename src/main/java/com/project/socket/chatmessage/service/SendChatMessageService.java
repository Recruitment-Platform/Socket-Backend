package com.project.socket.chatmessage.service;

import com.project.socket.chatmessage.model.ChatMessage;
import com.project.socket.chatmessage.repository.ChatMessageRepository;
import com.project.socket.chatmessage.service.usecase.SendChatMessageCommand;
import com.project.socket.chatmessage.service.usecase.SendChatMessageUseCase;
import com.project.socket.chatroom.exception.ChatRoomNotFoundException;
import com.project.socket.chatroom.model.ChatRoom;
import com.project.socket.chatroom.repository.ChatRoomRepository;
import com.project.socket.user.exception.UserNotFoundException;
import com.project.socket.user.model.User;
import com.project.socket.user.repository.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SendChatMessageService implements SendChatMessageUseCase {

  private final ChatMessageRepository chatMessageRepository;
  private final ChatRoomRepository chatRoomRepository;
  private final UserJpaRepository userJpaRepository;

  @Override
  public ChatMessage sendChatMessage(SendChatMessageCommand command) {
    ChatRoom chatRoom = findChatRoom(command.chatRoomId());
    User sender = findSender(command.senderId());

    ChatMessage chatMessage = ChatMessage.builder().chatRoom(chatRoom).readCount(1)
                                         .content(command.content()).sender(sender)
                                         .build();

    return chatMessageRepository.save(chatMessage);
  }

  private ChatRoom findChatRoom(Long chatRoomId) {
    return chatRoomRepository.findById(chatRoomId)
                             .orElseThrow(() -> new ChatRoomNotFoundException(chatRoomId));
  }

  private User findSender(Long senderId) {
    return userJpaRepository.findById(senderId)
                            .orElseThrow(() -> new UserNotFoundException(senderId));
  }
}
