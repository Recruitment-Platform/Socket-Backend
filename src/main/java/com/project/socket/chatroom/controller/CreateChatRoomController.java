package com.project.socket.chatroom.controller;

import com.project.socket.chatroom.controller.dto.response.CreateChatRoomResponse;
import com.project.socket.chatroom.model.ChatRoom;
import com.project.socket.chatroom.service.usecase.CreateChatRoomCommand;
import com.project.socket.chatroom.service.usecase.CreateChatRoomUseCase;
import jakarta.validation.constraints.Min;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequiredArgsConstructor
@Validated
public class CreateChatRoomController {

  private final CreateChatRoomUseCase createChatRoomUseCase;

  @PostMapping("/posts/{postId}/chat-rooms")
  public ResponseEntity<CreateChatRoomResponse> createChatroom(
      @AuthenticationPrincipal UserDetails userDetails,
      @PathVariable @Min(1) Long postId) {
    Long userId = Long.valueOf(userDetails.getUsername());
    ChatRoom chatRoom = createChatRoomUseCase.apply(new CreateChatRoomCommand(userId, postId));

    URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                                              .path("/{id}")
                                              .buildAndExpand(chatRoom.getChatRoomId())
                                              .toUri();

    CreateChatRoomResponse response = new CreateChatRoomResponse(chatRoom.getChatRoomId());
    return ResponseEntity.created(location).body(response);
  }
}
