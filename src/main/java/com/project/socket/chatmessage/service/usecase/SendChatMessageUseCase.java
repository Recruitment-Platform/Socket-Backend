package com.project.socket.chatmessage.service.usecase;

import com.project.socket.chatmessage.model.ChatMessage;

public interface SendChatMessageUseCase {

  ChatMessage sendChatMessage(SendChatMessageCommand command);
}
