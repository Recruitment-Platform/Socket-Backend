package com.project.socket.config.handler;

import static org.assertj.core.api.Assertions.assertThat;

import com.project.socket.common.error.ErrorCode;
import com.project.socket.security.exception.InvalidJwtException;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageDeliveryException;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;

@DisplayNameGeneration(ReplaceUnderscores.class)
class StompErrorHandlerTest {

  StompErrorHandler stompErrorHandler = new StompErrorHandler();

  @Test
  void 토큰_인증_예외가_발생하면_예외를_재정의하고_위임시킨다() {
    MessageDeliveryException exception = new MessageDeliveryException(null,
        new InvalidJwtException("invalid token", ErrorCode.INVALID_JWT));

    Message<byte[]> message = stompErrorHandler.handleClientMessageProcessingError(null,
        exception);

    StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

    assertThat(accessor.getMessage()).isEqualTo(ErrorCode.INVALID_JWT.getMessage());
  }

  @Test
  void 에러_메세지_처리를_위임시킨다() {
    MessageDeliveryException exception = new MessageDeliveryException("exception");

    Message<byte[]> message = stompErrorHandler.handleClientMessageProcessingError(null,
        exception);

    StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

    assertThat(accessor.getMessage()).isEqualTo(exception.getMessage());
  }
}