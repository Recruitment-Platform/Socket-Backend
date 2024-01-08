package com.project.socket.config.handler;

import com.project.socket.security.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StompInterceptor implements ChannelInterceptor {

  private final JwtProvider jwtProvider;

  @Override
  public Message<?> preSend(Message<?> message, MessageChannel channel) {
    StompHeaderAccessor accessor =
        MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
    SimpMessageType messageType = accessor.getMessageType();

    // Connect 연결 요청이면 InboundChannel에 메세지를 보내기전에 user 인증객체를 등록
    if (messageType.equals(SimpMessageType.CONNECT)) {
      String token = accessor.getFirstNativeHeader("Authorization");
      jwtProvider.validateToken(token);
      UserDetails userDetails = getUserDetails(jwtProvider.getSubjectFromToken(token),
          jwtProvider.getRoleFromToken(token));
      Authentication authentication = getAuthentication(userDetails);
      accessor.setUser(authentication);
    }

    return message;
  }

  private Authentication getAuthentication(UserDetails userDetails) {
    return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
  }

  private UserDetails getUserDetails(String userId, String role) {
    return User.builder().username(userId).password("").authorities(role).build();
  }
}
