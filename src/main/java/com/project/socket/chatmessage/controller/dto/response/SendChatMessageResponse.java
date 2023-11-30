package com.project.socket.chatmessage.controller.dto.response;

import com.project.socket.chatmessage.model.ChatMessage;
import java.time.LocalDateTime;

public record SendChatMessageResponse(
    Long chatMessageId,
    String content,
    Long senderId,
    LocalDateTime createdAt,
    Long chatRoomId,
    int readCount
) {

  public static SendChatMessageResponse from(ChatMessage chatMessage) {
    return new SendChatMessageResponse(chatMessage.getChatMessageId(), chatMessage.getContent(),
        chatMessage.getSender().getUserId(), chatMessage.getCreatedAt(),
        chatMessage.getChatRoom().getChatRoomId(), chatMessage.getReadCount());
  }
}
