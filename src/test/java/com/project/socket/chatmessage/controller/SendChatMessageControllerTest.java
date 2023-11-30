package com.project.socket.chatmessage.controller;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.project.socket.chatmessage.controller.dto.request.SendChatMessageRequest;
import com.project.socket.chatmessage.model.ChatMessage;
import com.project.socket.chatmessage.service.usecase.SendChatMessageUseCase;
import com.project.socket.chatroom.model.ChatRoom;
import com.project.socket.user.model.User;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@DisplayNameGeneration(ReplaceUnderscores.class)
class SendChatMessageControllerTest {

  @LocalServerPort
  private int port;
  private String URL;
  private WebSocketStompClient stompClient;
  @MockBean
  private SendChatMessageUseCase sendChatMessageUseCase;
  private static final String SEND_MESSAGE_ENDPOINT = "/pub/rooms/";
  private static final String SUBSCRIBE_CHAT_ROOM_ENDPOINT = "/sub/rooms/";

  @BeforeEach
  void setup() {
    URL = "ws://localhost:" + port + "/ws/chat";
    stompClient = new WebSocketStompClient(new StandardWebSocketClient());
    stompClient.setMessageConverter(new MappingJackson2MessageConverter());
  }

  @Test
  void 메세지를_발행하면_endpoint를_구독하는_클라이언트에게_전송된다() throws Exception {
    final Long ROOM_ID = 1L;
    StompSession stompSession = stompClient.connectAsync(URL, new StompSessionHandlerAdapter() {
    }).get(1, SECONDS);

    MessageFrameHandler<byte[]> handler = new MessageFrameHandler<>(
        byte[].class);

    ChatMessage givenMessage = createChatMessage();
    when(sendChatMessageUseCase.sendChatMessage(any())).thenReturn(givenMessage);

    stompSession.subscribe(SUBSCRIBE_CHAT_ROOM_ENDPOINT + ROOM_ID, handler);
    stompSession.send(SEND_MESSAGE_ENDPOINT + ROOM_ID, new SendChatMessageRequest(2L, 1L, "hi"));

    byte[] chatMessageByte = handler.completableFuture.get(10, SECONDS);
    String json = new String(chatMessageByte);

    assertThat(json).contains("hi");
  }

  private class MessageFrameHandler<T> implements StompFrameHandler {

    private final CompletableFuture<T> completableFuture = new CompletableFuture<>();

    private final Class<T> tClass;

    public MessageFrameHandler(Class<T> tClass) {
      this.tClass = tClass;
    }

    @Override
    public Type getPayloadType(StompHeaders headers) {
      return tClass;
    }

    @Override
    public void handleFrame(StompHeaders headers, Object payload) {
      completableFuture.complete((T) payload);
    }
  }

  ChatMessage createChatMessage() throws Exception {
    ChatMessage givenMessage = ChatMessage.builder().chatMessageId(1L).readCount(1).content("hi")
                                          .chatRoom(ChatRoom.builder().chatRoomId(1L).build())
                                          .sender(
                                              User.builder().userId(1L).build()).build();

    Field createdAtField = givenMessage.getClass().getSuperclass().getDeclaredField("createdAt");
    createdAtField.setAccessible(true);
    createdAtField.set(givenMessage, LocalDateTime.now());
    return givenMessage;
  }
}
