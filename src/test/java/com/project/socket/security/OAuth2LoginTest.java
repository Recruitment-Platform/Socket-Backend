package com.project.socket.security;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@AutoConfigureRestDocs
@DisplayNameGeneration(ReplaceUnderscores.class)
class OAuth2LoginTest {

  @Autowired
  MockMvc mockMvc;

  @Test
  void 로그인_요청을_하면_해당_페이지로_302_리다이렉트한다() throws Exception {
    mockMvc.perform(get("/oauth2/authorization/{provider}", "google")
               .queryParam("redirect_url", "http://localhost:3000/login/callback"))
           .andExpect(status().is3xxRedirection())
           .andDo(document("oauth2-login-redirect",
               preprocessRequest(prettyPrint()),
               preprocessResponse(prettyPrint()),
               pathParameters(parameterWithName("provider").description("kakao, google, github")),
               queryParameters(
                   parameterWithName("redirect_url").description("로그인 이후 토큰 결과를 받을 URL"))
           ));
  }
}
