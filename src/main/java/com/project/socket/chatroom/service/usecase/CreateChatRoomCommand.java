package com.project.socket.chatroom.service.usecase;

public record CreateChatRoomCommand(
    Long applicantId,
    Long postId
) {

}
