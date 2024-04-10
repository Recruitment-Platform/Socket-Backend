package com.project.socket.chatmessage.controller.dto.response;

public record SendEnterMessageResponse(
    MessageType messageType,
    Long enterId
) {

  public static SendEnterMessageResponse from(Long userId) {
    return new SendEnterMessageResponse(MessageType.ENTER, userId);
  }
}
