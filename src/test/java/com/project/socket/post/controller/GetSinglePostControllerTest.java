package com.project.socket.post.controller;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.socket.common.annotation.CustomWebMvcTestWithRestDocs;
import com.project.socket.docs.DocumentLinkGenerator;
import com.project.socket.docs.DocumentLinkGenerator.DocUrl;
import com.project.socket.post.exception.PostDeletedException;
import com.project.socket.post.exception.PostNotFoundException;
import com.project.socket.post.model.PostMeeting;
import com.project.socket.post.model.PostStatus;
import com.project.socket.post.model.PostType;
import com.project.socket.post.service.usecase.GetSinglePostUseCase;
import com.project.socket.post.service.usecase.PostDto;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;

@CustomWebMvcTestWithRestDocs(GetSinglePostController.class)
public class GetSinglePostControllerTest {

  @Autowired
  MockMvc mockMvc;

  @Autowired
  ObjectMapper objectMapper;

  @MockBean
  GetSinglePostUseCase getSinglePostUseCase;

  final Long POST_ID = 1L;


  @Test
  void 요청이_유효하면_포스트_아이디에_해당하는_게시물_조회와_200_응답을_한다() throws Exception {
    when(getSinglePostUseCase.getPostDetail(anyLong())).thenReturn(createUseCaseResult());

    mockMvc.perform(get("/posts/{postId}", POST_ID))
        .andExpect(status().isOk())
        .andDo(print())
        .andDo(document("getSinglePost",
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            pathParameters(
                parameterWithName("postId").description("조회할 포스트 ID")
            ),
            responseFields(
                fieldWithPath("postId").type(JsonFieldType.NUMBER).description("포스트 ID"),
                fieldWithPath("title").type(JsonFieldType.STRING).description("포스트 제목"),
                fieldWithPath("postContent").type(JsonFieldType.STRING).description("포스트 본문 내용"),
                fieldWithPath("postType").description(
                    DocumentLinkGenerator.generateLinkCode(DocUrl.POST_TYPE)),
                fieldWithPath("postMeeting").description(
                    DocumentLinkGenerator.generateLinkCode(DocUrl.POST_MEETING)),
                fieldWithPath("postStatus").description(
                    DocumentLinkGenerator.generateLinkCode(DocUrl.POST_STATUS)),
                fieldWithPath("userId").description("게시물 작성자 ID"),
                fieldWithPath("userNickname").description("게시물 작성자 닉네임"),
                fieldWithPath("createdAt").type(JsonFieldType.STRING).description("게시물 생성일")
            )));
  }

  private PostDto createUseCaseResult() {
    return new PostDto(1L, "title", "content", PostType.STUDY, PostMeeting.ONLINE,
        PostStatus.CREATED, 1L, "nickname", LocalDateTime.now());
  }

  @Test
  void postId_parameter가_1보다_작으면_400_응답을_한다() throws Exception {
    mockMvc.perform(get("/posts/{postId}", -1L))
        .andExpect(status().isBadRequest());
  }

  @Test
  void 조회결과가_없어_PostNotFoundException_예외가_발생하면_404_응답을_한다() throws Exception {
    when(getSinglePostUseCase.getPostDetail(anyLong())).thenThrow(new PostNotFoundException(1L));

    mockMvc.perform(get("/posts/{postId}", 1L))
        .andExpect(status().isNotFound());
  }

  @Test
  void 조회결과_게시물이_삭제되어_PostDeletedException_예외가_발생하면_404_응답을_한다() throws Exception {
    when(getSinglePostUseCase.getPostDetail(anyLong())).thenThrow(new PostDeletedException(1L));

    mockMvc.perform(get("/posts/{postId}", 1L))
        .andExpect(status().isNotFound());
  }

}
