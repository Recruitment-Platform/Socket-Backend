package com.project.socket.postskill.controller;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.socket.common.annotation.CustomWebMvcTestWithRestDocs;
import com.project.socket.docs.DocumentLinkGenerator;
import com.project.socket.docs.DocumentLinkGenerator.DocUrl;
import com.project.socket.post.exception.UnsupportedSortException;
import com.project.socket.post.model.PostMeeting;
import com.project.socket.post.model.PostStatus;
import com.project.socket.post.model.PostType;
import com.project.socket.post.service.usecase.PostDto;
import com.project.socket.postskill.service.usecase.GetAllPostsOfSkillUseCase;
import com.project.socket.skill.exception.SkillNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;

@CustomWebMvcTestWithRestDocs(GetAllPostsOfSkillController.class)
class GetAllPostsOfSkillControllerTest {

  @Autowired
  MockMvc mockMvc;

  @Autowired
  ObjectMapper objectMapper;

  @MockBean
  GetAllPostsOfSkillUseCase getAllPostsOfSkillUseCase;

  final String hashTag = "Java,Spring";
  final int page = 0;
  final int size = 5;
  final String order = "createdAt";


  @Test
  void 요청이_유효하면_해시태그_페이지_크기에_해당하는_게시물_조회와_200_응답을_한다() throws Exception {
    List<PostDto> postDtos = samplePostDtos();
    Pageable pageable = PageRequest.of(page, size, Direction.DESC, order);
    Page<PostDto> postDtoPage = new PageImpl<>(postDtos, pageable, postDtos.size());

    when(getAllPostsOfSkillUseCase.getPostsUsingSkill(eq(hashTag),
        any(Pageable.class))).thenReturn(postDtoPage);

    mockMvc.perform(get("/posts/projects")
            .param("hashtag", hashTag)
            .param("page", String.valueOf(page))
            .param("order", order))
        .andExpect(status().isOk())
        .andDo(print())
        .andDo(document("getAllPostsOfSkill",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                queryParameters(
                    parameterWithName("hashtag").description("해시 태그"),
                    parameterWithName("page").description("페이지 번호"),
                    parameterWithName("order").description("정렬 기준")
                ),
                responseFields(
                    // 게시물 정보 필드
                    fieldWithPath("content[].postId").type(JsonFieldType.NUMBER).description("포스트 ID"),
                    fieldWithPath("content[].title").type(JsonFieldType.STRING).description("포스트 제목"),
                    fieldWithPath("content[].postContent").type(JsonFieldType.STRING)
                        .description("포스트 본문 내용"),
                    fieldWithPath("content[].postType").description(
                        DocumentLinkGenerator.generateLinkCode(DocUrl.POST_TYPE)),
                    fieldWithPath("content[].postMeeting").description(
                        DocumentLinkGenerator.generateLinkCode(DocUrl.POST_MEETING)),
                    fieldWithPath("content[].postStatus").description(
                        DocumentLinkGenerator.generateLinkCode(DocUrl.POST_STATUS)),
                    fieldWithPath("content[].userId").description("게시물 작성자 ID"),
                    fieldWithPath("content[].userNickname").description("게시물 작성자 닉네임"),
                    fieldWithPath("content[].createdAt").type(JsonFieldType.STRING)
                        .description("게시물 생성일"),

                    fieldWithPath("pageMetaData.totalElements").type(JsonFieldType.NUMBER)
                        .description("전체 게시물 수"),
                    fieldWithPath("pageMetaData.totalPages").type(JsonFieldType.NUMBER)
                        .description("전체 페이지 수"),
                    fieldWithPath("pageMetaData.pageNumber").type(JsonFieldType.NUMBER)
                        .description("현재 페이지 번호"),
                    fieldWithPath("pageMetaData.pageSize").type(JsonFieldType.NUMBER)
                        .description("페이지당 게시물 수"),
                    fieldWithPath("pageMetaData.currentPageElements").type(JsonFieldType.NUMBER)
                        .description("현재 페이지의 게시물 수"),
                    fieldWithPath("pageMetaData.isFirstPage").type(JsonFieldType.BOOLEAN)
                        .description("현재 페이지가 첫번째 페이지인지 여부"),
                    fieldWithPath("pageMetaData.isLastPage").type(JsonFieldType.BOOLEAN)
                        .description("현재 페이지가 마지막 페이지인지 여부"),
                    fieldWithPath("pageMetaData.hasPreviousPage").type(JsonFieldType.BOOLEAN)
                        .description("이전 페이지가 있는지 여부"),
                    fieldWithPath("pageMetaData.hasNextPage").type(JsonFieldType.BOOLEAN)
                        .description("다음 페이지가 있는지 여부"),
                    fieldWithPath("pageMetaData.sortCriteria").type(JsonFieldType.STRING)
                        .description("필터링 기준"),
                    fieldWithPath("pageMetaData.sortDirection").type(JsonFieldType.STRING)
                        .description("정렬 방향 (ASC-오름차순 / DESC-내림차순)")

                )
            )
        );
  }

