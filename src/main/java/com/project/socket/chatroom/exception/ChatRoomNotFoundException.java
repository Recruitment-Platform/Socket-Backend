package com.project.socket.chatroom.exception;

import static com.project.socket.common.error.ErrorCode.CHAT_ROOM_NOT_FOUND;

import com.project.socket.common.error.exception.BusinessException;

public class ChatRoomNotFoundException extends BusinessException {

  public ChatRoomNotFoundException(Long chatRoomId) {
    super("ChatRoom ID: " + chatRoomId, CHAT_ROOM_NOT_FOUND);
  }
}
