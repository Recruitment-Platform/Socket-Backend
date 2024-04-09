package com.project.socket.userchatroom.exception;

import com.project.socket.common.error.ErrorCode;
import com.project.socket.common.error.exception.BusinessException;

public class UserChatRoomNotSameUserException extends BusinessException {

  public UserChatRoomNotSameUserException() {
    super(ErrorCode.USER_CHAT_ROOM_NOT_SAME_USER);
  }
}
