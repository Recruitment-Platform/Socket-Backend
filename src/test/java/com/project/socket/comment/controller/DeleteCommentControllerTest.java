package com.project.socket.comment.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.socket.comment.exception.CommentNotFoundException;
import com.project.socket.comment.exception.InvalidCommentRelationException;
import com.project.socket.comment.service.usecase.DeleteCommentUseCase;
import com.project.socket.common.annotation.CustomWebMvcTestWithRestDocs;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@CustomWebMvcTestWithRestDocs(DeleteCommentController.class)
class DeleteCommentControllerTest {

  @Autowired
  MockMvc mockMvc;

  @Autowired
  ObjectMapper objectMapper;

  @MockBean
  DeleteCommentUseCase deleteCommentUseCase;

  final Long POST_ID = 1L;
  final Long COMMENT_ID = 1L;

  @Test
  @WithMockUser(username = "1", authorities = "ROLE_USER")
  void 요청이_유효하면_204_응답을_한다() throws Exception {
    doNothing().when(deleteCommentUseCase).accept(any());
    mockMvc.perform(delete("/posts/{postId}/comments/{commentId}", POST_ID, COMMENT_ID)
               .header("Authorization", "Bearer accessToken"))
           .andExpect(status().is2xxSuccessful())
           .andDo(
               document("deleteComment",
                   preprocessRequest(prettyPrint()),
                   preprocessResponse(prettyPrint()),
                   pathParameters(
                       parameterWithName("postId").description("댓글의 포스트 ID"),
                       parameterWithName("commentId").description("삭제할 댓글 ID")
                   ),
                   requestHeaders(
                       headerWithName("Authorization").description("Bearer access-token")
                   ))
           );
  }

  @Test
  @WithMockUser(username = "1", authorities = "ROLE_USER")
  void postId_parameter가_1보다_작으면_400_응답을_한다() throws Exception {
    mockMvc.perform(delete("/posts/{postId}/comments/{commentId}", -1L, COMMENT_ID))
           .andExpect(status().isBadRequest());
  }

  @Test
  @WithMockUser(username = "1", authorities = "ROLE_USER")
  void commentId_parameter가_1보다_작으면_400_응답을_한다() throws Exception {
    mockMvc.perform(delete("/posts/{postId}/comments/{commentId}", POST_ID, -1L))
           .andExpect(status().isBadRequest());
  }

  @Test
  @WithMockUser(username = "1", authorities = "ROLE_USER")
  void CommentNotFoundException_예외가_발생하면_404_응답을_한다() throws Exception {
    doThrow(new CommentNotFoundException(1L)).when(deleteCommentUseCase).accept(any());

    mockMvc.perform(delete("/posts/{postId}/comments/{commentId}", POST_ID, COMMENT_ID)
               .header("Authorization", "Bearer accessToken"))
           .andExpect(status().isNotFound());
  }

  @Test
  @WithMockUser(username = "1", authorities = "ROLE_USER")
  void InvalidCommentRelationException_예외가_발생하면_400_응답을_한다() throws Exception {
    doThrow(new InvalidCommentRelationException()).when(deleteCommentUseCase).accept(any());

    mockMvc.perform(delete("/posts/{postId}/comments/{commentId}", POST_ID, COMMENT_ID)
               .header("Authorization", "Bearer accessToken"))
           .andExpect(status().isBadRequest());
  }

}