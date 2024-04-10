package com.project.socket.userchatroom.exception;

import com.project.socket.common.error.ErrorCode;
import com.project.socket.common.error.exception.BusinessException;

public class UserChatRoomNotFoundException extends BusinessException {

  public UserChatRoomNotFoundException() {
    super(ErrorCode.USER_CHAT_ROOM_NOT_FOUND);
  }
}
