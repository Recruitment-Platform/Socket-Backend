package com.project.socket.comment.controller;

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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.socket.comment.model.CommentStatus;
import com.project.socket.comment.service.usecase.CommentOfPostDto;
import com.project.socket.comment.service.usecase.GetAllCommentsOfPostUseCase;
import com.project.socket.common.annotation.CustomWebMvcTestWithRestDocs;
import com.project.socket.docs.DocumentLinkGenerator;
import com.project.socket.docs.DocumentLinkGenerator.DocUrl;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;

@CustomWebMvcTestWithRestDocs(GetAllCommentsOfPostController.class)
class GetAllCommentsOfPostControllerTest {

  @Autowired
  MockMvc mockMvc;

  @Autowired
  ObjectMapper objectMapper;

  @MockBean
  GetAllCommentsOfPostUseCase getAllCommentsOfPostUseCase;

  final Long POST_ID = 1L;
  final String DELETED_CONTENT = "삭제된 댓글입니다";


  @Test
  void 조회결과가_없으면_body가_빈_배열인_200_응답을_한다() throws Exception {
    when(getAllCommentsOfPostUseCase.apply(anyLong())).thenReturn(Map.of());

    mockMvc.perform(get("/posts/{postId}/comments", POST_ID))
           .andExpectAll(
               status().isOk(),
               content().string("[]")
           );
  }

  @Test
  void postId_parameter가_1보다_작으면_400_응답을_한다() throws Exception {
    mockMvc.perform(get("/posts/{postId}/comments", -1L))
           .andExpect(status().isBadRequest());
  }

  @Test
  void 요청이_유효하면_포스트의_댓글_목록이_담긴_200_응답을_한다() throws Exception {
    when(getAllCommentsOfPostUseCase.apply(anyLong())).thenReturn(createUseCaseResult());

    mockMvc.perform(get("/posts/{postId}/comments", POST_ID))
           .andExpectAll(
               status().isOk(),
               jsonPath("$.length()").value(3),

               jsonPath("$[2].childComments").isEmpty()
           )
        .andDo(print())
        .andDo(document("getAllCommentsOfPost",
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            pathParameters(
                parameterWithName("postId").description("댓글의 포스트 ID")
            ),
            responseFields(
                fieldWithPath("[].commentId").type(JsonFieldType.NUMBER).description("부모 댓글 ID"),
                fieldWithPath("[].content").type(JsonFieldType.STRING).description("댓글 내용"),
                fieldWithPath("[].commentStatus").description(
                    DocumentLinkGenerator.generateLinkCode(DocUrl.COMMENT_STATUS)),
                fieldWithPath("[].createdAt").type(JsonFieldType.STRING).description("댓글 생성일"),
                fieldWithPath("[].writerId").description("댓글 작성자 ID"),
                fieldWithPath("[].writerNickname").description("댓글 작성자 닉네임"),
                fieldWithPath("[].childComments[].commentId").type(JsonFieldType.NUMBER).description("자식 댓글 ID"),
                fieldWithPath("[].childComments[].content").type(JsonFieldType.STRING).description("댓글 내용"),
                fieldWithPath("[].childComments[].commentStatus").description(
                    DocumentLinkGenerator.generateLinkCode(DocUrl.COMMENT_STATUS)),
                fieldWithPath("[].childComments[].createdAt").type(JsonFieldType.STRING).description("댓글 생성일"),
                fieldWithPath("[].childComments[].writerId").description("댓글 작성자 ID"),
                fieldWithPath("[].childComments[].writerNickname").description("댓글 작성자 닉네임"),
                fieldWithPath("[2].childComments").ignored()
            )));

  }

  Map<Long, List<CommentOfPostDto>> createUseCaseResult() {
    LinkedHashMap<Long, List<CommentOfPostDto>> result = new LinkedHashMap<>();
    result.put(1L, List.of(
        new CommentOfPostDto(1L, "content", CommentStatus.CREATED, null, LocalDateTime.now(), 1L,
            "nickname"),
        new CommentOfPostDto(2L, DELETED_CONTENT, CommentStatus.DELETED, 1L, LocalDateTime.now(),
            1L, "nickname"),
        new CommentOfPostDto(3L, "content", CommentStatus.CREATED, 1L, LocalDateTime.now(), 1L,
            "nickname")
    ));
    result.put(4L, List.of(
        new CommentOfPostDto(4L, "content", CommentStatus.CREATED, null, LocalDateTime.now(), 1L,
            "nickname"),
        new CommentOfPostDto(5L, DELETED_CONTENT, CommentStatus.DELETED, 4L, LocalDateTime.now(),
            1L, "nickname")
        ));
    result.put(6L, List.of(
        new CommentOfPostDto(6L, "content", CommentStatus.CREATED, null, LocalDateTime.now(), 1L,
            "nickname")
        ));

    return result;
  }

}