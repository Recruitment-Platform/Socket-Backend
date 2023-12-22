package com.project.socket.common;

import static org.assertj.core.api.Assertions.assertThat;

import com.project.socket.config.ContainerBaseTest;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
@DisplayNameGeneration(ReplaceUnderscores.class)
class RedisContainerConnectTest extends ContainerBaseTest {

  @Autowired
  RedisTemplate<String, Object> redisTemplate;

  @Test
  void 레디스_컨테이너_연결_테스트() {
    String ping = redisTemplate.getConnectionFactory().getConnection().ping();
    final String PONG = "PONG";

    assertThat(ping).isEqualTo(PONG);
  }

}
