package com.project.socket.chatmessage.controller;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.project.socket.chatmessage.controller.dto.response.MessageType;
import com.project.socket.chatmessage.service.usecase.SendEnterMessageUseCase;
import com.project.socket.config.ContainerBaseTest;
import com.project.socket.security.JwtProvider;
import com.project.socket.user.model.Role;
import com.project.socket.user.model.User;
import java.net.URI;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.util.UriComponentsBuilder;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@DisplayNameGeneration(ReplaceUnderscores.class)
class SendEnterMessageControllerTest extends ContainerBaseTest {

  @LocalServerPort
  private int port;
  private String URL;
  private WebSocketStompClient stompClient;
  @MockBean
  private SendEnterMessageUseCase sendEnterMessageUseCase;
  final String SEND_MESSAGE_ENDPOINT = "/pub/enter/";
  final String SUBSCRIBE_CHAT_ROOM_ENDPOINT = "/sub/rooms/";

  @Autowired
  private JwtProvider jwtProvider;

  @BeforeEach
  void setup() {
    URL = "ws://localhost:" + port + "/ws/chat";
    stompClient = new WebSocketStompClient(new StandardWebSocketClient());
    stompClient.setMessageConverter(new MappingJackson2MessageConverter());
  }

  @Test
  void 채팅방_입장_메시지가_해당_채팅방을_구독하는_클라이언트에게_전송된다() throws Exception {
    final Long ROOM_ID = 1L;
    StompHeaders connectionHeaders = createConnectionHeaders();
    URI uri = UriComponentsBuilder.fromUriString(URL).buildAndExpand(List.of()).encode().toUri();

    StompSession stompSession = stompClient.connectAsync(uri, null, connectionHeaders,
        new StompSessionHandlerAdapter() {
        }).get(5, SECONDS);
    MessageFrameHandler<byte[]> handler = new MessageFrameHandler<>(
        byte[].class);

    when(sendEnterMessageUseCase.apply(any())).thenReturn(ROOM_ID);

    stompSession.subscribe(SUBSCRIBE_CHAT_ROOM_ENDPOINT + ROOM_ID, handler);
    stompSession.send(SEND_MESSAGE_ENDPOINT + ROOM_ID, null);

    byte[] chatMessageByte = handler.getCompletableFuture().get(10, SECONDS);
    String json = new String(chatMessageByte);
    stompSession.disconnect();

    assertThat(json).contains(MessageType.ENTER.name());
  }

  StompHeaders createConnectionHeaders() {
    User user = User.builder().userId(1L).role(Role.ROLE_USER).build();
    String accessToken = jwtProvider.createAccessToken(user);
    StompHeaders stompHeaders = new StompHeaders();
    stompHeaders.set("Authorization", accessToken);
    return stompHeaders;
  }
}