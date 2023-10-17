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
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.socket.comment.controller.dto.request.AddReplyRequestDto;
import com.project.socket.comment.exception.CommentNotFoundException;
import com.project.socket.comment.model.Comment;
import com.project.socket.comment.service.AddReplyService;
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

@CustomWebMvcTestWithRestDocs(AddReplyController.class)
class AddReplyControllerTest {

  @Autowired
  MockMvc mockMvc;

  @MockBean
  AddReplyService addReplyService;

  @Autowired
  ObjectMapper objectMapper;

  final Long POST_ID = 1L;
  final Long COMMENT_ID = 1L;

  @Test
  @WithMockUser(username = "1", authorities = "ROLE_USER")
  void 요청이_유효하면_201_응답을_한다() throws Exception {
    AddReplyRequestDto requestBody = new AddReplyRequestDto("content");

    when(addReplyService.apply(any())).thenReturn(Comment.builder().id(1L).build());

    mockMvc.perform(post("/posts/{postId}/comments/{commentId}/replies", POST_ID, COMMENT_ID)
               .header("Authorization", "Bearer access-token")
               .contentType(APPLICATION_JSON)
               .content(objectMapper.writeValueAsBytes(requestBody)))
           .andExpectAll(
               status().isCreated(),
               header().string("Location", containsString("replies/1"))
           )
           .andDo(
               document("addReply",
                   preprocessRequest(prettyPrint()),
                   preprocessResponse(prettyPrint()),
                   pathParameters(
                       parameterWithName("postId").description("대댓글을 추가할 포스트"),
                       parameterWithName("commentId").description("대댓글을 추가할 댓글")
                   ),
                   requestHeaders(
                       headerWithName("Authorization").description("Bearer access-token")
                   ),
                   requestFields(
                       fieldWithPath("content")
                           .type(JsonFieldType.STRING).description("댓글 내용")
                           .attributes(RestDocsAttributeFactory.constraintsField("널 x, 공백 x"))
                   ),
                   responseHeaders(
                       headerWithName("Location").description("생성된 대댓글 경로")
                   )
               )
           );
  }

  @Test
  @WithMockUser(username = "1", authorities = "ROLE_USER")
  void PostNotFoundException_예외가_발생하면_404_응답을_한다() throws Exception {
    AddReplyRequestDto requestBody = new AddReplyRequestDto("content");

    when(addReplyService.apply(any())).thenThrow(new PostNotFoundException(1L));

    mockMvc.perform(post("/posts/{postId}/comments/{commentId}/replies", POST_ID, COMMENT_ID)
               .contentType(APPLICATION_JSON)
               .content(objectMapper.writeValueAsBytes(requestBody)))
           .andExpect(status().isNotFound());
  }

  @Test
  @WithMockUser(username = "1", authorities = "ROLE_USER")
  void UserNotFoundException_예외가_발생하면_404_응답을_한다() throws Exception {
    AddReplyRequestDto requestBody = new AddReplyRequestDto("content");

    when(addReplyService.apply(any())).thenThrow(new UserNotFoundException(1L));

    mockMvc.perform(post("/posts/{postId}/comments/{commentId}/replies", POST_ID, COMMENT_ID)
               .contentType(APPLICATION_JSON)
               .content(objectMapper.writeValueAsBytes(requestBody)))
           .andExpect(status().isNotFound());
  }

  @Test
  @WithMockUser(username = "1", authorities = "ROLE_USER")
  void CommentNotFoundException_예외가_발생하면_404_응답을_한다() throws Exception {
    AddReplyRequestDto requestBody = new AddReplyRequestDto("content");

    when(addReplyService.apply(any())).thenThrow(new CommentNotFoundException(1L));

    mockMvc.perform(post("/posts/{postId}/comments/{commentId}/replies", POST_ID, COMMENT_ID)
               .contentType(APPLICATION_JSON)
               .content(objectMapper.writeValueAsBytes(requestBody)))
           .andExpect(status().isNotFound());
  }

  @Test
  @WithMockUser(username = "1", authorities = "ROLE_USER")
  void postId_parameter가_1보다_작으면_400_응답을_한다() throws Exception {
    AddReplyRequestDto requestBody = new AddReplyRequestDto("content");

    mockMvc.perform(post("/posts/{postId}/comments/{commentId}/replies", -1L, COMMENT_ID)
               .contentType(APPLICATION_JSON)
               .content(objectMapper.writeValueAsBytes(requestBody)))
           .andExpect(status().isBadRequest());
  }

  @Test
  @WithMockUser(username = "1", authorities = "ROLE_USER")
  void commentId_parameter가_1보다_작으면_400_응답을_한다() throws Exception {
    AddReplyRequestDto requestBody = new AddReplyRequestDto("content");

    mockMvc.perform(post("/posts/{postId}/comments/{commentId}/replies", POST_ID, -1L)
               .contentType(APPLICATION_JSON)
               .content(objectMapper.writeValueAsBytes(requestBody)))
           .andExpect(status().isBadRequest());
  }

  @ParameterizedTest(name = "content가_{0}이면_400_응답을_한다")
  @NullAndEmptySource
  @ValueSource(strings = {" "})
  @WithMockUser(username = "1", authorities = "ROLE_USER")
  void AddCommentRequestDto_not_valid(String content) throws Exception {
    AddReplyRequestDto requestBody = new AddReplyRequestDto(content);

    mockMvc.perform(post("/posts/{postId}/comments/{commentId}/replies", POST_ID, COMMENT_ID)
               .contentType(APPLICATION_JSON)
               .content(objectMapper.writeValueAsBytes(requestBody)))
           .andExpect(status().isBadRequest());
  }
}