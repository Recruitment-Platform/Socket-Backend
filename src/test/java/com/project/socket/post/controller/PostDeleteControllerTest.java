package com.project.socket.post.controller;

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
import com.project.socket.common.annotation.CustomWebMvcTestWithRestDocs;
import com.project.socket.post.exception.InvalidPostRelationException;
import com.project.socket.post.exception.PostNotFoundException;
import com.project.socket.post.service.usecase.PostDeleteUseCase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@CustomWebMvcTestWithRestDocs(PostDeleteController.class)
public class PostDeleteControllerTest {

  @Autowired
  MockMvc mockMvc;

  @Autowired
  ObjectMapper objectMapper;

  @MockBean
  PostDeleteUseCase postDeleteUseCase;

  final Long POST_ID = 1L;

  @Test
  @WithMockUser(username = "1", authorities = "ROLE_USER")
  void 요청이_유효하면_204_응답을_한다() throws Exception {
    doNothing().when(postDeleteUseCase).deletePostOne(any());

    mockMvc.perform(delete("/posts/{postId}", POST_ID)
            .header("Authorization", "Bearer accessToken"))
        .andExpect(status().is2xxSuccessful())
        .andDo(
            document("deletePost",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                pathParameters(
                    parameterWithName("postId").description("삭제할 포스트 ID")
                ),
                requestHeaders(
                    headerWithName("Authorization").description("Bearer access-token")
                ))
        );
  }

  @Test
  @WithMockUser(username = "1", authorities = "ROLE_USER")
  void postId가_1보다_작으면_400_응답을_한다() throws Exception {
    mockMvc.perform(delete("/posts/{postId}", -1L))
        .andExpect(status().isBadRequest());
  }

  @Test
  @WithMockUser(username = "1", authorities = "ROLE_USER")
  void InvalidPostRelationException_예외가_발생하면_400_응답을_한다() throws Exception {
    doThrow(new InvalidPostRelationException()).when(postDeleteUseCase).deletePostOne(any());

    mockMvc.perform(delete("/posts/{postId}", POST_ID)
            .header("Authorization", "Bearer accessToken"))
        .andExpect(status().isBadRequest());
  }

  @Test
  @WithMockUser(username = "1", authorities = "ROLE_USER")
  void PostNotFoundException_예외가_발생하면_404_응답을_한다() throws Exception {
    doThrow(new PostNotFoundException(POST_ID)).when(postDeleteUseCase).deletePostOne(any());

    mockMvc.perform(delete("/posts/{postId}", POST_ID)
            .header("Authorization", "Bearer accessToken"))
        .andExpect(status().isNotFound());
  }
}