package com.project.socket.chatmessage.controller;

import com.project.socket.chatmessage.controller.dto.request.SendChatMessageRequest;
import com.project.socket.chatmessage.controller.dto.response.SendChatMessageResponse;
import com.project.socket.chatmessage.model.ChatMessage;
import com.project.socket.chatmessage.service.usecase.SendChatMessageUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@RequiredArgsConstructor
@Controller
public class SendChatMessageController {

  private final SimpMessagingTemplate simpMessagingTemplate;
  private final SendChatMessageUseCase sendChatMessageUseCase;

  @MessageMapping("/rooms/{roomId}")
  public void sendMessage(@DestinationVariable Long roomId,
      SendChatMessageRequest sendChatMessageRequest) {
    ChatMessage chatMessage = sendChatMessageUseCase.sendChatMessage(
        sendChatMessageRequest.toCommand(roomId));
    SendChatMessageResponse response = SendChatMessageResponse.from(chatMessage);
    simpMessagingTemplate.convertAndSend("/sub/rooms/" + roomId, response);
  }
}
