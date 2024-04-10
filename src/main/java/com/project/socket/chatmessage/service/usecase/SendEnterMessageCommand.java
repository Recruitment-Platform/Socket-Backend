package com.project.socket.chatmessage.service.usecase;

public record SendEnterMessageCommand(
    Long userId,
    Long userChatRoomId
) {

}
