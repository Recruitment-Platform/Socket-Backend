package com.project.socket.chatmessage.service.usecase;

public record SendChatMessageCommand(
    Long chatRoomId,
    Long senderId,
    Long receiverId,
    String content
) {

}
