package com.project.socket.post.controller;

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
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.socket.common.annotation.CustomWebMvcTestWithRestDocs;
import com.project.socket.docs.RestDocsAttributeFactory;
import com.project.socket.post.controller.dto.request.PostSaveRequestDto;
import com.project.socket.post.model.Post;
import com.project.socket.post.model.PostMeeting;
import com.project.socket.post.model.PostType;
import com.project.socket.post.service.usecase.PostSaveUseCase;
import com.project.socket.user.exception.UserNotFoundException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@CustomWebMvcTestWithRestDocs(PostSaveController.class)
class PostSaveControllerTest {

  @Autowired
  MockMvc mockMvc;

  @Autowired
  ObjectMapper objectMapper;

  @MockBean
  PostSaveUseCase postSaveUseCase;

  @Test
  @WithMockUser(username = "1", authorities = "ROLE_USER")
  void 요청이_유효하면_201_응답을_한다() throws Exception {

    PostSaveRequestDto requestBody = new PostSaveRequestDto("title", "content", PostType.PROJECT,
        PostMeeting.ONLINE);

    when(postSaveUseCase.createPost(any())).thenReturn(Post.builder().id(1L).build());

    mockMvc.perform(post("/posts")
            .header("Authorization", "Bearer access-token")
            .contentType(APPLICATION_JSON)
            .content(objectMapper.writeValueAsBytes(requestBody)))
        .andExpectAll(
            status().isCreated(),
            header().string("Location", containsString("posts/1"))
        )
        .andDo(
            document("savePost",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestHeaders(
                    headerWithName("Authorization").description("Bearer access-token")
                ),
                requestFields(
                    fieldWithPath("title")
                        .type(JsonFieldType.STRING).description("게시물 제목")
                        .attributes(RestDocsAttributeFactory.constraintsField("널 x, 공백 x")),
                    fieldWithPath("postContent")
                        .type(JsonFieldType.STRING).description("게시물 내용")
                        .attributes(RestDocsAttributeFactory.constraintsField("널 x, 공백 x")),
                    fieldWithPath("postType")
                        .type(JsonFieldType.STRING).description("PROJECT/STUDY")
                        .attributes(RestDocsAttributeFactory.constraintsField("널 x, 공백 x")),
                    fieldWithPath("postMeeting")
                        .type(JsonFieldType.STRING).description("ONLINE/OFFLINE/ON_OFFLINE")
                        .attributes(RestDocsAttributeFactory.constraintsField("널 x, 공백 x"))
                ),
                responseHeaders(
                    headerWithName("Location").description("생성된 게시물 경로")
                )
            )
        );


  }

  @Test
  @WithMockUser(username = "1", authorities = "ROLE_USER")
  void UserNotFoundException_예외가_발생하면_404_응답을_한다() throws Exception {
    PostSaveRequestDto requestBody = new PostSaveRequestDto("testTitle", "testContent",
        PostType.PROJECT, PostMeeting.ONLINE);

    Assertions.assertThat(requestBody.title()).isEqualTo("testTitle");
    when(postSaveUseCase.createPost(any())).thenThrow(new UserNotFoundException(1L));

    mockMvc.perform(post("/posts")
            .contentType(APPLICATION_JSON)
            .content(objectMapper.writeValueAsBytes(requestBody)))
        .andExpect(status().isNotFound());

  }

  @ParameterizedTest(name = "title이_{0}이면_400_응답을_한다")
  @NullAndEmptySource
  @ValueSource(strings = {" "})
  @WithMockUser(username = "1", authorities = "ROLE_USER")
  void PostSaveRequestDto_title_not_valid(String title) throws Exception {

    PostSaveRequestDto requestBody = new PostSaveRequestDto(title, "test_postContent",
        PostType.PROJECT,
        PostMeeting.ONLINE);

    mockMvc.perform(post("/posts")
            .contentType(APPLICATION_JSON)
            .content(objectMapper.writeValueAsBytes(requestBody)))
        .andExpect(status().isBadRequest())
        .andDo(print());
  }

  @ParameterizedTest(name = "postContent가_{0}이면_400_응답을_한다")
  @NullAndEmptySource
  @ValueSource(strings = {" "})
  @WithMockUser(username = "1", authorities = "ROLE_USER")
  void PostSaveRequestDto_postContent_not_valid(String postContent) throws Exception {

    PostSaveRequestDto requestBody = new PostSaveRequestDto("test_title", postContent,
        PostType.PROJECT, PostMeeting.ONLINE);

    mockMvc.perform(post("/posts")
            .contentType(APPLICATION_JSON)
            .content(objectMapper.writeValueAsBytes(requestBody)))
        .andExpect(status().isBadRequest())
        .andDo(print());
  }

  @Test
  @WithMockUser(username = "1", authorities = "ROLE_USER")
  void PostSaveRequestDto_postType_not_valid_400_응답을_한다() throws Exception {

    PostSaveRequestDto requestBody = new PostSaveRequestDto("test_title", "test_postContent",
        null, PostMeeting.ONLINE);

    mockMvc.perform(post("/posts")
            .contentType(APPLICATION_JSON)
            .content(objectMapper.writeValueAsBytes(requestBody)))
        .andExpect(status().isBadRequest())
        .andDo(print());
  }

  @Test
  @WithMockUser(username = "1", authorities = "ROLE_USER")
  void PostSaveRequestDto_postMeeting_not_valid_400_응답을_한다() throws Exception {

    PostSaveRequestDto requestBody = new PostSaveRequestDto("test_title", "test_postContent",
        PostType.PROJECT, null);

    mockMvc.perform(post("/posts")
            .contentType(APPLICATION_JSON)
            .content(objectMapper.writeValueAsBytes(requestBody)))
        .andExpect(status().isBadRequest());

  }

}