  @Test
  void hashtag_값이_null_이면_200_응답을_한다() throws Exception {
    Pageable pageable = PageRequest.of(page, size, Direction.DESC, order);

    when(getAllPostsOfSkillUseCase.getPostsUsingSkill(eq(null), any(Pageable.class)))
        .thenReturn(new PageImpl<>(samplePostDtos(), pageable, samplePostDtos().size()));

    mockMvc.perform(get("/posts/projects")
            .param("page", String.valueOf(page))
            .param("order", order))
        .andExpect(status().isOk());
  }


  @Test
  void hashtag_값이_빈값_이면_200_응답을_한다() throws Exception {
    Pageable pageable = PageRequest.of(page, size, Direction.DESC, order);

    when(getAllPostsOfSkillUseCase.getPostsUsingSkill(eq(""), any(Pageable.class)))
        .thenReturn(new PageImpl<>(samplePostDtos(), pageable, samplePostDtos().size()));

    mockMvc.perform(get("/posts/projects")
            .param("hashtag", "")
            .param("page", String.valueOf(page))
            .param("order", order))
        .andExpect(status().isOk());
  }

  @Test
  void 페이징의_page_parameter가_0보다_작으면_400_응답을_한다() throws Exception {
    mockMvc.perform(get("/posts/projects")
            .param("hashtag", hashTag)
            .param("page", String.valueOf(-1))
            .param("order", "createdAt"))
        .andExpect(status().isBadRequest());

  }

  @Test
  void 정렬조건인_order_가_null_일_경우_200_응답을_한다() throws Exception {
    Pageable pageable = PageRequest.of(page, size, Direction.DESC, order);

    when(getAllPostsOfSkillUseCase.getPostsUsingSkill(anyString(), any(Pageable.class)))
        .thenReturn(new PageImpl<>(samplePostDtos(), pageable, samplePostDtos().size()));

    mockMvc.perform(get("/posts/projects")
            .param("hashtag", hashTag)
            .param("page", String.valueOf(page)))
        .andExpect(status().isOk());
  }

  @Test
  void 정렬조건인_order_가_정렬기준에_없을경우_UnsupportedSortException_예외가_발생하며_404_응답을_한다() throws Exception {
    when(getAllPostsOfSkillUseCase.getPostsUsingSkill(anyString(), any(Pageable.class))).thenThrow(
        new UnsupportedSortException());

    mockMvc.perform(get("/posts/projects")
            .param("hashtag", hashTag)
            .param("page", String.valueOf(page))
            .param("order", "wrongOrder"))
        .andExpect(status().isBadRequest());
  }


  @Test
  void skillName의_조회결과가_없어_SkillNotFoundException_예외가_발생하면_404_응답을_한다() throws Exception {
    when(getAllPostsOfSkillUseCase.getPostsUsingSkill(anyString(), any(Pageable.class))).thenThrow(
        new SkillNotFoundException());

    mockMvc.perform(get("/posts/projects")
            .param("hashtag", hashTag)
            .param("page", String.valueOf(page))
            .param("order", order))
        .andExpect(status().isNotFound());

  }


  private List<PostDto> samplePostDtos() {
    return List.of(
        new PostDto(1L, "title1", "content1", PostType.PROJECT, PostMeeting.ONLINE,
            PostStatus.DELETED, 1L, "nickname1", LocalDateTime.now()),
        new PostDto(2L, "title2", "content2", PostType.STUDY, PostMeeting.OFFLINE,
            PostStatus.CREATED, 1L, "nickname2", LocalDateTime.now()),
        new PostDto(3L, "title3", "content3", PostType.PROJECT, PostMeeting.OFFLINE,
            PostStatus.MODIFIED, 1L, "nickname3", LocalDateTime.now()),
        new PostDto(4L, "title4", "content4", PostType.STUDY, PostMeeting.ONLINE,
            PostStatus.CREATED, 1L, "nickname4", LocalDateTime.now()),
        new PostDto(5L, "title5", "content5", PostType.STUDY, PostMeeting.ONLINE,
            PostStatus.CREATED, 1L, "nickname5", LocalDateTime.now())
    );
  }

}
