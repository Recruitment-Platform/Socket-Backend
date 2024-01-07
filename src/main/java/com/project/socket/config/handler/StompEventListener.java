package com.project.socket.config.handler;

import com.project.socket.chatuser.repository.ChatUserRepository;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;
import org.springframework.web.socket.messaging.SessionUnsubscribeEvent;

@Component
@RequiredArgsConstructor
@Slf4j
public class StompEventListener {

  private final ChatUserRepository chatUserRepository;

  @EventListener
  public void connectedHandler(SessionConnectedEvent event) {
    StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
    chatUserRepository.save(accessor.getSessionId(), event.getUser().getName());

    log.info("session ID :{}, username :{} CONNECT", accessor.getSessionId(),
        event.getUser().getName());
  }


  @EventListener
  public void subscribeHandler(SessionSubscribeEvent event) {
    StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
    String destination = accessor.getDestination();
    chatUserRepository.subscribe(event.getUser().getName(), destination);

    log.info("session ID :{}, username :{}, destination :{} SUBSCRIBED", accessor.getSessionId(),
        event.getUser().getName(), destination);
  }

  @EventListener
  public void unsubscribeHandler(SessionUnsubscribeEvent event) {
    StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
    String destination = accessor.getDestination();
    chatUserRepository.unsubscribe(event.getUser().getName());

    log.info("session ID :{}, username :{}, destination :{} UNSUBSCRIBED", accessor.getSessionId(),
        event.getUser().getName(), destination);
  }

  @EventListener
  public void disconnectHandler(SessionDisconnectEvent event) {
    if (Objects.nonNull(event.getUser())) {
      chatUserRepository.remove(event.getSessionId());
      chatUserRepository.unsubscribe(event.getUser().getName());
    }

    log.info("{} DISCONNECTED", event);
  }
}
