package com.project.socket.chatmessage.controller;

import com.project.socket.chatmessage.controller.dto.response.SendEnterMessageResponse;
import com.project.socket.chatmessage.service.usecase.SendEnterMessageCommand;
import com.project.socket.chatmessage.service.usecase.SendEnterMessageUseCase;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@RequiredArgsConstructor
@Controller
public class SendEnterMessageController {

  private final SendEnterMessageUseCase sendEnterMessageUseCase;
  private final SimpMessagingTemplate simpMessagingTemplate;

  @MessageMapping("/enter/{userChatRoomId}")
  public void sendMessage(@DestinationVariable Long userChatRoomId, Principal principal) {
    Long userId = Long.valueOf(principal.getName());
    Long chatRoomId = sendEnterMessageUseCase.apply(
        new SendEnterMessageCommand(userId, userChatRoomId));
    SendEnterMessageResponse response = SendEnterMessageResponse.from(userId);

    simpMessagingTemplate.convertAndSend("/sub/rooms/" + chatRoomId, response);
  }
}
