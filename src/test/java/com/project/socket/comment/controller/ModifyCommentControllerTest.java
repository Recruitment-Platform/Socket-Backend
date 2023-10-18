package com.project.socket.comment.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.socket.comment.controller.dto.request.ModifyCommentRequestDto;
import com.project.socket.comment.exception.CommentNotFoundException;
import com.project.socket.comment.exception.InvalidCommentRelationException;
import com.project.socket.comment.model.Comment;
import com.project.socket.comment.service.usecase.ModifyCommentUseCase;
import com.project.socket.common.annotation.CustomWebMvcTestWithRestDocs;
import com.project.socket.docs.RestDocsAttributeFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@CustomWebMvcTestWithRestDocs(ModifyCommentController.class)
class ModifyCommentControllerTest {

  @Autowired
  MockMvc mockMvc;

  @Autowired
  ObjectMapper objectMapper;

  @MockBean
  ModifyCommentUseCase modifyCommentUseCase;

  final Long POST_ID = 1L;
  final Long COMMENT_ID = 1L;

  ModifyCommentRequestDto validRequest = new ModifyCommentRequestDto("modify");

  @Test
  @WithMockUser(username = "1", authorities = "ROLE_USER")
  void 요청이_유효하면_성공적으로_댓글을_변경하고_200_응답을_한다() throws Exception {
    when(modifyCommentUseCase.apply(any()))
        .thenReturn(Comment.builder().id(1L).build());
    mockMvc.perform(patch("/posts/{postId}/comments/{commentId}", POST_ID, COMMENT_ID)
               .header("Authorization", "Bearer accessToken")
               .contentType(MediaType.APPLICATION_JSON)
               .content(objectMapper.writeValueAsBytes(validRequest)))
           .andExpectAll(
               status().isOk()
           )
           .andDo(
               document("modifyComment",
                   preprocessRequest(prettyPrint()),
                   preprocessResponse(prettyPrint()),
                   pathParameters(
                       parameterWithName("postId").description("댓글의 포스트 ID"),
                       parameterWithName("commentId").description("수정할 댓글 ID")
                   ),
                   requestHeaders(
                       headerWithName("Authorization").description("Bearer access-token")
                   ),
                   requestFields(
                       fieldWithPath("content")
                           .type(JsonFieldType.STRING).description("수정할 댓글 내용")
                           .attributes(RestDocsAttributeFactory.constraintsField("널 x, 공백 x"))
                   )
               )
           );
  }

  @Test
  @WithMockUser(username = "1", authorities = "ROLE_USER")
  void CommentNotFoundException_예외가_발생하면_404_응답을_한다() throws Exception{
    when(modifyCommentUseCase.apply(any())).thenThrow(new CommentNotFoundException(1L));

    mockMvc.perform(patch("/posts/{postId}/comments/{commentId}", POST_ID, COMMENT_ID)
               .header("Authorization", "Bearer accessToken")
               .contentType(MediaType.APPLICATION_JSON)
               .content(objectMapper.writeValueAsBytes(validRequest)))
           .andExpectAll(
               status().isNotFound()
           );
  }

  @Test
  @WithMockUser(username = "1", authorities = "ROLE_USER")
  void InvalidCommentRelationException_예외가_발생하면_400_응답을_한다() throws Exception{
    when(modifyCommentUseCase.apply(any())).thenThrow(new InvalidCommentRelationException());

    mockMvc.perform(patch("/posts/{postId}/comments/{commentId}", POST_ID, COMMENT_ID)
               .header("Authorization", "Bearer accessToken")
               .contentType(MediaType.APPLICATION_JSON)
               .content(objectMapper.writeValueAsBytes(validRequest)))
           .andExpectAll(
               status().isBadRequest()
           );
  }

  @Test
  @WithMockUser(username = "1", authorities = "ROLE_USER")
  void postId_parameter가_1보다_작으면_400_응답을_한다() throws Exception {
    mockMvc.perform(patch("/posts/{postId}/comments/{commentId}", -1L, COMMENT_ID)
               .contentType(APPLICATION_JSON)
               .content(objectMapper.writeValueAsBytes(validRequest)))
           .andExpect(status().isBadRequest());
  }

  @Test
  @WithMockUser(username = "1", authorities = "ROLE_USER")
  void commentId_parameter가_1보다_작으면_400_응답을_한다() throws Exception {
    mockMvc.perform(patch("/posts/{postId}/comments/{commentId}", POST_ID, -1L)
               .contentType(APPLICATION_JSON)
               .content(objectMapper.writeValueAsBytes(validRequest)))
           .andExpect(status().isBadRequest());
  }

  @ParameterizedTest(name = "content가_{0}이면_400_응답을_한다")
  @NullAndEmptySource
  @ValueSource(strings = {" "})
  @WithMockUser(username = "1", authorities = "ROLE_USER")
  void ModifyCommentRequestDto_not_valid(String content) throws Exception {
    ModifyCommentRequestDto InvalidRequest = new ModifyCommentRequestDto(content);

    mockMvc.perform(patch("/posts/{postId}/comments/{commentId}", POST_ID, COMMENT_ID)
               .contentType(APPLICATION_JSON)
               .content(objectMapper.writeValueAsBytes(InvalidRequest)))
           .andExpect(status().isBadRequest());
  }
}