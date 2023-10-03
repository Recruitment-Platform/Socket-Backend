package com.project.socket.user.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.socket.common.error.ErrorCode;
import com.project.socket.config.SecurityTestConfig;
import com.project.socket.docs.RestDocsAttributeFactory;
import com.project.socket.security.filter.JwtFilter;
import com.project.socket.user.controller.dto.request.SignupRequestDto;
import com.project.socket.user.exception.DuplicatedNicknameException;
import com.project.socket.user.exception.UserNotFoundException;
import com.project.socket.user.model.User;
import com.project.socket.user.service.usecase.SignupUseCase;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.MethodArgumentNotValidException;

@WebMvcTest(controllers = SignupController.class,
    excludeFilters = @ComponentScan.Filter(
        type = FilterType.ASSIGNABLE_TYPE, classes = JwtFilter.class))
@AutoConfigureRestDocs
@Import(SecurityTestConfig.class)
@DisplayNameGeneration(ReplaceUnderscores.class)
class SignupControllerTest {

  @Autowired
  MockMvc mockMvc;

  @MockBean
  SignupUseCase signupUseCase;

  @Autowired
  ObjectMapper objectMapper;

  final String NICKNAME = "nickname";
  final String GITHUB_LINK = "https://github.com";

  @Test
  @WithMockUser(username = "1", authorities = "ROLE_USER")
  void 유효한_요청이_오면_업데이트_정보가_담긴_200_응답을_한다() throws Exception {
    SignupRequestDto requestDto = new SignupRequestDto(NICKNAME, GITHUB_LINK);
    User givenUser = User.builder().nickname(NICKNAME).githubLink(GITHUB_LINK).build();

    when(signupUseCase.signup(any())).thenReturn(givenUser);

    mockMvc.perform(patch("/signup")
               .header("Authorization", "Bearer access-token")
               .contentType(MediaType.APPLICATION_JSON)
               .content(objectMapper.writeValueAsBytes(requestDto)))
           .andExpectAll(
               status().isOk(),
               jsonPath("$.nickname").value(NICKNAME),
               jsonPath("$.githubLink").value(GITHUB_LINK)
           )
           .andExpect(status().isOk())
           .andDo(
               document("signup",
                   preprocessRequest(prettyPrint()),
                   preprocessResponse(prettyPrint()),
                   requestHeaders(
                       headerWithName("Authorization").description("Bearer access-token")
                   ),
                   requestFields(
                       fieldWithPath("nickname")
                           .type(JsonFieldType.STRING).description("닉네임")
                           .attributes(RestDocsAttributeFactory.constraintsField("널 x, 공백 x")),
                       fieldWithPath("githubLink")
                           .type(JsonFieldType.STRING).optional().description("깃허브 주소")
                           .attributes(RestDocsAttributeFactory.constraintsField("URL 형식"))
                   ),
                   responseFields(
                       fieldWithPath("nickname").description("업데이트 된 닉네임"),
                       fieldWithPath("githubLink").description("업데이트 된 깃허브 주소")
                   )
               )
           );
  }

  @Test
  @WithMockUser(username = "1", authorities = "ROLE_USER")
  void nickname_필드가_null이면_400_응답을_한다() throws Exception {
    SignupRequestDto requestDto = new SignupRequestDto(null, GITHUB_LINK);

    mockMvc.perform(patch("/signup")
               .header("Authorization", "Bearer access-token")
               .contentType(MediaType.APPLICATION_JSON)
               .content(objectMapper.writeValueAsBytes(requestDto)))
           .andExpectAll(
               status().isBadRequest(),
               jsonPath("$.code").value(ErrorCode.INVALID_INPUT_VALUE.getCode()),
               result -> assertThat(result.getResolvedException())
                   .isInstanceOf(MethodArgumentNotValidException.class)
           );
  }

  @Test
  @WithMockUser(username = "1", authorities = "ROLE_USER")
  void nickname_필드가_빈_문자열이면_400_응답을_한다() throws Exception {
    SignupRequestDto requestDto = new SignupRequestDto("", GITHUB_LINK);

    mockMvc.perform(patch("/signup")
               .header("Authorization", "Bearer access-token")
               .contentType(MediaType.APPLICATION_JSON)
               .content(objectMapper.writeValueAsBytes(requestDto)))
           .andExpectAll(
               status().isBadRequest(),
               jsonPath("$.code").value(ErrorCode.INVALID_INPUT_VALUE.getCode()),
               result -> assertThat(result.getResolvedException())
                   .isInstanceOf(MethodArgumentNotValidException.class)
           );
  }

  @Test
  @WithMockUser(username = "1", authorities = "ROLE_USER")
  void githubLink_필드가_URL_형식이_아니면_400_응답을_한다() throws Exception {
    SignupRequestDto requestDto = new SignupRequestDto(NICKNAME, "not url");

    mockMvc.perform(patch("/signup")
               .header("Authorization", "Bearer access-token")
               .contentType(MediaType.APPLICATION_JSON)
               .content(objectMapper.writeValueAsBytes(requestDto)))
           .andExpectAll(
               status().isBadRequest(),
               jsonPath("$.code").value(ErrorCode.INVALID_INPUT_VALUE.getCode()),
               result -> assertThat(result.getResolvedException())
                   .isInstanceOf(MethodArgumentNotValidException.class)
           );
  }

  @Test
  @WithMockUser(username = "1", authorities = "ROLE_USER")
  void 닉네임이_중복이면_400_응답을_한다() throws Exception {
    SignupRequestDto requestDto = new SignupRequestDto(NICKNAME, GITHUB_LINK);

    when(signupUseCase.signup(any())).thenThrow(new DuplicatedNicknameException());
    mockMvc.perform(patch("/signup")
               .header("Authorization", "Bearer access-token")
               .contentType(MediaType.APPLICATION_JSON)
               .content(objectMapper.writeValueAsBytes(requestDto)))
           .andExpectAll(
               status().isBadRequest(),
               jsonPath("$.code").value(ErrorCode.DUPLICATED_NICKNAME.getCode()),
               result -> assertThat(result.getResolvedException())
                   .isInstanceOf(DuplicatedNicknameException.class)
           );
  }

  @Test
  @WithMockUser(username = "1", authorities = "ROLE_USER")
  void 유저가_존재하지_않으면_404_응답을_한다() throws Exception {
    SignupRequestDto requestDto = new SignupRequestDto(NICKNAME, GITHUB_LINK);

    when(signupUseCase.signup(any())).thenThrow(new UserNotFoundException(1L));
    mockMvc.perform(patch("/signup")
               .header("Authorization", "Bearer access-token")
               .contentType(MediaType.APPLICATION_JSON)
               .content(objectMapper.writeValueAsBytes(requestDto)))
           .andDo(print())
           .andExpectAll(
               status().isNotFound(),
               jsonPath("$.code").value(ErrorCode.USER_NOT_FOUND.getCode()),
               result -> assertThat(result.getResolvedException())
                   .isInstanceOf(UserNotFoundException.class)
           );
  }
}