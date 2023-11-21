package com.project.socket.chatroom.controller;

import static org.hamcrest.core.StringContains.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.socket.chatroom.exception.WriterCanNotStartChatException;
import com.project.socket.chatroom.model.ChatRoom;
import com.project.socket.chatroom.service.usecase.CreateChatRoomUseCase;
import com.project.socket.common.annotation.CustomWebMvcTestWithRestDocs;
import com.project.socket.post.exception.PostNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@CustomWebMvcTestWithRestDocs(CreateChatRoomController.class)
class CreateChatRoomControllerTest {

  @Autowired
  MockMvc mockMvc;

  @MockBean
  CreateChatRoomUseCase createChatRoomUseCase;

  @Autowired
  ObjectMapper objectMapper;

  final Long POST_ID = 1L;

  @Test
  @WithMockUser(username = "1", authorities = "ROLE_USER")
  void 요청이_유효하면_201_응답을_한다() throws Exception {
    ChatRoom chatRoom = ChatRoom.builder().chatRoomId(1L).build();
    when(createChatRoomUseCase.apply(any())).thenReturn(chatRoom);

    mockMvc.perform(
               post("/posts/{postId}/chat-rooms", POST_ID)
                   .header("Authorization", "Bearer access-token")
           )
           .andExpectAll(
               status().isCreated(),
               header().string("Location", containsString("chat-rooms/1")),
               jsonPath("$.chatRoomId").value(1L)
           )
           .andDo(
               document("createChatRoom",
                   preprocessRequest(prettyPrint()),
                   preprocessResponse(prettyPrint()),
                   pathParameters(
                       parameterWithName("postId").description("채팅을 시작할 추가할 포스트 ID")
                   ),
                   requestHeaders(
                       headerWithName("Authorization").description("Bearer access-token")
                   ),
                   responseHeaders(
                       headerWithName("Location").description("생성된 채팅방 경로")
                   ),
                   responseFields(
                       fieldWithPath("chatRoomId").description("채팅방 ID")
                   )
               )
           );
  }

  @Test
  @WithMockUser(username = "1", authorities = "ROLE_USER")
  void postId_parameter가_1보다_작으면_400_응답을_한다() throws Exception {
    mockMvc.perform(post("/posts/{postId}/chat-rooms", -1L))
           .andExpect(status().isBadRequest());
  }

  @Test
  @WithMockUser(username = "1", authorities = "ROLE_USER")
  void PostNotFoundException_예외가_발생하면_404_응답을_한다() throws Exception {
    when(createChatRoomUseCase.apply(any())).thenThrow(new PostNotFoundException(POST_ID));

    mockMvc.perform(post("/posts/{postId}/chat-rooms", POST_ID))
           .andExpect(status().isNotFound());
  }

  @Test
  @WithMockUser(username = "1", authorities = "ROLE_USER")
  void WriterCanNotStartChatException_예외가_발생하면_400_응답을_한다() throws Exception {
    when(createChatRoomUseCase.apply(any())).thenThrow(new WriterCanNotStartChatException());

    mockMvc.perform(post("/posts/{postId}/chat-rooms", POST_ID))
           .andExpect(status().isBadRequest());
  }

}