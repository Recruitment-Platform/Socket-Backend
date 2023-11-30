package com.project.socket.chatmessage.controller.dto.request;

import com.project.socket.chatmessage.service.usecase.SendChatMessageCommand;

public record SendChatMessageRequest(
    Long receiverId,
    Long senderId,
    String content
) {

  public SendChatMessageCommand toCommand(Long chatRoomId) {
    return new SendChatMessageCommand(chatRoomId, senderId, receiverId, content);
  }
}
