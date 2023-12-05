package com.project.socket.post.controller;

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
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.socket.common.annotation.CustomWebMvcTestWithRestDocs;
import com.project.socket.docs.RestDocsAttributeFactory;
import com.project.socket.post.controller.dto.request.PostModifyRequestDto;
import com.project.socket.post.exception.InvalidPostRelationException;
import com.project.socket.post.exception.PostNotFoundException;
import com.project.socket.post.model.Post;
import com.project.socket.post.model.PostMeeting;
import com.project.socket.post.model.PostType;
import com.project.socket.post.service.usecase.PostModifyUseCase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@CustomWebMvcTestWithRestDocs(PostModifyController.class)
public class PostModifyControllerTest {

  @Autowired
  MockMvc mockMvc;

  @Autowired
  ObjectMapper objectMapper;

  @MockBean
  PostModifyUseCase postModifyUseCase;

  final Long POST_ID = 1L;


  PostModifyRequestDto validRequest = new PostModifyRequestDto("modifyTitle", "modifyPostContent",
      PostType.STUDY, PostMeeting.ON_OFFLINE);


  @Test
  @WithMockUser(username = "1", authorities = "ROLE_USER")
  void 요청이_유효하면_성공적으로_게시물_변경하고_200_응답을_한다() throws Exception {
    when(postModifyUseCase.modifyPostNew(any())).thenReturn(Post.builder().id(1L).build());

    mockMvc.perform(patch("/posts/{postId}", POST_ID)
            .header("Authorization", "Bearer accessToken")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsBytes(validRequest)))
        .andExpectAll(
            status().isOk()
        )
        .andDo(
            document("modifyPost",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                pathParameters(
                    parameterWithName("postId").description("수정할 포스트 ID")
                ),
                requestHeaders(
                    headerWithName("Authorization").description("Bearer access-token")
                ),
                requestFields(
                    fieldWithPath("title")
                        .type(JsonFieldType.STRING).description("수정할 게시물 제목")
                        .attributes(RestDocsAttributeFactory.constraintsField("널 O, 공백 x")),
                    fieldWithPath("postContent")
                        .type(JsonFieldType.STRING).description("수정할 게시물 내용")
                        .attributes(RestDocsAttributeFactory.constraintsField("널 O, 공백 x")),
                    fieldWithPath("postType")
                        .type(JsonFieldType.STRING).description("수정할 PROJECT/STUDY")
                        .attributes(RestDocsAttributeFactory.constraintsField("널 O, 공백 x")),
                    fieldWithPath("postMeeting")
                        .type(JsonFieldType.STRING).description("수정할 ONLINE/OFFLINE/ON_OFFLINE")
                        .attributes(RestDocsAttributeFactory.constraintsField("널 O, 공백 x"))
                )
            )
        );
  }

  @Test
  @WithMockUser(username = "1", authorities = "ROLE_USER")
  void PostNotFoundException_예외가_발생하면_404_응답을_한다() throws Exception {
    when(postModifyUseCase.modifyPostNew(any())).thenThrow(new PostNotFoundException(1L));

    mockMvc.perform(patch("/posts/{postId}", POST_ID)
            .header("Authorization", "Bearer accessToken")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsBytes(validRequest)))
        .andExpectAll(
            status().isNotFound()
        );
  }

  @Test
  @WithMockUser(username = "1", authorities = "ROLE_USER")
  void InvalidPostRelationException_예외가_발생하면_400_응답을_한다() throws Exception {
    when(postModifyUseCase.modifyPostNew(any())).thenThrow(new InvalidPostRelationException());

    mockMvc.perform(patch("/posts/{postId}", POST_ID)
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
    mockMvc.perform(patch("/posts/{postId}", -1L)
            .contentType(APPLICATION_JSON)
            .content(objectMapper.writeValueAsBytes(validRequest)))
        .andExpect(status().isBadRequest());

  }

  @ParameterizedTest(name = "변경한_title이_{0}이면_400_응답을_한다")
  @EmptySource
  @ValueSource(strings = {" "})
  @WithMockUser(username = "1", authorities = "ROLE_USER")
  void PostModifyRequestDto_title_not_valid(String title) throws Exception {

    PostModifyRequestDto requestBody = new PostModifyRequestDto(title, "test_postContent",
        PostType.PROJECT,
        PostMeeting.ONLINE);

    mockMvc.perform(patch("/posts/{postId}", POST_ID)
            .contentType(APPLICATION_JSON)
            .content(objectMapper.writeValueAsBytes(requestBody)))
        .andExpect(status().isBadRequest())
        .andDo(print());
  }

  @ParameterizedTest(name = "변경한_postContent가_{0}이면_400_응답을_한다")
  @EmptySource
  @ValueSource(strings = {" "})
  @WithMockUser(username = "1", authorities = "ROLE_USER")
  void PostModifyRequestDto_postContent_not_valid(String postContent) throws Exception {

    PostModifyRequestDto requestBody = new PostModifyRequestDto("test_title", postContent,
        PostType.PROJECT, PostMeeting.ONLINE);

    mockMvc.perform(patch("/posts/{postId}", POST_ID)
            .contentType(APPLICATION_JSON)
            .content(objectMapper.writeValueAsBytes(requestBody)))
        .andExpect(status().isBadRequest())
        .andDo(print());
  }

  @Test
  @WithMockUser(username = "1", authorities = "ROLE_USER")
  void PostModifyRequestDto_postType_isEmpty_not_valid_400_응답을_한다() throws Exception {

    mockMvc.perform(patch("/posts/{postId}", POST_ID)
            .contentType(APPLICATION_JSON)
            .content(
                "{\"title\":\"test_title\",\"postContent\":\"test_postContent\",\"postType\":\"\",\"postMeeting\":\"ONLINE\"}"
            )
        )
        .andExpect(status().isBadRequest());
  }

  @Test
  @WithMockUser(username = "1", authorities = "ROLE_USER")
  void PostModifyRequestDto_postType_isBlank_not_valid_400_응답을_한다() throws Exception {

    mockMvc.perform(patch("/posts/{postId}", POST_ID)
            .contentType(APPLICATION_JSON)
            .content(
                "{\"title\":\"test_title\",\"postContent\":\"test_postContent\",\"postType\":\" \",\"postMeeting\":\"ONLINE\"}"
            )
        )
        .andExpect(status().isBadRequest());
  }

  @Test
  @WithMockUser(username = "1", authorities = "ROLE_USER")
  void PostModifyRequestDto_postMeeting_isEmpty_not_valid_400_응답을_한다() throws Exception {

    mockMvc.perform(patch("/posts/{postId}", POST_ID)
            .contentType(APPLICATION_JSON)
            .content(
                "{\"title\":\"test_title\",\"postContent\":\"test_postContent\",\"postType\":\"PROJECT\",\"postMeeting\":\"\"}"
            )
        )
        .andExpect(status().isBadRequest());
  }

  @Test
  @WithMockUser(username = "1", authorities = "ROLE_USER")
  void PostModifyRequestDto_postMeeting_isBlank_not_valid_400_응답을_한다() throws Exception {

    mockMvc.perform(patch("/posts/{postId}", POST_ID)
            .contentType(APPLICATION_JSON)
            .content(
                "{\"title\":\"test_title\",\"postContent\":\"test_postContent\",\"postType\":\"PROJECT\",\"postMeeting\":\" \"}"
            )
        )
        .andExpect(status().isBadRequest());
  }

}
