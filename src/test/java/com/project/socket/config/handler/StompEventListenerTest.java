package com.project.socket.config.handler;

import static com.project.socket.user.model.Role.ROLE_USER;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.project.socket.chatuser.repository.ChatUserRepository;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;
import org.springframework.web.socket.messaging.SessionUnsubscribeEvent;

@DisplayNameGeneration(ReplaceUnderscores.class)
@ExtendWith(MockitoExtension.class)
class StompEventListenerTest {

  @InjectMocks
  StompEventListener stompEventListener;

  @Mock
  ChatUserRepository chatUserRepository;

  final String SESSION_ID = "123-123";
  final String USERNAME = "1";

  final String DESTINATION = "/sub/rooms/1";


  @Test
  void SessionConnectedEvent는_유저의_세션정보를_저장한다() {
    Authentication authentication = createAuthentication();
    StompHeaderAccessor accessor = StompHeaderAccessor.create(StompCommand.CONNECTED);
    accessor.setSessionId(SESSION_ID);
    Message<byte[]> message = MessageBuilder.createMessage(new byte[0],
        accessor.getMessageHeaders());
    SessionConnectedEvent event = new SessionConnectedEvent(this, message, authentication);

    doNothing().when(chatUserRepository).save(anyString(), anyString());

    stompEventListener.connectedHandler(event);

    verify(chatUserRepository, times(1)).save(anyString(), anyString());
  }


  @Test
  void SessionSubscribeEvent는_구독정보를_저장한다() {
    Authentication authentication = createAuthentication();
    StompHeaderAccessor accessor = StompHeaderAccessor.create(StompCommand.SUBSCRIBE);
    accessor.setSessionId(SESSION_ID);
    accessor.setDestination(DESTINATION);
    Message<byte[]> message = MessageBuilder.createMessage(new byte[0],
        accessor.getMessageHeaders());

    doNothing().when(chatUserRepository).subscribe(anyString(), anyString());

    SessionSubscribeEvent event = new SessionSubscribeEvent(this, message, authentication);

    stompEventListener.subscribeHandler(event);

    verify(chatUserRepository, times(1)).subscribe(anyString(), anyString());
  }

  @Test
  void SessionUnsubscribeEvent는_구독정보를_삭제한다() {
    Authentication authentication = createAuthentication();
    StompHeaderAccessor accessor = StompHeaderAccessor.create(StompCommand.UNSUBSCRIBE);
    accessor.setSessionId(SESSION_ID);
    accessor.setDestination(DESTINATION);
    Message<byte[]> message = MessageBuilder.createMessage(new byte[0],
        accessor.getMessageHeaders());

    doNothing().when(chatUserRepository).unsubscribe(anyString());

    SessionUnsubscribeEvent event = new SessionUnsubscribeEvent(this, message, authentication);

    stompEventListener.unsubscribeHandler(event);

    verify(chatUserRepository, times(1)).unsubscribe(anyString());
  }

  @Test
  void SessionDisconnectEvent는_유저_세션정보와_구독정보를_삭제한다() {
    Authentication authentication = createAuthentication();
    StompHeaderAccessor accessor = StompHeaderAccessor.create(StompCommand.DISCONNECT);
    Message<byte[]> message = MessageBuilder.createMessage(new byte[0],
        accessor.getMessageHeaders());

    doNothing().when(chatUserRepository).remove(anyString());
    doNothing().when(chatUserRepository).unsubscribe(anyString());

    SessionDisconnectEvent event = new SessionDisconnectEvent(this, message, SESSION_ID,
        CloseStatus.NORMAL, authentication);

    stompEventListener.disconnectHandler(event);

    verify(chatUserRepository, times(1)).remove(anyString());
    verify(chatUserRepository, times(1)).unsubscribe(anyString());
  }

  @Test
  void SessionDisconnectEvent는_인증정보가_없으면_삭제작업을_수행하지_않는다() {
    StompHeaderAccessor accessor = StompHeaderAccessor.create(StompCommand.DISCONNECT);
    Message<byte[]> message = MessageBuilder.createMessage(new byte[0],
        accessor.getMessageHeaders());

    SessionDisconnectEvent event = new SessionDisconnectEvent(this, message, SESSION_ID,
        CloseStatus.NORMAL, null);

    stompEventListener.disconnectHandler(event);

    verify(chatUserRepository, never()).remove(anyString());
    verify(chatUserRepository, never()).unsubscribe(anyString());
  }


  Authentication createAuthentication() {
    UserDetails user = User.builder().username(USERNAME).password("").authorities(ROLE_USER.name())
                           .build();
    return new UsernamePasswordAuthenticationToken(user, "", user.getAuthorities());
  }

}