package com.project.socket.config.handler;

import static com.project.socket.common.error.ErrorCode.INVALID_JWT;

import com.project.socket.security.exception.InvalidJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageDeliveryException;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.StompSubProtocolErrorHandler;

@Component
@Slf4j
public class StompErrorHandler extends StompSubProtocolErrorHandler {

  @Override
  public Message<byte[]> handleClientMessageProcessingError(Message<byte[]> clientMessage,
      Throwable ex) {
    // 토큰 문제로 예외 발생시 예외를 재정의해서 위임
    if (ex.getCause() instanceof InvalidJwtException) {
      log.info(ex.getMessage());
      ex = new MessageDeliveryException(INVALID_JWT.getMessage());
    }

    return super.handleClientMessageProcessingError(clientMessage, ex);
  }
}
