package com.project.socket.comment.controller;

import static org.hamcrest.core.StringContains.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.socket.comment.controller.dto.request.AddCommentRequestDto;
import com.project.socket.comment.model.Comment;
import com.project.socket.comment.service.usecase.AddCommentUseCase;
import com.project.socket.common.annotation.CustomWebMvcTestWithRestDocs;
import com.project.socket.docs.RestDocsAttributeFactory;
import com.project.socket.post.exception.PostNotFoundException;
import com.project.socket.user.exception.UserNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@CustomWebMvcTestWithRestDocs(AddCommentController.class)
class AddCommentControllerTest {

  @Autowired
  MockMvc mockMvc;

  @MockBean
  AddCommentUseCase addCommentUseCase;

  @Autowired
  ObjectMapper objectMapper;

  final Long POST_ID = 1L;

  @Test
  @WithMockUser(username = "1", authorities = "ROLE_USER")
  void 요청이_유효하면_201_응답을_한다() throws Exception {
    AddCommentRequestDto requestBody = new AddCommentRequestDto("content");

    when(addCommentUseCase.apply(any())).thenReturn(Comment.builder().id(1L).build());

    mockMvc.perform(post("/posts/{postId}/comments", POST_ID)
               .header("Authorization", "Bearer access-token")
               .contentType(APPLICATION_JSON)
               .content(objectMapper.writeValueAsBytes(requestBody)))
           .andExpectAll(
               status().isCreated(),
               header().string("Location", containsString("comments/1"))
           )
           .andDo(
               document("addComment",
                   preprocessRequest(prettyPrint()),
                   preprocessResponse(prettyPrint()),
                   requestHeaders(
                       headerWithName("Authorization").description("Bearer access-token")
                   ),
                   requestFields(
                       fieldWithPath("content")
                           .type(JsonFieldType.STRING).description("댓글 내용")
                           .attributes(RestDocsAttributeFactory.constraintsField("널 x, 공백 x"))
                   ),
                   responseHeaders(
                       headerWithName("Location").description("생성된 댓글 경로")
                   )
               )
           );
  }

  @Test
  @WithMockUser(username = "1", authorities = "ROLE_USER")
  void PostNotFoundException_예외가_발생하면_404_응답을_한다() throws Exception {
    AddCommentRequestDto requestBody = new AddCommentRequestDto("content");

    when(addCommentUseCase.apply(any())).thenThrow(new PostNotFoundException(1L));

    mockMvc.perform(post("/posts/{postId}/comments", POST_ID)
               .contentType(APPLICATION_JSON)
               .content(objectMapper.writeValueAsBytes(requestBody)))
           .andExpect(status().isNotFound());
  }

  @Test
  @WithMockUser(username = "1", authorities = "ROLE_USER")
  void UserNotFoundException_예외가_발생하면_404_응답을_한다() throws Exception {
    AddCommentRequestDto requestBody = new AddCommentRequestDto("content");

    when(addCommentUseCase.apply(any())).thenThrow(new UserNotFoundException(1L));

    mockMvc.perform(post("/posts/{postId}/comments", POST_ID)
               .contentType(APPLICATION_JSON)
               .content(objectMapper.writeValueAsBytes(requestBody)))
           .andExpect(status().isNotFound());
  }

  @Test
  @WithMockUser(username = "1", authorities = "ROLE_USER")
  void postId_parameter가_1보다_작으면_400_응답을_한다() throws Exception {
    AddCommentRequestDto requestBody = new AddCommentRequestDto("content");

    mockMvc.perform(post("/posts/{postId}/comments", -1L)
               .contentType(APPLICATION_JSON)
               .content(objectMapper.writeValueAsBytes(requestBody)))
           .andExpect(status().isBadRequest());
  }

  @ParameterizedTest(name = "content가_{0}이면_400_응답을_한다")
  @NullAndEmptySource
  @ValueSource(strings = {" "})
  @WithMockUser(username = "1", authorities = "ROLE_USER")
  void AddCommentRequestDto_not_valid(String content) throws Exception {
    AddCommentRequestDto requestBody = new AddCommentRequestDto(content);

    mockMvc.perform(post("/posts/{postId}/comments", POST_ID)
               .contentType(APPLICATION_JSON)
               .content(objectMapper.writeValueAsBytes(requestBody)))
           .andExpect(status().isBadRequest());
  }
}