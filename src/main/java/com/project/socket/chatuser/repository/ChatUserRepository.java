package com.project.socket.chatuser.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ChatUserRepository {

  private static final String CHAT_PARTICIPANT = "chat_participant:";
  private static final String DESTINATION = "destination";
  private final RedisTemplate<String, Object> redisTemplate;

  public void save(String sessionId, String username) {
    redisTemplate.opsForValue().set(sessionId, username);
  }

  public void remove(String sessionId) {
    redisTemplate.delete(sessionId);
  }

  public void subscribe(String username, String destination) {
    redisTemplate.opsForHash()
                 .put(CHAT_PARTICIPANT + username, DESTINATION, destination);
  }

  public void unsubscribe(String username) {
    redisTemplate.opsForHash().delete(CHAT_PARTICIPANT + username, DESTINATION);
  }
}
