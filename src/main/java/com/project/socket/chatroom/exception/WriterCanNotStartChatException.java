package com.project.socket.chatroom.exception;

import com.project.socket.common.error.ErrorCode;
import com.project.socket.common.error.exception.BusinessException;

public class WriterCanNotStartChatException extends BusinessException {

  public WriterCanNotStartChatException() {
    super(ErrorCode.WRITER_CAN_NOT_START_CHAT);
  }
}
