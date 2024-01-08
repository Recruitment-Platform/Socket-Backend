package com.project.socket.chatuser.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.project.socket.config.ContainerBaseTest;
import com.project.socket.config.RedisConfig;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.redis.DataRedisTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ActiveProfiles;

@DataRedisTest
@Import({ChatUserRepository.class, RedisConfig.class})
@DisplayNameGeneration(ReplaceUnderscores.class)
@ActiveProfiles("test")
class ChatUserRepositoryTest extends ContainerBaseTest {

  @Autowired
  ChatUserRepository chatUserRepository;

  @Autowired
  RedisTemplate<String, Object> redisTemplate;

  final String CHAT_PARTICIPANT = "chat_participant:";

  @Test
  void 유저정보를_저장한다() {
    final String sessionId = "1";
    final String username = "1";

    chatUserRepository.save(sessionId, username);

    String savedUsername = (String) redisTemplate.opsForValue().get(sessionId);

    assertThat(savedUsername).isEqualTo(username);
  }

  @Test
  void 구독정보를_저장한다() {
    final String username = "1";
    final String destination = "/sub/rooms/1";

    chatUserRepository.subscribe(username, destination);

    String savedDestination = (String) redisTemplate.opsForHash()
                                                    .get(CHAT_PARTICIPANT + username,
                                                        "destination");

    assertThat(savedDestination).isEqualTo(destination);
  }

  @Test
  void 구독정보를_삭제한다() {
    final String username = "1";
    final String destination = "/sub/rooms/1";

    chatUserRepository.subscribe(username, destination);
    chatUserRepository.unsubscribe(username);

    Boolean isExist = redisTemplate.opsForHash()
                                   .hasKey(CHAT_PARTICIPANT + username, "destination");

    assertThat(isExist).isFalse();
  }

  @Test
  void 유저정보를_삭제한다() {
    final String sessionId = "1";
    final String username = "1";

    chatUserRepository.save(sessionId, username);
    chatUserRepository.remove(sessionId);

    Object removedUsername = redisTemplate.opsForValue().get(sessionId);

    assertThat(removedUsername).isNull();
  }

}